package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.RadioButtonComponent;
import org.jetbrains.annotations.NotNull;

public class RadioButtonChangeEvent extends AbstractComponentEvent<RadioButtonComponent> {

  private final boolean newState;

  public RadioButtonChangeEvent(@NotNull RadioButtonComponent component,
      boolean newState) {
    super(component);
    this.newState = newState;
  }

  public boolean newState() {
    return newState;
  }
}
