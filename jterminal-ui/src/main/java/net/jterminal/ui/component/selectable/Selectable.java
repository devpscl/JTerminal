package net.jterminal.ui.component.selectable;

import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Container;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Selectable {

  @Nullable Container rootContainer();

  default boolean isSelected() {
    Container container = rootContainer();
    if(container == null) {
      return false;
    }
    if(container instanceof TermScreen screen && this instanceof Component component) {
      return screen.selectedComponent() == component;
    }
    return false;
  }

  boolean allowActionInput(@NotNull KeyboardInputEvent event);

}
