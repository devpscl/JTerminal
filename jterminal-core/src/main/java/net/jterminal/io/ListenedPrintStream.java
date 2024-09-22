package net.jterminal.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListenedPrintStream extends PrintStream {

  private final Listener listener;

  public ListenedPrintStream(@NotNull Listener listener, @NotNull OutputStream out,
      boolean autoFlush, @NotNull Charset charset) {
    super(out, autoFlush, charset);
    this.listener = listener;
  }

  public ListenedPrintStream(@NotNull Listener listener, @NotNull OutputStream out,
      boolean autoFlush) {
    super(out, autoFlush);
    this.listener = listener;
  }

  public ListenedPrintStream(@NotNull Listener listener, @NotNull OutputStream out) {
    super(out);
    this.listener = listener;
  }

  @Override
  public void print(@Nullable String x) {
    String str = listener.accept(x == null ? "null" : x);
    if(str != null) {
      super.print(str);
    }
  }

  @Override
  public void println(@Nullable String x) {
    print((x == null ? "null" : x) + "\n");
  }

  @Override
  public void println() {
    print("\n");
  }

  @Override
  public void println(boolean x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(char x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(long x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(float x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(double x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(char @NotNull [] x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(@Nullable Object x) {
    println(String.valueOf(x));
  }

  @Override
  public void println(int x) {
    println(String.valueOf(x));
  }

  @Override
  public void print(boolean x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(char x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(int x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(long x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(float x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(double d) {
    print(String.valueOf(d));
  }

  @Override
  public void print(char @NotNull [] x) {
    print(String.valueOf(x));
  }

  @Override
  public void print(@Nullable Object x) {
    print(String.valueOf(x));
  }

  @Override
  public PrintStream printf(@NotNull String format, Object... args) {
    print(String.format(format, args));
    return this;
  }

  @Override
  public PrintStream printf(Locale l, @NotNull String format, Object... args) {
    print(String.format(l, format, args));
    return this;
  }

  @Override
  public PrintStream format(@NotNull String format, Object... args) {
    return printf(format, args);
  }

  @Override
  public PrintStream format(Locale l, @NotNull String format, Object... args) {
    return printf(l, format, args);
  }

  @Override
  public PrintStream append(CharSequence csq) {
    print(csq.toString());
    return this;
  }

  @Override
  public PrintStream append(CharSequence csq, int start, int end) {
    print(csq.subSequence(start, end).toString());
    return this;
  }

  @Override
  public PrintStream append(char c) {
    print(c);
    return this;
  }

  public interface Listener {

    @Nullable String accept(@NotNull final String out);

  }


}
