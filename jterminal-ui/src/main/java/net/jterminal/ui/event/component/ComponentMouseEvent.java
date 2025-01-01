package net.jterminal.ui.event.component;

import net.jterminal.event.Event;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class ComponentMouseEvent implements Event {

  private final Button button;
  private final Action action;
  private final TerminalPosition terminalPosition;

  public ComponentMouseEvent(@NotNull Button button,
      @NotNull Action action,
      @NotNull TerminalPosition terminalPosition) {
    this.button = button;
    this.action = action;
    this.terminalPosition = terminalPosition;
  }

  public ComponentMouseEvent(@NotNull MouseInputEvent e) {
    this.button = e.button();
    this.action = e.action();
    this.terminalPosition = e.terminalPosition();
  }

  public @NotNull Button button() {
    return button;
  }

  public @NotNull Action action() {
    return action;
  }

  public @NotNull TerminalPosition position() {
    return terminalPosition;
  }

  public @NotNull ComponentMouseEvent shiftPosition(@NotNull TerminalPosition origin) {
    TerminalPosition tpos = terminalPosition.clone();
    tpos.subtract(origin).add(1);
    return new ComponentMouseEvent(button, action, tpos);
  }

}
