package net.jterminal.ui.event.component;


import net.jterminal.ui.component.Component;
import net.jterminal.ui.event.special.AbstractComponentEvent;
import org.jetbrains.annotations.NotNull;

public class ComponentUnselectEvent extends AbstractComponentEvent<Component> {

  public ComponentUnselectEvent(@NotNull Component component) {
    super(component);
  }
}
