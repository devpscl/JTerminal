package net.jterminal.system;

public class UnsupportedSystemException extends Exception {

  public UnsupportedSystemException() {
  }

  public UnsupportedSystemException(String message) {
    super(message);
  }

  public UnsupportedSystemException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedSystemException(Throwable cause) {
    super(cause);
  }

  public UnsupportedSystemException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
