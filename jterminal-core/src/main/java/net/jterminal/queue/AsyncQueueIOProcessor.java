package net.jterminal.queue;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import net.jterminal.util.StreamConfig;
import org.jetbrains.annotations.NotNull;

public class AsyncQueueIOProcessor {

  private final InputStream inputStream;
  private final ExecutorService executor = Executors.newSingleThreadExecutor();
  private final Lock lock = new ReentrantLock();
  private final Condition condition = lock.newCondition();
  private final List<QueuedByteBuf> queueList = new ArrayList<>();

  private boolean asyncCreated = false;
  private boolean enabled = false;
  private boolean closed = false;

  public AsyncQueueIOProcessor(@NotNull InputStream inputStream) {
    this.inputStream = inputStream;
  }

  private void asyncRun() {
    byte[] buf = new byte[1024];
    int len;
    try {
      while (!closed) {
        while(!enabled) {
          condition.await();
          if(closed) {
            break;
          }
        }
        len = inputStream.read(buf, 0, 1024);
        synchronized (queueList) {
          for (QueuedByteBuf queuedByteBuf : queueList) {
            queuedByteBuf.writer(new StreamConfig().nonBlocking(true))
                .write(buf, 0, len);
          }
        }
      }
    } catch (InterruptedException | IOException ex) {
      Terminal.LOGGER.error("Failed to read input stream", ex);
    } finally {
      lock.unlock();
    }
  }

  public @NotNull AsyncQueueIOProcessor enabled(boolean state) {
    lock.lock();
    if(state && !asyncCreated) {
      asyncCreated = true;
      executor.submit(this::asyncRun);
    }
    enabled = state;
    condition.signal();
    return this;
  }

  public @NotNull AsyncQueueIOProcessor add(@NotNull QueuedByteBuf byteBuf) {
    synchronized (queueList) {
      queueList.add(byteBuf);
    }
    return this;
  }

  public @NotNull AsyncQueueIOProcessor remove(@NotNull QueuedByteBuf byteBuf) {
    synchronized (queueList) {
      queueList.remove(byteBuf);
    }
    return this;
  }

  public boolean enabled() {
    return enabled;
  }

  public boolean isClosed() {
    return !closed;
  }

  public void close() {
    closed = true;
    condition.signal();
  }
}
