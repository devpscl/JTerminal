package net.jterminal.cli;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.annotation.SubscribeEvent;
import net.jterminal.cli.event.LineReleaseEvent;
import net.jterminal.cli.line.DefaultLineReader;
import net.jterminal.cli.line.InternalLineReader;
import net.jterminal.cli.line.LineReader;
import net.jterminal.cli.line.LineView;
import net.jterminal.cli.util.RecordingBuffer;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.input.WindowInputEvent;
import net.jterminal.instance.AbstractNativeTerminal;
import net.jterminal.io.ListenedPrintStream;
import net.jterminal.util.SetLock;
import net.jterminal.util.TerminalDimension;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AbstractCLITerminal extends AbstractNativeTerminal<CLITerminal>
    implements CLITerminal, ListenedPrintStream.Listener {

  private LineReader lineReader;
  private volatile boolean lineReading = false;
  private final ReentrantLock lock = new ReentrantLock();
  private LineView lineView = null;
  private RecordingBuffer recordingBuffer = null;

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
    flags(FLAG_EXTENDED_INPUT | FLAG_WINDOW_INPUT);
    eventBus().register(this);
  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();
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
      lineView = lineReader.lineRenderer().print(this, lineReader, buffer);
      FD_OUT.write(buffer.toBytes());
    } catch (IOException e) {
      LOGGER.error("Failed to print line", e);
    } finally {
      lock.unlock();
    }
  }

  protected void printLegacyLine() {
    lock.lock();
    try {
      TerminalBuffer buffer = new TerminalBuffer();
      lineReader.lineRenderer().printLegacy(this, lineReader, buffer);
      write(new String(buffer.toBytes()));
    } catch (IOException e) {
      LOGGER.error("Failed to print legacy line", e);
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
      lineView = lineReader.lineRenderer().print(this, lineReader, buffer);
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

  protected void updateWindowSize(@NotNull TerminalDimension windowSize) {
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
  public void eventReleaseLine(@NotNull String line) {
    if((lineReader.flags() & LineReader.FLAG_PRINT_LEGACY) == LineReader.FLAG_PRINT_LEGACY) {
      printLegacyLine();
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
