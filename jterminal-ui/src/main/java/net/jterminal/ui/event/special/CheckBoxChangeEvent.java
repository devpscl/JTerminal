package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.CheckBoxComponent;
import org.jetbrains.annotations.NotNull;

public class CheckBoxChangeEvent extends AbstractComponentEvent<CheckBoxComponent> {

  private final boolean newState;

  public CheckBoxChangeEvent(@NotNull CheckBoxComponent component,
      boolean newState) {
    super(component);
    this.newState = newState;
  }

  public boolean newState() {
    return newState;
  }
}
