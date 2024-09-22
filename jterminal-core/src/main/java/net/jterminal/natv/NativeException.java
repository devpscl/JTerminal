package net.jterminal.natv;

public class NativeException extends Exception {

  public NativeException() {
  }

  public NativeException(String message) {
    super(message);
  }

  public NativeException(String message, Throwable cause) {
    super(message, cause);
  }

  public NativeException(Throwable cause) {
    super(cause);
  }

  public NativeException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
