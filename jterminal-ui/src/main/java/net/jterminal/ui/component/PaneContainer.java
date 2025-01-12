package net.jterminal.ui.component;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class PaneContainer extends Container implements Resizeable {

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {
    ComponentGraphics.prepare(component);
    ComponentGraphics.draw(graphics, component);
  }


}