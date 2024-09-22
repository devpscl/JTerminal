package net.jterminal.queue;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.util.StreamConfig;
import org.jetbrains.annotations.NotNull;

class QueuedByteBufImpl implements QueuedByteBuf {

  protected final byte[] array;
  protected int headIndex = 0;
  protected int tailIndex = 0;
  protected final Lock lock = new ReentrantLock();
  protected final Condition writeCondition = lock.newCondition();
  protected final Condition readCondition = lock.newCondition();

  public QueuedByteBufImpl(byte[] array) {
    this.array = array;
  }

  @Override
  public int capacity() {
    return array.length;
  }

  @Override
  public void clear() {
    headIndex = 0;
    tailIndex = 0;
  }

  @Override
  public boolean empty() {
    return headIndex == tailIndex;
  }

  @Override
  public boolean full() {
    return headIndex + 1 == tailIndex;
  }

  @Override
  public @NotNull QueueWriter writer() {
    return writer(new StreamConfig());
  }

  @Override
  public @NotNull QueueReader reader() {
    return reader(new StreamConfig());
  }

  @Override
  public @NotNull QueueWriter writer(@NotNull StreamConfig streamConfig) {
    return new WriterImpl(this, streamConfig);
  }

  @Override
  public @NotNull QueueReader reader(@NotNull StreamConfig streamConfig) {
    return new ReaderImpl(this, streamConfig);
  }

}
