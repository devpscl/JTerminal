package net.jterminal.input;

import org.jetbrains.annotations.NotNull;

public class InputEvent {

  public enum Type {
    KEYBOARD,
    MOUSE,
    WINDOW,
    UNKNOWN
  }

  private final @NotNull String raw;

  public InputEvent(@NotNull String raw) {
    this.raw = raw;
  }

  public InputEvent(byte @NotNull [] raw) {
    this.raw = new String(raw);
  }

  public @NotNull Type type() {
    return Type.UNKNOWN;
  }

  public @NotNull String raw() {
    return raw;
  }

}
