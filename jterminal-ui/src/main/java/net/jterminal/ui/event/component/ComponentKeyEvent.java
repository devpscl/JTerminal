package net.jterminal.ui.event.component;

import net.devpscl.eventbus.Event;
import net.jterminal.input.Keyboard.State;
import net.jterminal.input.KeyboardInputEvent;
import org.jetbrains.annotations.NotNull;

public class ComponentKeyEvent implements Event {

  private final KeyboardInputEvent event;
  private boolean cancelledAction = false;
  private boolean intercept = false;
  protected boolean ignoreChildComponents = false;

  public ComponentKeyEvent(@NotNull KeyboardInputEvent event) {
    this.event = event;
  }

  protected ComponentKeyEvent(KeyboardInputEvent event, boolean cancelledAction,
      boolean intercept, boolean ignoreChildComponents) {
    this.event = event;
    this.cancelledAction = cancelledAction;
  }

  public @NotNull KeyboardInputEvent event() {
    return event;
  }

  public char input() {
    return event.input();
  }

  public int key() {
    return event.key();
  }

  public @NotNull State state() {
    return event.state();
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

  public @NotNull ComponentKeyEvent copy() {
    return new ComponentKeyEvent(event, cancelledAction, intercept, ignoreChildComponents);
  }

}
