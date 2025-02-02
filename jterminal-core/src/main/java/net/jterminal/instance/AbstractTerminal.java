package net.jterminal.instance;

import java.io.IOException;
import java.io.PrintStream;
import net.devpscl.eventbus.EventBus;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.ansi.AnsiCodeSerializer;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.Log4JErrorHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractTerminal<T extends Terminal>
    implements Terminal {

  protected static final int defaultFlags = Terminal.FLAG_LINE_INPUT | Terminal.FLAG_ECHO |
      Terminal.FLAG_SIGNAL_INPUT;

  protected final Class<?> interfaceType;

  protected final PrintStream out;
  protected final PrintStream err;

  protected final EventBus eventBus = EventBus.builder()
      .enableListenerPriorities()
      .errorHandler(new Log4JErrorHandler(Terminal.LOGGER))
      .build();
  protected boolean inputEventListening = false;

  private boolean created = false;

  public AbstractTerminal(@NotNull Class<T> interfaceType) {
    this(interfaceType, null, null);
  }

  public AbstractTerminal(@NotNull Class<T> interfaceType,
      @Nullable PrintStream out, @Nullable PrintStream err) {
    this.interfaceType = interfaceType;
    this.out = out == null ? new PrintStream(FD_OUT) : out;
    this.err = err == null ? new PrintStream(FD_ERR) : err;
  }

  public void enable() throws TerminalProviderException {
    System.setOut(out);
    System.setErr(err);
    System.setIn(FD_IN);
    if(!created) {
      created = true;
      initialize();
    }
  }

  public abstract void initialize() throws TerminalProviderException;

  public abstract void disable() throws TerminalProviderException;

  @Override
  public void writeLine(@NotNull String x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(double x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(float x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(boolean x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(char x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(long x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(int x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(short x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(byte x) {
    if(!isEnabled()) {
      return;
    }
    out.println(x);
  }

  @Override
  public void writeLine(byte[] bytes, int off, int len) {
    if(!isEnabled()) {
      return;
    }
    out.write(bytes, off, len);
    out.println();
  }

  @Override
  public void writeLine(@NotNull Object obj) {
    if(!isEnabled()) {
      return;
    }
    out.println(obj);
  }

  @Override
  public void writeLine(@NotNull TerminalBuffer terminalBuffer) {
    if(!isEnabled()) {
      return;
    }
    out.println(terminalBuffer);
  }

  @Override
  public void writeLine(@NotNull TermString termString) {
    if(!isEnabled()) {
      return;
    }
    out.println(AnsiCodeSerializer.DEFAULT.serialize(termString));
  }

  @Override
  public void writeLine(@NotNull TextElement textElement) {
    if(!isEnabled()) {
      return;
    }
    out.println(AnsiCodeSerializer.DEFAULT.serialize(textElement));
  }

  @Override
  public void newLine() {
    if(!isEnabled()) {
      return;
    }
    out.print(System.lineSeparator());
  }

  @Override
  public void write(@NotNull String x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(@NotNull TerminalBuffer terminalBuffer) {
    if(!isEnabled()) {
      return;
    }
    out.print(terminalBuffer);
  }

  @Override
  public void write(@NotNull TermString termString) {
    if(!isEnabled()) {
      return;
    }
    out.print(AnsiCodeSerializer.DEFAULT.serialize(termString));
  }

  @Override
  public void write(@NotNull TextElement textElement) {
    if(!isEnabled()) {
      return;
    }
    out.print(AnsiCodeSerializer.DEFAULT.serialize(textElement));
  }

  @Override
  public void write(double x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(float x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(boolean x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(char x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(long x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(int x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(short x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(byte x) {
    if(!isEnabled()) {
      return;
    }
    out.print(x);
  }

  @Override
  public void write(byte[] bytes, int off, int len) {
    if(!isEnabled()) {
      return;
    }
    out.write(bytes, off, len);
  }

  @Override
  public void write(@NotNull Object obj) {
    if(!isEnabled()) {
      return;
    }
    out.print(obj);
  }

  @Override
  public void foregroundColor(@Nullable ForegroundColor foregroundColor) {
    if(!isEnabled()) {
      return;
    }
    if(foregroundColor == null) {
      foregroundColor = TerminalColor.DEFAULT;
    }
    try {
      FD_OUT.write(foregroundColor.getForegroundAnsiCode().getBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to write ansi color", e);
    }
  }

  @Override
  public void backgroundColor(@Nullable BackgroundColor backgroundColor) {
    if(!isEnabled()) {
      return;
    }
    if(backgroundColor == null) {
      backgroundColor = TerminalColor.DEFAULT;
    }
    try {
      FD_OUT.write(backgroundColor.getBackgroundAnsiCode().getBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to write ansi color", e);
    }
  }

  @Override
  public void style(@Nullable TextStyle textStyle) {
    if(!isEnabled()) {
      return;
    }
    if(textStyle == null) {
      textStyle = TextStyle.create();
    }
    try {
      FD_OUT.write(AnsiCodeSerializer.DEFAULT.serialize(textStyle).getBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to write ansi color", e);
    }
  }

  @Override
  public void resetStyle() {
    style(TextStyle.getDefault());
  }

  @Override
  @NotNull
  public EventBus eventBus() {
    return eventBus;
  }

  @Override
  public void waitFutureShutdown() {
    //do nothing
  }

  @Override
  public void inputEventListenerEnabled(boolean state) {
    this.inputEventListening = state;
  }

  @Override
  public boolean inputEventListenerEnabled() {
    return inputEventListening;
  }

}
