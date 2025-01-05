package net.jterminal.instance;

import java.io.IOException;
import java.io.PrintStream;
import net.jterminal.Terminal;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.exception.TerminalRuntimeException;
import net.jterminal.system.SystemInfo;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractNonNativeTerminal<T extends Terminal> extends AbstractTerminal<T> {

  private String title = "Terminal";

  public AbstractNonNativeTerminal(@NotNull Class<T> interfaceType) {
    super(interfaceType);
  }

  public AbstractNonNativeTerminal(@NotNull Class<T> interfaceType, @Nullable PrintStream out,
      @Nullable PrintStream err) {
    super(interfaceType, out, err);
  }

  @Override
  public void initialize() {

  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();
  }

  @Override
  public void disable() {
  }

  @Override
  public void cursorPosition(@NotNull TermPos pos) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull TermPos cursorPosition() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void clear() {
    if(!isEnabled()) {
      return;
    }
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
    title = "Terminal";
    if(!isEnabled()) {
      return;
    }
    write("\u001B[0m");
    if(clearScreen) {
      clear();
    }
  }

  @Override
  public void title(@NotNull String title) {
    this.title = title;
    if(!isEnabled()) {
      return;
    }
    update();
  }

  @Override
  public @NotNull String title() {
    return title;
  }

  @Override
  public void windowSize(@NotNull TermDim size) {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull TermDim windowSize() {
    throw new UnsupportedOperationException();
  }

  @Override
  public @NotNull TermDim defaultWindowSize() {
    return new TermDim(80, 25);
  }

  @Override
  public void update() {
    if(!isEnabled()) {
      return;
    }
    write("\u001B]0;" + title + "\007");
  }

  @Override
  public void beep() {
    if(!isEnabled()) {
      return;
    }
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
