package net.jterminal.buffer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

class SyncByteBufImpl extends ByteBufImpl {

  private final Object mutex = new Object();

  public SyncByteBufImpl(@NotNull ByteBuffer byteBuffer, int additionCapacity) {
    super(byteBuffer, additionCapacity);
  }

  @Override
  public @NotNull ByteBuf write(byte[] bytes, int off, int len) {
    synchronized (mutex) {
      return super.write(bytes, off, len);
    }
  }

  @Override
  public @NotNull ByteBuf write(byte[] bytes) {
    synchronized (mutex) {
      return super.write(bytes);
    }
  }

  @Override
  public @NotNull ByteBuf writeByte(byte x) {
    synchronized (mutex) {
      return super.writeByte(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeByte(int x) {
    synchronized (mutex) {
      return super.writeByte(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeBoolean(boolean x) {
    synchronized (mutex) {
      return super.writeBoolean(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeChar(char x) {
    synchronized (mutex) {
      return super.writeChar(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeShort(short x) {
    synchronized (mutex) {
      return super.writeShort(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeUnsignedShort(int x) {
    synchronized (mutex) {
      return super.writeUnsignedShort(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeInt(int x) {
    synchronized (mutex) {
      return super.writeInt(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeUnsignedInt(long x) {
    synchronized (mutex) {
      return super.writeUnsignedInt(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeLong(long x) {
    synchronized (mutex) {
      return super.writeLong(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeFloat(float x) {
    synchronized (mutex) {
      return super.writeFloat(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeDouble(double x) {
    synchronized (mutex) {
      return super.writeDouble(x);
    }
  }

  @Override
  public @NotNull ByteBuf writeString(@NotNull String str, @NotNull Charset charset) {
    synchronized (mutex) {
      return super.writeString(str, charset);
    }
  }

  @Override
  public @NotNull ByteBuf writeVarInt(int x) {
    synchronized (mutex) {
      return super.writeVarInt(x);
    }
  }

  @Override
  public @NotNull ByteBuf writePosition(@NotNull TerminalPosition position) {
    synchronized (mutex) {
      return super.writePosition(position);
    }

  }

  @Override
  public @NotNull ByteBuf writeDimension(@NotNull TerminalDimension dimension) {
    synchronized (mutex) {
      return super.writeDimension(dimension);
    }
  }

  @Override
  public @NotNull <T extends Enum<T>> ByteBuf writeEnum(T enumElement) {
    synchronized (mutex) {
      return super.writeEnum(enumElement);
    }
  }

  @Override
  public int read(byte[] arr, int off, int len) {
    synchronized (mutex) {
      return super.read(arr, off, len);
    }
  }

  @Override
  public int read(byte[] arr) {
    synchronized (mutex) {
      return super.read(arr);
    }
  }

  @Override
  public byte readByte() {
    synchronized (mutex) {
      return super.readByte();
    }
  }

  @Override
  public boolean readBoolean() {
    synchronized (mutex) {
      return super.readBoolean();
    }
  }

  @Override
  public char readChar() {
    synchronized (mutex) {
      return super.readChar();
    }
  }

  @Override
  public short readShort() {
    synchronized (mutex) {
      return super.readShort();
    }
  }

  @Override
  public int readUnsignedShort() {
    synchronized (mutex) {
      return super.readUnsignedShort();
    }
  }

  @Override
  public int readInt() {
    synchronized (mutex) {
      return super.readInt();
    }
  }

  @Override
  public long readUnsignedInt() {
    synchronized (mutex) {
      return super.readUnsignedInt();
    }
  }

  @Override
  public long readLong() {
    synchronized (mutex) {
      return super.readLong();
    }
  }

  @Override
  public float readFloat() {
    synchronized (mutex) {
      return super.readFloat();
    }
  }

  @Override
  public double readDouble() {
    synchronized (mutex) {
      return super.readDouble();
    }
  }

  @Override
  public @NotNull String readString(@NotNull Charset charset) {
    synchronized (mutex) {
      return super.readString(charset);
    }
  }

  @Override
  public int readVarInt() {
    synchronized (mutex) {
      return super.readVarInt();
    }
  }

  @Override
  public @NotNull TerminalPosition readPosition() {
    synchronized (mutex) {
      return super.readPosition();
    }
  }

  @Override
  public @NotNull TerminalDimension readDimension() {
    synchronized (mutex) {
      return super.readDimension();
    }
  }

  @Override
  public <T extends Enum<T>> @NotNull T readEnum(Class<? extends T> enumType) {
    synchronized (mutex) {
      return super.readEnum(enumType);
    }
  }

  @Override
  public @NotNull ByteBuf cursor(int cursor) {
    synchronized (mutex) {
      return super.cursor(cursor);
    }
  }

  @Override
  public @NotNull ByteBuf skip() {
    synchronized (mutex) {
      return super.skip();
    }
  }

  @Override
  public @NotNull ByteBuf skip(int count) {
    synchronized (mutex) {
      return super.skip(count);
    }
  }

  @Override
  public @NotNull ByteBuf trim() {
    synchronized (mutex) {
      return super.trim();
    }
  }

  @Override
  public @NotNull ByteBuf resize(int newLength) {
    synchronized (mutex) {
      return super.resize(newLength);
    }
  }

  @Override
  public byte[] byteArray() {
    return super.byteArray();
  }
}
