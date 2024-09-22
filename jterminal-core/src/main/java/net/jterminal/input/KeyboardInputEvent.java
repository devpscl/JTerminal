package net.jterminal.input;

import net.jterminal.input.Keyboard.State;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class KeyboardInputEvent extends InputEvent {

  private final char input;
  private final int key;
  private final State state;

  public KeyboardInputEvent(@NotNull String raw,
      char input, int key, @NotNull State state) {
    super(raw);
    this.input = input;
    this.key = key;
    this.state = state;
  }

  public KeyboardInputEvent(byte[] sequence, char input, int key, byte state) {
    this(new String(sequence), input, key, State.values()[state]);
  }

  @Override
  public final @NotNull Type type() {
    return Type.KEYBOARD;
  }

  public int key() {
    return key;
  }

  public char input() {
    return input;
  }

  public @NotNull State state() {
    return state;
  }

}
