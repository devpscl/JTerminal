package net.jterminal.cli;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.devpscl.eventbus.annotation.SubscribeEvent;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.command.CommandHandler;
import net.jterminal.cli.exception.CommandParseException;
import net.jterminal.cli.event.LineReleaseEvent;
import net.jterminal.cli.line.DefaultLineReader;
import net.jterminal.cli.line.InternalLineReader;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.line.LineView;
import net.jterminal.cli.line.PrefixedLineRenderer;
import net.jterminal.cli.tab.TabCompletion;
import net.jterminal.cli.util.RecordingBuffer;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.input.WindowInputEvent;
import net.jterminal.instance.AbstractNativeTerminal;
import net.jterminal.io.ListenedPrintStream;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.SetLock;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractCLITerminal extends AbstractNativeTerminal<CLITerminal>
    implements CLITerminal, ListenedPrintStream.Listener {

  private LineReader lineReader;
  private volatile boolean lineReading = false;
  private final ReentrantLock lock = new ReentrantLock();
  private LineView lineView = null;
  private RecordingBuffer recordingBuffer = null;
  private CommandHandler<?> commandHandler = null;

  public AbstractCLITerminal(@NotNull Class<CLITerminal> interfaceType) {
    super(interfaceType, new ListenedPrintStream(Terminal.FD_OUT),
        new ListenedPrintStream(Terminal.FD_ERR));
    ListenedPrintStream outPrintStream = (ListenedPrintStream) out;
    ListenedPrintStream errPrintStream = (ListenedPrintStream) err;
    outPrintStream.setListener(this);
    errPrintStream.setListener(this);
    lineReader(new DefaultLineReader());
  }

  @Override
  public void initialize() throws TerminalProviderException {
    super.initialize();
    inputEventListenerEnabled(true);
    flags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT | FLAG_SIGNAL_INPUT);
    eventBus().register(this);
  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();
  }

  @Override
  public @Nullable CommandHandler<?> commandHandler() {
    return commandHandler;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends CommandArgument> CommandHandler<T> commandHandler(Class<T> type) {
    return commandHandler == null ? null : (CommandHandler<T>)commandHandler;
  }

  @Override
  public void commandHandler(@Nullable CommandHandler<?> commandHandler) {
    this.commandHandler = commandHandler;
  }

  @Override
  public void unsetCommandHandler() {
    this.commandHandler = null;
  }

  @Override
  public @NotNull CommandArgument[] parseArguments(@NotNull String input)
      throws CommandParseException {
    if(commandHandler == null) {
      return new CommandArgument[0];
    }
    return commandHandler.parser().parse(input);
  }

  @SuppressWarnings("unchecked")
  @Override
  public @NotNull TermString modifyCommandLineView(@NotNull TermString termString,
      @NotNull String input) {
    if(commandHandler == null) {
      return termString;
    }
    try {
      CommandArgument[] args = parseArguments(input);
      CommandHandler<CommandArgument> raw = (CommandHandler<CommandArgument>)
          commandHandler;
      return raw.view(termString, args);
    } catch (CommandParseException e) {
      LOGGER.error(e);
    }
    return termString;
  }

  @Override
  public boolean dispatchCommand(@NotNull String line) {
    if(commandHandler == null) {
      return false;
    }
    try {
      CommandArgument[] args = commandHandler.parser().parse(line);
      commandHandler(CommandArgument.class)
          .command(args, line);
    } catch (CommandParseException e) {
      throw new RuntimeException(e);
    }
    return false;
  }

  @Override
  public @NotNull LineReader lineReader() {
    return lineReader;
  }

  @Override
  public void lineReader(@NotNull LineReader lineReader) {
    InternalLineReader internalLineReader = (InternalLineReader) lineReader;
    SetLock<CLITerminal> lock = internalLineReader.getSetLock();
    lock.set(this);
    removeLine();
    this.lineReader = lineReader;
    printLine();
  }

  @Override
  public void lineReading(boolean state) {
    lineReading = state;
    if(lineReading) {
      printLine();
    } else {
      removeLine();
    }
  }

  @Override
  public boolean lineReading() {
    return lineReading;
  }

  @Override
  public void setOutputRecordingBuffer(@Nullable RecordingBuffer recordingBuffer) {
    this.recordingBuffer = recordingBuffer;
  }

  @Override
  public @Nullable RecordingBuffer outputRecordingBuffer() {
    return recordingBuffer;
  }

  @Override
  public boolean restoreRecordedOutput() {
    try {
      recordingBuffer.writeTo(FD_OUT);
      return true;
    } catch (IOException e) {
      LOGGER.error("Failed to restore recorded output", e);
    }
    return false;
  }

  @Override
  public void clearRecordedInput() {
    recordingBuffer.clear();
  }

  protected void printLine() {
    lock.lock();
    try {
      if(lineView != null || !lineReading) {
        return;
      }
      TerminalBuffer buffer = new TerminalBuffer();
      lineView = lineReader.lineRenderer().print(this, lineReader, buffer, true);
      FD_OUT.write(buffer.toBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to print line", e);
    } finally {
      lock.unlock();
    }
  }

  protected void printDeathLine() {
    lock.lock();
    try {
      TerminalBuffer buffer = new TerminalBuffer();
      lineView = lineReader.lineRenderer().print(this, lineReader, buffer, false);
      buffer.append("\n");
      FD_OUT.write(buffer.toBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to print line", e);
    } finally {
      lock.unlock();
    }
  }

  protected void removeLine() {
    lock.lock();
    try {
      if(lineView == null) {
        return;
      }
      TerminalBuffer buffer = new TerminalBuffer();
      lineReader.lineRenderer().remove(this, lineReader, buffer, lineView);
      FD_OUT.write(buffer.toBytes());
      lineView = null;
    } catch (IOException e) {
      LOGGER.error("Failed to remove line", e);
    } finally {
      lock.unlock();
    }
  }

  public void updateLine() {
    lock.lock();
    try {
      if(lineView == null) {
        return;
      }
      TerminalBuffer buffer = new TerminalBuffer();
      lineReader.lineRenderer().remove(this, lineReader, buffer, lineView);
      lineView = lineReader.lineRenderer().print(this, lineReader, buffer, true);
      FD_OUT.write(buffer.toBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to update line", e);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void update() {
    super.update();
    updateLine();
  }

  @Override
  public @NotNull String readLine(@NotNull String prefix) throws IOException {
    return readLine(prefix, LineReader.DEFAULT_FLAGS);
  }

  @Override
  public @NotNull String readPassword(@NotNull String prefix) throws IOException {
    return readLine(prefix, LineReader.FLAG_INSERT_MODE | LineReader.FLAG_KEEP_LINE);
  }

  @Override
  public @NotNull String readLine(@NotNull String prefix, int flags) throws IOException {
    LineReader prevReader = lineReader();
    try {
      DefaultLineReader newLineReader = new DefaultLineReader();
      newLineReader.flags(flags);
      newLineReader.lineRenderer(new PrefixedLineRenderer(prefix));
      lineReader(newLineReader);
      return newLineReader.readLine();
    } catch (InterruptedException e) {
      throw new IOException("Failed to read line", e);
    } finally {
      lineReader(prevReader);
    }
  }

  protected void updateWindowSize(@NotNull TermDim windowSize) {
    lock.lock();
    try {
      if(lineView == null) {
        return;
      }
      lineView = lineView.convert(windowSize);
      updateLine();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public void clear() {
    super.clear();
    printLine();
  }

  @Override
  public void eventReleaseLine(@NotNull String line) {
    if((lineReader.flags() & LineReader.FLAG_KEEP_LINE) == LineReader.FLAG_KEEP_LINE) {
      printDeathLine();
    }
    eventBus.post(new LineReleaseEvent(line));
  }

  @Override
  public @Nullable String accept(@NotNull String out) {
    removeLine();
    return out;
  }

  @Override
  public void afterPrint(@NotNull String value) {
    if(lineReading) {
      printLine();
    }
    if(recordingBuffer != null) {
      recordingBuffer.write(value);
    }
  }

  @SubscribeEvent
  public void onWindowEvent(WindowInputEvent e) {
    updateWindowSize(e.newDimension());
  }

  @SubscribeEvent
  public void onKeyboardEvent(KeyboardInputEvent e) {
    if(!lineReading) {
      return;
    }
    InternalLineReader internalLineReader = (InternalLineReader) lineReader;
    internalLineReader.handleKeyboardEvent(e);
    updateLine();
  }

}
