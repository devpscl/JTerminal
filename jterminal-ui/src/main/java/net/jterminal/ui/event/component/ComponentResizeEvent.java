package net.jterminal.ui.event.component;

import net.devpscl.eventbus.Event;
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
    return oldDimension.copy();
  }

  public @NotNull TermDim newDimension() {
    return newDimension.copy();
  }
}
