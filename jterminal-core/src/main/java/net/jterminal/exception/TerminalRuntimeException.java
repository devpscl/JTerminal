package net.jterminal.exception;

public class TerminalRuntimeException extends RuntimeException {

  public TerminalRuntimeException() {
    super();
  }

  public TerminalRuntimeException(String message) {
    super(message);
  }

  public TerminalRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public TerminalRuntimeException(Throwable cause) {
    super(cause);
  }

  protected TerminalRuntimeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
