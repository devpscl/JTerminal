package net.jterminal.input;

import java.io.IOException;
import java.time.Duration;
import net.jterminal.Terminal;
import net.jterminal.exception.TerminalRuntimeException;
import net.jterminal.io.QueuedBufferedInputStream;
import net.jterminal.util.Ref;
import org.jetbrains.annotations.NotNull;

class NonNativeTerminalInput implements TerminalInput {

  private final QueuedBufferedInputStream inputStream;
  private final int capacity;

  public NonNativeTerminalInput(int capacity) {
    this.capacity = capacity;
    this.inputStream = new QueuedBufferedInputStream(Terminal.FD_IN, capacity);
  }

  public void init() {
    inputStream.startTransfer();
  }

  @Override
  public int capacity() {
    return capacity;
  }

  @Override
  public int available() {
    return inputStream.available();
  }

  @Override
  public int read() {
    return inputStream.read();
  }

  @Override
  public int read(byte @NotNull [] bytes) {
    return inputStream.read(bytes);
  }

  @Override
  public int read(byte @NotNull [] bytes, @NotNull Duration timeout) {
    try {
      return inputStream.read(bytes, timeout);
    } catch (InterruptedException e) {
      Terminal.LOGGER.error("Illegal interrupt");
      return -1;
    }
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len) {
    return inputStream.read(bytes, off, len);
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration timeout) {
    try {
      return inputStream.read(bytes, off, len, timeout);
    } catch (InterruptedException e) {
      Terminal.LOGGER.error("Illegal interrupt");
      return -1;
    }
  }

  @Override
  public int peek(byte @NotNull [] bytes) {
    return inputStream.peek(bytes);
  }

  @Override
  public int peek(byte @NotNull [] bytes, int off, int len) {
    return inputStream.peek(bytes, off, len);
  }

  @Override
  public int peekEvent(@NotNull Ref<InputEvent> ref) {
    return 0;
  }

  @Override
  public InputEvent readEvent() {
    throw new UnsupportedOperationException();
  }

  @Override
  public InputEvent readEvent(@NotNull Duration timeout) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void reset() {
    inputStream.clear();
  }

  @Override
  public void close() {
    try {
      inputStream.close();
    } catch (IOException e) {
      throw new TerminalRuntimeException(e);
    }
  }

  @Override
  public boolean isClosed() {
    return inputStream.closed();
  }

  @Override
  public boolean isClosable() {
    return true;
  }

  @Override
  public boolean isNative() {
    return false;
  }

}
