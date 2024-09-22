package net.jterminal.queue;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import net.jterminal.util.StreamConfig;
import net.jterminal.queue.QueuedByteBuf.QueueWriter;

class WriterImpl implements QueueWriter {

  private final QueuedByteBufImpl buf;
  private final StreamConfig streamConfig;

  WriterImpl(QueuedByteBufImpl buf, StreamConfig streamConfig) {
    this.buf = buf;
    this.streamConfig = streamConfig;
  }

  @Override
  public int free() {
    final int head = buf.headIndex;
    final int tail = buf.tailIndex;
    final int len = buf.array.length;
    if(head >= tail) {
      return len - (head - tail);
    }
    return len - (head + len - tail);
  }

  @Override
  public int write(byte[] arr) throws InterruptedException {
    return write(arr, 0, arr.length);
  }

  @Override
  public int write(byte[] arr, int off, int len) throws InterruptedException {
    final int arrayLen = buf.array.length;
    final Condition writeCondition = buf.writeCondition;
    final Condition readCondition = buf.readCondition;
    final Lock lock = buf.lock;
    lock.lock();
    try {
      int free = free();
      while(!streamConfig.nonBlocking() && free == 0) {
        Duration timeout = streamConfig.timeout();
        if(timeout == null) {
          writeCondition.await();
        } else {
          if(!writeCondition.await(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
            return -1;
          }
        }
        free = free();
      }
      len = Math.min(free, len);
      int head = buf.headIndex;
      for (int idx = 0; idx < len; idx++) {
        buf.array[head % (arrayLen + 1)] = arr[idx + off];
        head++;
      }
      buf.headIndex = head;
      readCondition.signal();
      return len;
    } finally {
      lock.unlock();
    }
  }

}
