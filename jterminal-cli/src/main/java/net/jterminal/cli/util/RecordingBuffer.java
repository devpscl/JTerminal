package net.jterminal.cli.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class RecordingBuffer {

  private final int baseCapacity;
  private final ByteBuf buf;
  private final ReentrantLock lock = new ReentrantLock();

  public RecordingBuffer(int baseCapacity, int limitCapacity) {
    this.baseCapacity = baseCapacity;
    buf = ByteBuf.builder()
        .capacity(limitCapacity)
        .fixedCapacity()
        .build();
  }

  public void write(@NotNull String str) {
    write(str.getBytes());
  }

  public void write(byte[] array) {
    lock.lock();
    try {
      if(buf.remaining() < array.length) {
        int overflow = array.length - buf.remaining();
        int free = Math.max(overflow, buf.capacity() - baseCapacity);
        buf.ensureRemaining(free);
      }
      buf.write(array, 0, Math.min(array.length, buf.capacity()));
    } finally {
      lock.unlock();
    }
  }

  public void clear() {
    buf.cursor(0);
  }

  public @NotNull ByteBuf buf() {
    return buf;
  }

  public void writeTo(@NotNull OutputStream outputStream) throws IOException {
    byte[] bytes = buf.byteArray();
    outputStream.write(bytes, 0, buf.cursor());
  }

}
