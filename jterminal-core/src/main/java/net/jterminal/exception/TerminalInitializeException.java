package net.jterminal.exception;

public class TerminalInitializeException extends Exception {

  public TerminalInitializeException() {
    super();
  }

  public TerminalInitializeException(String message) {
    super(message);
  }

  public TerminalInitializeException(String message, Throwable cause) {
    super(message, cause);
  }

  public TerminalInitializeException(Throwable cause) {
    super(cause);
  }

  protected TerminalInitializeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
