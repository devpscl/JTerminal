package net.jterminal.buffer;

import java.nio.ByteBuffer;
import net.jterminal.buffer.ByteBuf.Builder;
import org.jetbrains.annotations.NotNull;

class ByteBufBuilder implements Builder {

  private int capacity = 32;
  private int additionCapacity = 32;
  private boolean sync = false;
  private byte[] src = null;
  private int srcOff = 0;
  private int srcLen = 0;

  @Override
  public @NotNull Builder capacity(int capacity) {
    this.capacity = capacity;
    return this;
  }

  @Override
  public @NotNull Builder sync() {
    sync = true;
    return this;
  }

  @Override
  public @NotNull Builder wrap(byte[] bytes, int off, int len) {
    src = bytes;
    srcOff = off;
    srcLen = len;
    return this;
  }

  @Override
  public @NotNull Builder wrap(byte[] bytes) {
    return wrap(bytes, 0, bytes.length);
  }

  @Override
  public @NotNull Builder dynamicCapacityAddition(int additionCapacity) {
    this.additionCapacity = additionCapacity;
    return this;
  }

  @Override
  public @NotNull Builder fixedCapacity() {
    return dynamicCapacityAddition(0);
  }

  @Override
  public @NotNull ByteBuf build() {
    ByteBuffer buffer = src == null ? ByteBuffer.allocate(capacity)
        : ByteBuffer.wrap(src, srcOff, srcLen);
    return sync ? new SyncByteBufImpl(buffer, additionCapacity)
        : new ByteBufImpl(buffer, additionCapacity);
  }
}
