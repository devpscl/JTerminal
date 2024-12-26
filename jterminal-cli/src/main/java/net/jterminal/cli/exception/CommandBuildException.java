package net.jterminal.cli.exception;

public class CommandBuildException extends Exception {

  public CommandBuildException() {
  }

  public CommandBuildException(String message) {
    super(message);
  }

  public CommandBuildException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommandBuildException(Throwable cause) {
    super(cause);
  }

  public CommandBuildException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
