package net.jterminal.cli.command;

import org.jetbrains.annotations.NotNull;

public class CommandArgument {

  private final String raw;
  private final Object value;
  private final int positionStart;
  private final int positionEnd;

  public CommandArgument(@NotNull String raw, @NotNull Object value, int start, int end) {
    this.raw = raw;
    this.value = value;
    this.positionStart = start;
    this.positionEnd = end;
  }

  public int positionStart() {
    return positionStart;
  }

  public int positionEnd() {
    return positionEnd;
  }

  public @NotNull String raw() {
    return raw;
  }

  public @NotNull Object value() {
    return value;
  }

  public @NotNull String asString() {
    return String.valueOf(value);
  }

  public boolean isNumber() {
    return value instanceof Number;
  }

  public @NotNull Number asNumber() {
    if(!isNumber()) {
      throw new IllegalStateException("Value of argument is not a number");
    }
    return (Number) value;
  }

}
