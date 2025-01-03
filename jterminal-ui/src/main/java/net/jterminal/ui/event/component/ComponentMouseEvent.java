package net.jterminal.ui.event.component;

import net.jterminal.event.Event;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.ui.util.PosDimUtil;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class ComponentMouseEvent implements Event {

  private final Button button;
  private final Action action;
  private final TerminalPosition terminalPosition;
  private boolean cancelledAction = false;

  public ComponentMouseEvent(@NotNull Button button,
      @NotNull Action action,
      @NotNull TerminalPosition terminalPosition, boolean cancelledAction) {
    this.button = button;
    this.action = action;
    this.terminalPosition = terminalPosition;
    this.cancelledAction = cancelledAction;
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

  public boolean cancelledAction() {
    return cancelledAction;
  }

  public void cancelledAction(boolean cancelledAction) {
    this.cancelledAction = cancelledAction;
  }

  public @NotNull ComponentMouseEvent shiftPosition(@NotNull TerminalPosition origin) {
    TerminalPosition tpos = PosDimUtil.subtract(terminalPosition, origin);
    return new ComponentMouseEvent(button, action, tpos, cancelledAction);
  }

  public @NotNull ComponentMouseEvent copy() {
    return new ComponentMouseEvent(button, action, terminalPosition.clone(), cancelledAction);
  }

}
