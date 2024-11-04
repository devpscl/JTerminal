package net.jterminal.io;

import java.io.InputStream;
import net.jterminal.input.TerminalInput;
import org.jetbrains.annotations.NotNull;

public class TerminalInputStream extends InputStream {

  private final TerminalInput terminalInput;

  public TerminalInputStream(@NotNull TerminalInput terminalInput) {
    this.terminalInput = terminalInput;
  }

  @Override
  public int read() {
    return terminalInput.read();
  }

  @Override
  public int read(byte @NotNull [] b) {
    return terminalInput.read(b);
  }

  @Override
  public int read(byte @NotNull [] b, int off, int len) {
    return terminalInput.read(b, off, len);
  }

  @Override
  public byte[] readAllBytes() {
    byte[] bytes = new byte[terminalInput.available()];
    terminalInput.read(bytes);
    return bytes;
  }

  @Override
  public synchronized void reset() {
    terminalInput.reset();
  }

  @Override
  public void close() {
    terminalInput.close();
  }

  @Override
  public int available() {
    return terminalInput.available();
  }

}
