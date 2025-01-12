package net.jterminal.ui.event.component;

import net.jterminal.input.KeyboardInputEvent;
import org.jetbrains.annotations.NotNull;

public class SelectableComponentKeyEvent extends ComponentKeyEvent {

  private boolean interceptInput = false;

  public SelectableComponentKeyEvent(
      @NotNull KeyboardInputEvent event) {
    super(event);
  }

  public SelectableComponentKeyEvent(@NotNull KeyboardInputEvent event,
      boolean cancelledAction) {
    super(event, cancelledAction);
  }

  public void interceptInput(boolean interceptInput) {
    this.interceptInput = interceptInput;
  }

  public boolean interceptInput() {
    return interceptInput;
  }
}
