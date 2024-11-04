package net.jterminal.buffer;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.jterminal.text.ColorNamePalette;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.XtermColor;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.FontMap;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.IndexedStyleData;
import net.jterminal.text.termstring.IndexedStyleData.IndexEntry;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

class ByteBufImpl implements ByteBuf {

  private ByteBuffer buffer;
  private final int additionCapacity;

  public ByteBufImpl(@NotNull ByteBuffer byteBuffer, int additionCapacity) {
    this.buffer = byteBuffer;
    this.additionCapacity = additionCapacity;
  }

  public void ensureCapacity(int free) {
    if(additionCapacity == 0) {
      return;
    }
    int capacity = capacity();
    int pos = buffer.position();
    if(pos + free <= capacity) {
      return;
    }
    resize(capacity + (Math.max(free, additionCapacity)));
  }

  @Override
  public @NotNull ByteBuf write(byte[] bytes, int off, int len) {
    ensureCapacity(len);
    buffer.put(bytes, off, len);
    return this;
  }

  @Override
  public @NotNull ByteBuf write(byte[] bytes) {
    ensureCapacity(bytes.length);
    buffer.put(bytes, 0, bytes.length);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeByte(byte x) {
    ensureCapacity(1);
    buffer.put(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeByte(int x) {
    ensureCapacity(1);
    return writeByte((byte) x);
  }

  @Override
  public @NotNull ByteBuf writeBoolean(boolean x) {
    ensureCapacity(1);
    buffer.put((byte) (x ? 1 : 0));
    return this;
  }

  @Override
  public @NotNull ByteBuf writeChar(char x) {
    ensureCapacity(2);
    buffer.putChar(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeShort(short x) {
    ensureCapacity(2);
    buffer.putShort(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeUnsignedShort(int x) {
    ensureCapacity(2);
    buffer.putShort((short) x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeInt(int x) {
    ensureCapacity(4);
    buffer.putInt(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeUnsignedInt(long x) {
    ensureCapacity(4);
    buffer.putInt((int) x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeLong(long x) {
    ensureCapacity(8);
    buffer.putLong(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeFloat(float x) {
    ensureCapacity(4);
    buffer.putFloat(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeDouble(double x) {
    ensureCapacity(8);
    buffer.putDouble(x);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeString(@NotNull String str, @NotNull Charset charset) {
    byte[] bytes = str.getBytes(charset);
    int len = bytes.length;
    ensureCapacity(4 + len);
    writeVarInt(bytes.length);
    return write(bytes, 0, len);
  }

  @Override
  public @NotNull ByteBuf writeVarInt(int x) {
    ensureCapacity(4);
    while(true) {
      if((x & ~0x7F) == 0) {
        writeByte(x);
        return this;
      }
      writeByte((x & 0x7F) | 0x80);
      x >>>= 7;
    }
  }

  @Override
  public @NotNull ByteBuf writePosition(@NotNull TerminalPosition position) {
    writeUnsignedShort(position.x());
    writeUnsignedShort(position.y());
    return this;
  }

  @Override
  public @NotNull ByteBuf writeDimension(@NotNull TerminalDimension dimension) {
    writeUnsignedShort(dimension.width());
    writeUnsignedShort(dimension.height());
    return this;
  }

  @Override
  public @NotNull <T extends Enum<T>> ByteBuf writeEnum(T enumElement) {
    return writeVarInt(enumElement.ordinal());
  }

  @Override
  public @NotNull <T> ByteBuf writeOpt(@NotNull Optional<T> opt,
      BiConsumer<T, ByteBuf> byteBufBiConsumer) {
    if(opt.isPresent()) {
      writeBoolean(true);
      byteBufBiConsumer.accept(opt.get(), this);
      return this;
    }
    writeBoolean(false);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeColor(@NotNull TerminalColor terminalColor) {
    if(terminalColor.isDefault()) {
      writeByte(0);
      return this;
    }
    if(terminalColor instanceof XtermColor xtermColor) {
      writeByte(1);
      writeByte(xtermColor.ordinal());
      return this;
    }
    if(terminalColor instanceof ColorNamePalette colorNamePalette) {
      writeByte(1);
      writeByte(colorNamePalette.asXtermColor().ordinal());
      return this;
    }
    Color color = terminalColor.toColor();
    writeByte(2);
    writeInt(color.getRGB());
    return this;
  }

  @Override
  public @NotNull ByteBuf writeFontMap(@NotNull FontMap fontMap) {
    byte trueBits = 0;
    byte falseBits = 0;
    for (Entry<TextFont, Boolean> entry : fontMap.entrySet()) {
      byte bitFlag = (byte) (1 << entry.getKey().ordinal());
      if(entry.getValue()) {
        trueBits |= bitFlag;
      } else {
        falseBits |= bitFlag;
      }
    }
    writeByte(trueBits);
    writeByte(falseBits);
    return this;
  }

  @Override
  public @NotNull ByteBuf writeTextStyle(@NotNull TextStyle textStyle) {
    writeOpt(Optional.ofNullable(textStyle.foregroundColor()), (val, buf) -> {
      buf.writeColor((TerminalColor) val);
    });
    writeOpt(Optional.ofNullable(textStyle.backgroundColor()), (val, buf) -> {
      buf.writeColor((TerminalColor) val);
    });
    writeFontMap(textStyle.fontMap());
    return this;
  }

  @Override
  public @NotNull ByteBuf writeTextElement(@NotNull TextElement textElement) {
    return writeTextElement(textElement, Charset.defaultCharset());
  }

  @Override
  public @NotNull ByteBuf writeTextElement(@NotNull TextElement textElement,
      @NotNull Charset charset) {
    writeString(textElement.value(), charset);
    writeTextStyle(textElement.style());
    List<TextElement> elementList = textElement.child();
    writeVarInt(elementList.size());
    for (TextElement element : elementList) {
      writeTextElement(element, charset);
    }
    return this;
  }

  @Override
  public @NotNull ByteBuf writeIndexedStyleData(@NotNull IndexedStyleData data) {
    List<IndexEntry> indexes = data.indexes();
    writeVarInt(indexes.size());
    for (IndexEntry index : indexes) {
      writeInt(index.index());
      writeTextStyle(index.textStyle());
    }
    return this;
  }

  @Override
  public @NotNull ByteBuf writeTermString(@NotNull TermString termString) {
    return writeTermString(termString, Charset.defaultCharset());
  }

  @Override
  public @NotNull ByteBuf writeTermString(@NotNull TermString termString,
      @NotNull Charset charset) {
    writeString(termString.raw(), charset);
    writeIndexedStyleData(termString.data());
    return this;
  }

  @Override
  public int read(byte[] arr, int off, int len) {
    int remaining = remaining();
    len = Math.min(remaining, len);
    buffer.get(arr, off, len);
    return len;
  }

  @Override
  public int read(byte[] arr) {
    return read(arr, 0, arr.length);
  }

  @Override
  public byte peek() {
    return buffer.array()[buffer.position()];
  }

  @Override
  public byte readByte() {
    return buffer.get();
  }

  @Override
  public int readUnsignedByte() {
    return readByte() & 0xFF;
  }

  @Override
  public boolean readBoolean() {
    return buffer.get() == 1;
  }

  @Override
  public char readChar() {
    return buffer.getChar();
  }

  @Override
  public short readShort() {
    return buffer.getShort();
  }

  @Override
  public int readUnsignedShort() {
    return buffer.getShort();
  }

  @Override
  public int readInt() {
    return buffer.getInt();
  }

  @Override
  public long readUnsignedInt() {
    return buffer.getInt();
  }

  @Override
  public long readLong() {
    return buffer.getLong();
  }

  @Override
  public float readFloat() {
    return buffer.getFloat();
  }

  @Override
  public double readDouble() {
    return buffer.getDouble();
  }

  @Override
  public @NotNull String readString(@NotNull Charset charset) {
    int len = readVarInt();
    byte[] arr = new byte[len];
    len = read(arr);
    return new String(arr, 0, len, charset);
  }

  @Override
  public int readVarInt() {
    byte current;
    int pos = 0;
    int value = 0;
    while(true) {
      current = readByte();
      value |= (current & 0x7F) << pos;
      if((current & 0x80) == 0) {
        return value;
      }
      pos += 7;
      if(pos >= 32) {
        throw new IllegalStateException("Illegal VarInt. Size is too big: " + pos + " > 31");
      }
    }
  }

  @Override
  public @NotNull TerminalPosition readPosition() {
    int x = readShort();
    int y = readShort();
    return new TerminalPosition(x, y);
  }

  @Override
  public @NotNull TerminalDimension readDimension() {
    int width = readShort();
    int height = readShort();
    return new TerminalDimension(width, height);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends Enum<T>> @NotNull T readEnum(Class<? extends T> enumType) {
    int ordinal = readVarInt();
    try {
      Method arrayRetMethod = enumType.getMethod("values");
      Object obj = arrayRetMethod.invoke(null);
      T[] arr = (T[]) obj;
      return arr[ordinal];
    } catch (ClassCastException | NoSuchMethodException |
        InvocationTargetException | IllegalAccessException ex) {
      throw new IllegalStateException("Failed to read enum " + enumType.getSimpleName(), ex);
    }
  }

  @Override
  public @NotNull <T> Optional<T> readOpt(@NotNull Function<ByteBuf, T> func) {
    boolean state = readBoolean();
    return state ? Optional.of(func.apply(this)) : Optional.empty();
  }

  @Override
  public @NotNull TerminalColor readColor() {
    byte mode = readByte();
    return switch (mode) {
      case 0 -> TerminalColor.DEFAULT;
      case 1 -> {
        int ordinal = readUnsignedByte();
        yield XtermColor.getColor(ordinal);
      }
      case 2 -> {
        int rgb = readInt();
        yield TerminalColor.from(new Color(rgb));
      }
      default -> throw new IllegalStateException("Invalid color mode byte: " + mode);
    };
  }

  @Override
  public @NotNull FontMap readFontMap() {
    byte trueBits = readByte();
    byte falseBits = readByte();
    FontMap fontMap = new FontMap();
    for (TextFont value : TextFont.values()) {
      byte bitFlag = (byte) (1 << value.ordinal());
      if((trueBits & bitFlag) == bitFlag) {
        fontMap.set(value);
      }
      if((falseBits & bitFlag) == bitFlag) {
        fontMap.unset(value);
      }
    }
    return fontMap;
  }

  @Override
  public @NotNull TextStyle readTextStyle() {
    Optional<TerminalColor> opt1 = readOpt(ByteBuf::readColor);
    Optional<TerminalColor> opt2 = readOpt(ByteBuf::readColor);
    FontMap fontMap = readFontMap();
    return TextStyle.create(opt1.orElse(null), opt2.orElse(null), fontMap);
  }

  @Override
  public @NotNull TextElement readTextElement() {
    return readTextElement(Charset.defaultCharset());
  }

  @Override
  public @NotNull TextElement readTextElement(@NotNull Charset charset) {
    String value = readString(charset);
    TextStyle textStyle = readTextStyle();
    int childCount = readVarInt();
    List<TextElement> textElements = new ArrayList<>();
    for (int idx = 0; idx < childCount; idx++) {
      TextElement element = readTextElement(charset);
      textElements.add(element);
    }
    return TextElement.create(value)
        .style(textStyle)
        .child(textElements);
  }

  @Override
  public @NotNull IndexedStyleData readIndexedStyleData() {
    int count = readVarInt();
    IndexedStyleData data = IndexedStyleData.create();
    for (int idx = 0; idx < count; idx++) {
      int index = readInt();
      TextStyle textStyle = readTextStyle();
      data.set(index, textStyle);
    }
    return data;
  }

  @Override
  public @NotNull TermString readTermString() {
    return readTermString(Charset.defaultCharset());
  }

  @Override
  public @NotNull TermString readTermString(@NotNull Charset charset) {
    String value = readString(charset);
    IndexedStyleData data = readIndexedStyleData();
    return TermString.create(value, data);
  }

  @Override
  public int capacity() {
    return buffer.capacity();
  }

  @Override
  public int size() {
    return capacity();
  }

  @Override
  public int remaining() {
    return buffer.remaining();
  }

  @Override
  public int cursor() {
    return buffer.position();
  }

  @Override
  public @NotNull ByteBuf ensureRemaining(int remaining) {
    if(remaining() >= remaining) {
      return this;
    }
    int offset = remaining - remaining();
    if (offset < 0) {
      return this;
    }

    byte[] array = buffer.array();
    for (int idx = offset; idx < array.length; idx++) {
      array[idx - offset] = array[idx];
    }
    ByteBuffer byteBuffer = ByteBuffer.wrap(array);
    byteBuffer.position(Math.max(0, cursor() - offset));

    this.buffer = byteBuffer;
    return this;
  }

  @Override
  public @NotNull ByteBuf cursor(int cursor) {
    if(cursor < 0 || buffer.capacity() < cursor) {
      throw new IndexOutOfBoundsException(
          "Cursor out of buffer(" + buffer.capacity() + ": " + cursor);
    }
    buffer.position(cursor);
    return this;
  }

  @Override
  public @NotNull ByteBuf skip() {
    return skip(1);
  }

  @Override
  public @NotNull ByteBuf skip(int count) {
    int newCursor = cursor() + count;
    int size = size();
    buffer.position(Math.max(0, Math.min(newCursor, size)));
    return this;
  }

  @Override
  public @NotNull ByteBuf trim() {
    return resize(buffer.position());
  }

  @Override
  public @NotNull ByteBuf resize(int newLength) {
    byte[] array = buffer.array();
    int len = array.length;
    byte[] newArray = new byte[newLength];
    System.arraycopy(array, 0, newArray, 0,
        Math.min(len, newLength));
    buffer = ByteBuffer.wrap(newArray);
    return this;
  }

  @Override
  public byte[] byteArray() {
    return buffer.array();
  }
}
