package net.jterminal.exception;

public class TerminalProviderException extends Exception {

  public TerminalProviderException(String message) {
    super(message);
  }

  public TerminalProviderException(String message, Throwable cause) {
    super(message, cause);
  }

  public TerminalProviderException(Throwable cause) {
    super(cause);
  }

  public TerminalProviderException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
