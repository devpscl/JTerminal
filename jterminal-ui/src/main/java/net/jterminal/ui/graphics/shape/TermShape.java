package net.jterminal.ui.graphics.shape;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface TermShape {

  @NotNull TermDim size();

  void render(@NotNull TermGraphics graphics, @NotNull TermPos pos,
      @NotNull TermDim dim);

}
