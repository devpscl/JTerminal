package net.jterminal.ui.component;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public interface Displayable {

  void repaint();

  void repaintFully();

  void paint(@NotNull TermGraphics graphics);

  boolean isVisible();

  void visible(boolean state);

}
