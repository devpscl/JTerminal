package net.jterminal.ui.component;

import net.jterminal.ui.graphics.CellBuffer;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class ComponentGraphics {

  public static void draw(@NotNull TermGraphics graphics, Component component) {
    CellBuffer buffer = new CellBuffer(component.effectiveSize());
    TermGraphics innerGraphics = TermGraphics.from(buffer);
    innerGraphics.foregroundColor(component.foregroundColor());
    innerGraphics.backgroundColor(component.backgroundColor());
    component.paint(innerGraphics);
    graphics.draw(component.effectivePosition(), innerGraphics);
  }

}
