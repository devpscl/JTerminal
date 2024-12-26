package net.jterminal.cli.exception;

public class CommandParseException extends Exception {

  private final int position;

  public CommandParseException(int position) {
    this.position = position;
  }

  public CommandParseException(String message, int position) {
    super(message);
    this.position = position;
  }

  public CommandParseException(String message, Throwable cause, int position) {
    super(message, cause);
    this.position = position;
  }

  public int position() {
    return position;
  }
}
