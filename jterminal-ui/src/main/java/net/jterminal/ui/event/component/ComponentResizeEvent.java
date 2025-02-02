package net.jterminal.ui.event.component;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.event.special.AbstractComponentEvent;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class ComponentResizeEvent extends AbstractComponentEvent<Component> {

  private final TermDim oldDimension;
  private final TermDim newDimension;

  public ComponentResizeEvent(@NotNull Component component, @NotNull TermDim oldDimension,
      @NotNull TermDim newDimension) {
    super(component);
    this.oldDimension = oldDimension;
    this.newDimension = newDimension;
  }

  public @NotNull TermDim oldDimension() {
    return oldDimension.copy();
  }

  public @NotNull TermDim newDimension() {
    return newDimension.copy();
  }
}
