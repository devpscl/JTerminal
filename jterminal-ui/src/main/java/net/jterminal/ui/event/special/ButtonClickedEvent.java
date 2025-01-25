package net.jterminal.ui.event.special;

import net.jterminal.ui.component.selectable.ButtonComponent;
import org.jetbrains.annotations.NotNull;

public class ButtonClickedEvent extends AbstractComponentEvent<ButtonComponent> {

  public ButtonClickedEvent(@NotNull ButtonComponent component) {
    super(component);
  }

}
