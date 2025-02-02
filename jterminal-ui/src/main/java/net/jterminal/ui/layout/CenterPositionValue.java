package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class CenterPositionValue implements PositionValue {

  @Override
  public int x(@NotNull TermDim currentDim) {
    return (int) Math.round(currentDim.width()/2D);
  }

  @Override
  public int y(@NotNull TermDim currentDim) {
    return (int) Math.round(currentDim.height()/2D);
  }
}
