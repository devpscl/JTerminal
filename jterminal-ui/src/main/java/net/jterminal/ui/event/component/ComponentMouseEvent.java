package net.jterminal.ui.event.component;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.input.MouseInputEvent;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.event.special.AbstractComponentEvent;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class ComponentMouseEvent extends AbstractComponentEvent<Component> {

  private final Button button;
  private final Action action;
  private final TermPos terminalPosition;
  private boolean cancelledAction = false;
  protected boolean intercept = false;
  protected boolean ignoreChildComponents = false;

  public ComponentMouseEvent(@NotNull Component component, @NotNull Button button,
      @NotNull Action action,
      @NotNull TermPos terminalPosition, boolean cancelledAction,
      boolean intercept, boolean ignoreChildComponents) {
    super(component);
    this.button = button;
    this.action = action;
    this.terminalPosition = terminalPosition;
    this.cancelledAction = cancelledAction;
  }

  public ComponentMouseEvent(@NotNull Component component, @NotNull MouseInputEvent e) {
    super(component);
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

  public @NotNull ComponentMouseEvent shiftPosition(@NotNull TermPos origin,
      @NotNull Component component) {
    TermPos tpos = terminalPosition.copy().subtractShift(origin);
    return new ComponentMouseEvent(component, button, action, tpos, cancelledAction,
        intercept, ignoreChildComponents);
  }

  public @NotNull ComponentMouseEvent copy() {
    return new ComponentMouseEvent(component(), button, action, terminalPosition.copy(),
        cancelledAction, intercept, ignoreChildComponents);
  }

}
