package net.jterminal.buffer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
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
  public @NotNull ByteBuf cursor(int cursor) {
    if(cursor < 0 || buffer.capacity() <= cursor) {
      throw new IndexOutOfBoundsException(
          "Cursor out of buffer(" + buffer.capacity() + ": " + cursor);
    }
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
