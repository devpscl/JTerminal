package net.jterminal.io;

import java.time.Duration;
import net.jterminal.Terminal;
import net.jterminal.input.InputEvent;
import net.jterminal.queue.AsyncQueueIOProcessor;
import net.jterminal.queue.QueuedByteBuf;
import net.jterminal.util.Ref;
import net.jterminal.util.StreamConfig;
import org.jetbrains.annotations.NotNull;

public class DefaultTerminalInputStream extends TerminalInputStream {

  private final QueuedByteBuf queuedByteBuf;
  private final AsyncQueueIOProcessor asyncQueueIOProcessor;
  private boolean closed = false;

  public DefaultTerminalInputStream(@NotNull QueuedByteBuf queuedByteBuf,
      AsyncQueueIOProcessor asyncQueueIOProcessor) {
    this.queuedByteBuf = queuedByteBuf;
    this.asyncQueueIOProcessor = asyncQueueIOProcessor;
  }

  @Override
  public int available() {
    return queuedByteBuf.reader().available();
  }

  @Override
  public int read() {
    byte[] b = new byte[1];
    try {
      int len = queuedByteBuf.reader().read(b);
      if(len > 0) {
        return b[0] & 0xFF;
      }
    } catch (InterruptedException e) {
      Terminal.LOGGER.catching(e);
    }
    return -1;
  }

  @Override
  public int read(byte @NotNull [] bytes) {
    try {
      return queuedByteBuf.reader().read(bytes);
    } catch (InterruptedException e) {
      Terminal.LOGGER.catching(e);
    }
    return -1;
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len) {
    try {
      return queuedByteBuf.reader().read(bytes, off, len);
    } catch (InterruptedException e) {
      Terminal.LOGGER.catching(e);
    }
    return -1;
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration duration) {
    try {
      return queuedByteBuf.reader(new StreamConfig().timeout(duration))
          .read(bytes);
    } catch (InterruptedException e) {
      Terminal.LOGGER.catching(e);
    }
    return -1;
  }

  @Override
  public int peek(byte @NotNull [] bytes) {
    return queuedByteBuf.reader().peek(bytes);
  }

  @Override
  public int peek(byte @NotNull [] bytes, int off, int len) {
    return queuedByteBuf.reader().peek(bytes, off, len);
  }

  @Override
  public int peekInputEvent(@NotNull Ref<InputEvent> ref) {
    return -1;
  }

  @Override
  public InputEvent readInputEvent() {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputEvent readInputEvent(@NotNull Duration duration) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reset() {
    queuedByteBuf.clear();
  }

  @Override
  public void close() {
    closed = true;
    asyncQueueIOProcessor.remove(queuedByteBuf);
  }

  @Override
  public boolean isClosed() {
    return closed;
  }
}
