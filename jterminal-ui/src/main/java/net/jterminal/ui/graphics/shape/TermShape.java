package net.jterminal.ui.graphics.shape;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public interface TermShape {

  @NotNull TerminalDimension size();

  void render(@NotNull TermGraphics graphics, @NotNull TerminalPosition pos,
      @NotNull TerminalDimension dim);

}
