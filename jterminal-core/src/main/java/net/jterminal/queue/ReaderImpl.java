package net.jterminal.queue;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import net.jterminal.util.StreamConfig;
import net.jterminal.queue.QueuedByteBuf.QueueReader;

class ReaderImpl implements QueueReader {

  private final QueuedByteBufImpl buf;
  private final StreamConfig streamConfig;

  ReaderImpl(QueuedByteBufImpl buf, StreamConfig streamConfig) {
    this.buf = buf;
    this.streamConfig = streamConfig;
  }

  @Override
  public int available() {
    final int head = buf.headIndex;
    final int tail = buf.tailIndex;
    final int len = buf.array.length;
    if(head >= tail) {
      return (head - tail);
    }
    return (head + len - tail);
  }

  @Override
  public int read(byte[] arr) throws InterruptedException {
    return read(arr, 0, arr.length);
  }

  @Override
  public int peek(byte[] arr) {
    return peek(arr, 0, arr.length);
  }

  @Override
  public int read(byte[] arr, int off, int len) throws InterruptedException {
    final int arrayLen = buf.array.length;
    final Condition writeCondition = buf.writeCondition;
    final Condition readCondition = buf.readCondition;
    final Lock lock = buf.lock;
    lock.lock();
    try {
      int avail = available();
      while(!streamConfig.nonBlocking() && avail == 0) {
        Duration timeout = streamConfig.timeout();
        if(timeout == null) {
          readCondition.await();
        } else {
          if(!readCondition.await(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
            return -1;
          }
        }
        avail = available();
      }

      len = Math.min(avail, len);
      int tail = buf.tailIndex;
      for (int idx = 0; idx < len; idx++) {
        arr[idx + off] = buf.array[tail % (arrayLen + 1)];
        tail++;
      }
      buf.tailIndex = tail;
      writeCondition.signal();
      return len;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int peek(byte[] arr, int off, int len) {
    final int arrayLen = buf.array.length;
    buf.lock.lock();
    try {
      final int avail = available();
      len = Math.min(avail, len);
      int tail = buf.tailIndex;
      for (int idx = 0; idx < len; idx++) {
        arr[idx + off] = buf.array[tail % (arrayLen + 1)];
        tail++;
      }
      return len;
    } finally {
      buf.lock.unlock();
    }
  }
}
