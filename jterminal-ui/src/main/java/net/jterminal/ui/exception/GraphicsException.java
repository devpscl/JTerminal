package net.jterminal.ui.exception;

public class GraphicsException extends Exception {

  public GraphicsException() {
  }

  public GraphicsException(String message) {
    super(message);
  }

  public GraphicsException(String message, Throwable cause) {
    super(message, cause);
  }

  public GraphicsException(Throwable cause) {
    super(cause);
  }

  public GraphicsException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
