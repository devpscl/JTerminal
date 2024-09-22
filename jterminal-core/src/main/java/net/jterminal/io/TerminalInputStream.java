package net.jterminal.io;

import java.io.InputStream;
import java.time.Duration;
import net.jterminal.input.InputEvent;
import net.jterminal.util.Ref;
import org.jetbrains.annotations.NotNull;

public abstract class TerminalInputStream extends InputStream {

  public abstract int available();

  public abstract int read(byte @NotNull [] bytes);

  public abstract int read(byte @NotNull [] bytes, int off, int len);

  public abstract int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration duration);

  public abstract int peek(byte @NotNull [] bytes);

  public abstract int peek(byte @NotNull [] bytes, int off, int len);

  public abstract int peekInputEvent(@NotNull Ref<InputEvent> ref);

  public abstract InputEvent readInputEvent();

  public abstract InputEvent readInputEvent(@NotNull Duration duration);

  @Override
  public abstract void reset();

  public abstract void close();

  public abstract boolean isClosed();
}
