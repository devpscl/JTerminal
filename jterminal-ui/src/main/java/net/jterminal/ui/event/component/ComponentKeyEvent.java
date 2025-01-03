package net.jterminal.ui.event.component;

import net.jterminal.event.Event;
import net.jterminal.input.KeyboardInputEvent;
import org.jetbrains.annotations.NotNull;

public class ComponentKeyEvent implements Event {

  private final KeyboardInputEvent event;
  private boolean cancelledAction = false;

  public ComponentKeyEvent(@NotNull KeyboardInputEvent event) {
    this.event = event;
  }

  private ComponentKeyEvent(KeyboardInputEvent event, boolean cancelledAction) {
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

  public boolean cancelledAction() {
    return cancelledAction;
  }

  public void cancelledAction(boolean cancelledAction) {
    this.cancelledAction = cancelledAction;
  }

  public @NotNull ComponentKeyEvent copy() {
    return new ComponentKeyEvent(event, cancelledAction);
  }

}
