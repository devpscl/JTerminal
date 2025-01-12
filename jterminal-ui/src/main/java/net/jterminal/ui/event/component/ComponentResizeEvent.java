package net.jterminal.ui.event.component;

import net.jterminal.event.Event;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class ComponentResizeEvent implements Event {

  private final TermDim oldDimension;
  private final TermDim newDimension;

  public ComponentResizeEvent(@NotNull TermDim oldDimension, @NotNull TermDim newDimension) {
    this.oldDimension = oldDimension;
    this.newDimension = newDimension;
  }

  public @NotNull TermDim oldDimension() {
    return oldDimension.clone();
  }

  public @NotNull TermDim newDimension() {
    return newDimension.clone();
  }
}
