package net.jterminal.buffer;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
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

  @NotNull ByteBuf writePosition(@NotNull TerminalPosition position);

  @NotNull ByteBuf writeDimension(@NotNull TerminalDimension dimension);
  <T extends Enum<T>> @NotNull ByteBuf writeEnum(T enumElement);

  <T> @NotNull ByteBuf writeOpt(@NotNull Optional<T> opt,
      BiConsumer<T, ByteBuf> byteBufBiConsumer);

  int read(byte[] arr, int off, int len);

  int read(byte[] arr);

  byte peek();

  byte readByte();

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

  @NotNull TerminalPosition readPosition();

  @NotNull TerminalDimension readDimension();

  <T extends Enum<T>> @NotNull T readEnum(Class<? extends T> enumType);

  <T> @NotNull Optional<T> readOpt(@NotNull Function<ByteBuf, T> func);

  int capacity();

  int size();

  int remaining();

  int cursor();

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
