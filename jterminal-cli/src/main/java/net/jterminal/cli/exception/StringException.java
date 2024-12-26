package net.jterminal.cli.exception;

import org.jetbrains.annotations.NotNull;

public class StringException extends Exception {

  private final int position;

  public StringException(int position) {
    this.position = position;
  }

  public StringException(@NotNull String message, int position) {
    super(message);
    this.position = position;
  }

  public StringException(@NotNull String message, @NotNull Throwable cause, int position) {
    super(message, cause);
    this.position = position;
  }

  public int position() {
    return position;
  }

}
