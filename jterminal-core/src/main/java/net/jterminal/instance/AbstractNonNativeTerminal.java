package net.jterminal.instance;

import java.io.IOException;
import java.io.PrintStream;
import net.jterminal.Terminal;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.exception.TerminalRuntimeException;
import net.jterminal.io.DefaultTerminalInputStream;
import net.jterminal.io.TerminalInputStream;
import net.jterminal.queue.AsyncQueueIOProcessor;
import net.jterminal.queue.QueuedByteBuf;
import net.jterminal.system.SystemInfo;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractNonNativeTerminal<T extends Terminal> extends AbstractTerminal<T> {

  private String title = "Terminal";
  protected static final AsyncQueueIOProcessor queueIoProcessor
      = new AsyncQueueIOProcessor(FD_IN);

  public AbstractNonNativeTerminal(@NotNull Class<T> interfaceType) {
    super(interfaceType);
  }

  public AbstractNonNativeTerminal(@NotNull Class<T> interfaceType, @Nullable PrintStream out,
      @Nullable PrintStream err) {
    super(interfaceType, out, err);
  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();
    queueIoProcessor.enabled(true);
  }

  @Override
  public void disable() {
    queueIoProcessor.enabled(false);
  }

  @Override
  public void cursorPosition(@NotNull TerminalPosition pos) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TerminalInputStream newInputStream(int capacity) {
    QueuedByteBuf buf = QueuedByteBuf.create(capacity);
    DefaultTerminalInputStream inputStream
        = new DefaultTerminalInputStream(buf, queueIoProcessor);
    queueIoProcessor.add(buf);
    return inputStream;
  }

  @Override
  public @NotNull TerminalPosition cursorPosition() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    SystemInfo current = SystemInfo.current();
    try {
      switch (current.os()) {
        case WINDOWS -> {
          new ProcessBuilder("cmd.exe", "/c", "cls").inheritIO().start().waitFor();
        }
        case LINUX, MACOS -> {
          new ProcessBuilder("/bin/bash", "-c", "clear").inheritIO().start().waitFor();
        }
        default -> throw new UnsupportedOperationException("Terminal cannot be cleared");
      }
    } catch (IOException | InterruptedException e) {
      throw new TerminalRuntimeException("Terminal cannot be cleared", e);
    }
  }

  @Override
  public void flags(int flags) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int flags() {
    return 0;
  }

  @Override
  public void cursorFlags(int flags) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int cursorFlags() {
    return 0;
  }

  @Override
  public void reset(boolean clearScreen) {
    write("\u001B[0m");
    if(clearScreen) {
      clear();
    }
  }

  @Override
  public void title(@NotNull String title) {
    this.title = title;
    update();
  }

  @Override
  public @NotNull String title() {
    return title;
  }

  @Override
  public void windowSize(@NotNull TerminalDimension size) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull TerminalDimension windowSize() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void update() {
    write("\u001B]0;" + title + "\007");
  }

  @Override
  public void beep() {
    write('\007');
  }

  @Override
  public boolean isNative() {
    return false;
  }

  @Override
  public void shutdown(int status) {
    System.exit(status);
  }

}
