package net.jterminal.input;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class MouseInputEvent extends InputEvent {

  private final Button button;
  private final Action action;
  private final TermPos terminalPosition;

  public MouseInputEvent(@NotNull String raw, @NotNull Button button, @NotNull Action action,
      @NotNull TermPos terminalPosition) {
    super(raw);
    this.button = button;
    this.action = action;
    this.terminalPosition = terminalPosition;
  }

  public MouseInputEvent(byte[] raw, byte button, byte action, int x, int y) {
    this(new String(raw), Button.values()[button],
        Action.values()[action], new TermPos(x, y));
  }

  @Override
  public final @NotNull Type type() {
    return Type.MOUSE;
  }

  public @NotNull Button button() {
    return button;
  }

  public @NotNull Action action() {
    return action;
  }

  public @NotNull TermPos terminalPosition() {
    return terminalPosition;
  }
}
