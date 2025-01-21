package net.jterminal.ui.component;

import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public interface HeadSurfacePainter {

  void paintSurface(@NotNull TermGraphics graphics);

  default void processSurfaceMouseInput(@NotNull ComponentMouseEvent event) {}

}
