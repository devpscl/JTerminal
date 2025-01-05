package net.jterminal.ui.component;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class PaneContainer extends Container implements Resizeable {

  private TermDim minimumSize = new TermDim(1, 1);
  private TermDim maximumSize = new TermDim(Integer.MAX_VALUE,
      Integer.MAX_VALUE);

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {
    ComponentGraphics.prepare(component);
    ComponentGraphics.draw(graphics, component);
  }

  @Override
  public @NotNull TermDim minimumSize() {
    return minimumSize;
  }

  @Override
  public @NotNull TermDim maximumSize() {
    return maximumSize;
  }

  public void minimumSize(@NotNull TermDim minimumSize) {
    this.minimumSize = minimumSize;
  }

  public void maximumSize(@NotNull TermDim maximumSize) {
    this.maximumSize = maximumSize;
  }
}
