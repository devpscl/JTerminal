package net.jterminal.ui.event.component;

import net.devpscl.eventbus.Event;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class ComponentMouseEvent implements Event {

  private final Button button;
  private final Action action;
  private final TermPos terminalPosition;
  private boolean cancelledAction = false;
  protected boolean intercept = false;
  protected boolean ignoreChildComponents = false;

  public ComponentMouseEvent(@NotNull Button button,
      @NotNull Action action,
      @NotNull TermPos terminalPosition, boolean cancelledAction,
      boolean intercept, boolean ignoreChildComponents) {
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

  public @NotNull TermPos position() {
    return terminalPosition;
  }

  public boolean isCancelledAction() {
    return cancelledAction;
  }

  public void cancelledAction(boolean cancelledAction) {
    this.cancelledAction = cancelledAction;
  }

  public void intercept(boolean state) {
    this.intercept = state;
  }

  public boolean isIntercept() {
    return intercept;
  }

  public void ignoreChildComponents(boolean ignoreChildComponents) {
    this.ignoreChildComponents = ignoreChildComponents;
  }

  public boolean isIgnoreChildComponents() {
    return ignoreChildComponents;
  }

  public @NotNull ComponentMouseEvent shiftPosition(@NotNull TermPos origin) {
    TermPos tpos = terminalPosition.copy().subtractShift(origin);
    return new ComponentMouseEvent(button, action, tpos, cancelledAction,
        intercept, ignoreChildComponents);
  }

  public @NotNull ComponentMouseEvent copy() {
    return new ComponentMouseEvent(button, action, terminalPosition.copy(),
        cancelledAction, intercept, ignoreChildComponents);
  }

}
