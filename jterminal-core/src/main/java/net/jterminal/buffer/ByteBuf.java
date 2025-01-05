package net.jterminal.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.FontMap;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.IndexedStyleData;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface ByteBuf {

  @NotNull ByteBuf write(byte[] bytes, int off, int len);

  @NotNull ByteBuf write(byte[] bytes);

  @NotNull ByteBuf writeByte(byte x);

  @NotNull ByteBuf writeByte(int x);

  @NotNull ByteBuf writeBoolean(boolean x);

  @NotNull ByteBuf writeChar(char x);

  @NotNull ByteBuf writeShort(short x);

  @NotNull ByteBuf writeUnsignedShort(int x);

  @NotNull ByteBuf writeInt(int x);

  @NotNull ByteBuf writeUnsignedInt(long x);

  @NotNull ByteBuf writeLong(long x);

  @NotNull ByteBuf writeFloat(float x);

  @NotNull ByteBuf writeDouble(double x);

  @NotNull ByteBuf writeString(@NotNull String str, @NotNull Charset charset);

  @NotNull ByteBuf writeVarInt(int x);

  @NotNull ByteBuf writePosition(@NotNull TermPos position);

  @NotNull ByteBuf writeDimension(@NotNull TermDim dimension);

  <T extends Enum<T>> @NotNull ByteBuf writeEnum(T enumElement);

  <T> @NotNull ByteBuf writeOpt(@NotNull Optional<T> opt,
      BiConsumer<T, ByteBuf> byteBufBiConsumer);

  @NotNull ByteBuf writeColor(@NotNull TerminalColor terminalColor);

  @NotNull ByteBuf writeFontMap(@NotNull FontMap fontMap);

  @NotNull ByteBuf writeTextStyle(@NotNull TextStyle textStyle);

  @NotNull ByteBuf writeTextElement(@NotNull TextElement textElement);

  @NotNull ByteBuf writeTextElement(@NotNull TextElement textElement, @NotNull Charset charset);

  @NotNull ByteBuf writeIndexedStyleData(@NotNull IndexedStyleData data);

  @NotNull ByteBuf writeTermString(@NotNull TermString termString);

  @NotNull ByteBuf writeTermString(@NotNull TermString termString, @NotNull Charset charset);

  int read(byte[] arr, int off, int len);

  int read(byte[] arr);

  byte peek();

  byte readByte();

  int readUnsignedByte();

  boolean readBoolean();

  char readChar();

  short readShort();

  int readUnsignedShort();

  int readInt();

  long readUnsignedInt();

  long readLong();

  float readFloat();

  double readDouble();

  @NotNull String readString(@NotNull Charset charset);

  int readVarInt();

  @NotNull TermPos readPosition();

  @NotNull TermDim readDimension();

  <T extends Enum<T>> @NotNull T readEnum(Class<? extends T> enumType);

  <T> @NotNull Optional<T> readOpt(@NotNull Function<ByteBuf, T> func);

  @NotNull TerminalColor readColor();

  @NotNull FontMap readFontMap();

  @NotNull TextStyle readTextStyle();

  @NotNull TextElement readTextElement();

  @NotNull TextElement readTextElement(@NotNull Charset charset);

  @NotNull IndexedStyleData readIndexedStyleData();

  @NotNull TermString readTermString();

  @NotNull TermString readTermString(@NotNull Charset charset);

  int capacity();

  int size();

  int remaining();

  int cursor();

  @NotNull ByteBuf ensureRemaining(int remaining);

  @NotNull ByteBuf cursor(int cursor);

  @NotNull ByteBuf skip();

  @NotNull ByteBuf skip(int count);

  @NotNull ByteBuf trim();

  @NotNull ByteBuf resize(int newLength);

  byte[] byteArray();

  static void writeTo(@NotNull OutputStream outputStream, @NotNull ByteBuf buf)
      throws IOException {
    outputStream.write(buf.byteArray(), 0, buf.cursor());
  }

  static @NotNull Builder builder() {
    return new ByteBufBuilder();
  }

  static @NotNull ByteBuf from(byte[] array, int off, int len) {
    return builder()
        .wrap(array, off, len)
        .build();
  }

  static @NotNull ByteBuf from(byte[] array) {
    return builder()
        .wrap(array)
        .build();
  }

  static @NotNull ByteBuf alloc(int size) {
    return builder()
        .capacity(size)
        .build();
  }

  interface Builder {

    @NotNull Builder capacity(int capacity);

    @NotNull Builder sync();

    @NotNull Builder wrap(byte[] bytes, int off, int len);

    @NotNull Builder wrap(byte[] bytes);

    @NotNull Builder dynamicCapacityAddition(int additionCapacity);

    @NotNull Builder fixedCapacity();

    @NotNull ByteBuf build();

  }

}
