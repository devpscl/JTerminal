package net.jterminal.ui.component;

import net.jterminal.input.MouseInputEvent;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public interface HeadSurfacePainter {

  void paintGlobal(@NotNull TermGraphics graphics);

  default boolean processGlobalMouseInput(@NotNull MouseInputEvent event) {
    return true;
  }

}
