package net.jterminal.instance;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.exception.TerminalProviderException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class AbstractTerminal<T extends Terminal>
    implements Terminal {

  protected final Class<?> interfaceType;

  protected final PrintStream out;
  protected final PrintStream err;


  public AbstractTerminal(@NotNull Class<T> interfaceType) {
    this(interfaceType, null, null);
  }

  public AbstractTerminal(@NotNull Class<T> interfaceType,
      @Nullable PrintStream out, @Nullable PrintStream err) {
    this.interfaceType = interfaceType;
    this.out = out == null ? new PrintStream(FD_OUT) : out;
    this.err = err == null ? new PrintStream(FD_ERR) : out;
  }

  public void enable() throws TerminalProviderException {
    System.setOut(out);
    System.setErr(err);
    System.setIn(FD_IN);
  }

  public abstract void disable() throws TerminalProviderException;

  @Override
  public void writeLine(@NotNull String x) {
    out.println(x);
  }

  @Override
  public void writeLine(double x) {
    out.println(x);
  }

  @Override
  public void writeLine(float x) {
    out.println(x);
  }

  @Override
  public void writeLine(boolean x) {
    out.println(x);
  }

  @Override
  public void writeLine(char x) {
    out.println(x);
  }

  @Override
  public void writeLine(long x) {
    out.println(x);
  }

  @Override
  public void writeLine(int x) {
    out.println(x);
  }

  @Override
  public void writeLine(short x) {
    out.println(x);
  }

  @Override
  public void writeLine(byte x) {
    out.println(x);
  }

  @Override
  public void writeLine(byte[] bytes, int off, int len) {
    out.write(bytes, off, len);
    out.println();
  }

  @Override
  public void writeLine(@NotNull Object obj) {
    out.println(obj);
  }

  @Override
  public void writeLine(@NotNull TerminalBuffer terminalBuffer) {
    out.println(terminalBuffer);
  }

  @Override
  public void write(@NotNull String x) {
    out.print(x);
  }

  @Override
  public void write(@NotNull TerminalBuffer terminalBuffer) {
    out.print(terminalBuffer);
  }

  @Override
  public void write(double x) {
    out.print(x);
  }

  @Override
  public void write(float x) {
    out.print(x);
  }

  @Override
  public void write(boolean x) {
    out.print(x);
  }

  @Override
  public void write(char x) {
    out.print(x);
  }

  @Override
  public void write(long x) {
    out.print(x);
  }

  @Override
  public void write(int x) {
    out.print(x);
  }

  @Override
  public void write(short x) {
    out.print(x);
  }

  @Override
  public void write(byte x) {
    out.print(x);
  }

  @Override
  public void write(byte[] bytes, int off, int len) {
    out.write(bytes, off, len);
  }

  @Override
  public void write(@NotNull Object obj) {
    out.print(obj);
  }

}
