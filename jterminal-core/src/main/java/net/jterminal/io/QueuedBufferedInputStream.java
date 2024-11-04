package net.jterminal.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueuedBufferedInputStream implements Closeable {

  final byte[] array;
  int headIndex = 0;
  int tailIndex = 0;
  final Lock lock = new ReentrantLock();
  final Lock readLock = new ReentrantLock();
  final Condition readCondition = lock.newCondition();
  final InputStream inputStream;
  boolean closed = false;

  Thread thread;

  public QueuedBufferedInputStream(@NotNull InputStream inputStream,
      int bufferCapacity) {
    array = new byte[bufferCapacity];
    this.inputStream = inputStream;
  }

  public void startTransfer() {
    if(thread != null) {
      return;
    }
    thread = new Thread(this::run);
    thread.start();
  }

  private void run() {
    try {
      byte[] buf = new byte[256];
      int len;
      while (!closed) {
        len = inputStream.read(buf);
        write(buf, 0, len);
      }
    } catch (IOException e) {
      Terminal.LOGGER.catching(e);
    }
  }

  private int write(byte[] arr, int off, int len) {
    final int arrayLen = array.length;
    lock.lock();
    try {
      int free = free();
      len = Math.min(free, len);
      int head = headIndex;
      for (int idx = 0; idx < len; idx++) {
        array[head % (arrayLen + 1)] = arr[idx + off];
        head++;
      }
      headIndex = head;
      readCondition.signal();
      return len;
    } finally {
      lock.unlock();
    }
  }

  public int free() {
    final int head = headIndex;
    final int tail = tailIndex;
    final int len = array.length;
    if(head >= tail) {
      return len - (head - tail);
    }
    return len - (head + len - tail);
  }

  public int available() {
    final int head = headIndex;
    final int tail = tailIndex;
    final int len = array.length;
    if(head >= tail) {
      return (head - tail);
    }
    return (head + len - tail);
  }

  public int read() {
    byte[] arr = new byte[1];
    read(arr);
    return arr[0];
  }

  public int read(@Nullable Duration duration) throws InterruptedException {
    byte[] arr = new byte[1];
    if(read(arr, duration) == -1) {
      return -1;
    }
    return arr[0];
  }

  public int read(byte[] arr) {
    return read(arr, 0, arr.length);
  }

  public int read(byte[] arr, @Nullable Duration duration) throws InterruptedException {
    return read(arr, 0, arr.length, duration);
  }

  public int peek(byte[] arr) {
    return peek(arr, 0, arr.length);
  }

  public int read(byte[] arr, int off, int len) {
    try {
      return read(arr, off, len, null);
    } catch (InterruptedException ex) {
      Terminal.LOGGER.catching(ex);
    }
    return 0;
  }

  public int read(byte[] arr, int off, int len, @Nullable Duration timeout)
      throws InterruptedException {
    readLock.lock();
    try {
      lock.lock();
      try {
        final int arrayLen = array.length;
        int avail = available();
        while(avail == 0) {
          if(closed) {
            return 0;
          }
          if(timeout == null) {
            readCondition.await();
          } else {
            if(!readCondition.await(timeout.toMillis(), TimeUnit.MILLISECONDS)) {
              return 0;
            }
          }
          avail = available();
        }
        len = Math.min(avail, len);
        int tail = tailIndex;
        for (int idx = 0; idx < len; idx++) {
          arr[idx + off] = array[tail % (arrayLen + 1)];
          tail++;
        }
        tailIndex = tail;
        return len;
      } finally {
        lock.unlock();
      }
    } finally {
      readLock.unlock();
    }
  }

  public int peek(byte[] arr, int off, int len) {
    final int arrayLen = array.length;
    lock.lock();
    try {
      final int avail = available();
      len = Math.min(avail, len);
      int tail = tailIndex;
      for (int idx = 0; idx < len; idx++) {
        arr[idx + off] = array[tail % (arrayLen + 1)];
        tail++;
      }
      return len;
    } finally {
      lock.unlock();
    }
  }

  public void clear() {
    lock.lock();
    try {
      headIndex = 0;
      tailIndex = 0;
    } finally {
      lock.unlock();
    }
  }

  public boolean closed() {
    return closed;
  }

  @Override
  public void close() throws IOException {
    if(!closed) {
      closed = true;
      readCondition.signal();
    }
  }
}
