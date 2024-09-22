package net.jterminal.queue;

import net.jterminal.util.StreamConfig;
import org.jetbrains.annotations.NotNull;

public interface QueuedByteBuf {

  int capacity();

  void clear();

  boolean empty();

  boolean full();

  @NotNull QueueWriter writer();

  @NotNull QueueReader reader();

  @NotNull QueueWriter writer(@NotNull StreamConfig streamConfig);

  @NotNull QueueReader reader(@NotNull StreamConfig streamConfig);

  static @NotNull QueuedByteBuf create(int capacity) {
    return new QueuedByteBufImpl(new byte[capacity]);
  }

  interface QueueWriter {

    int free();

    int write(byte[] arr) throws InterruptedException;

    int write(byte[] arr, int off, int len) throws InterruptedException;

  }

  interface QueueReader {

    int available();

    int read(byte[] arr) throws InterruptedException;

    int peek(byte[] arr);

    int read(byte[] arr, int off, int len) throws InterruptedException;

    int peek(byte[] arr, int off, int len);

  }

}
