package net.jterminal.ui.component;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class PaneContainer extends Container {

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {
    component.updatePositionSize();
    ComponentGraphics.draw(graphics, component);
  }

}
