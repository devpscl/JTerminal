package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class CenterPositionValue implements PositionValue {

  private int offLength;

  public CenterPositionValue(int offLength) {
    this.offLength = offLength;
  }

  @Override
  public int x(@NotNull TermDim currentDim) {
    int off = 0;
    if(offLength > 0) {
      off = offLength / 2;
    }
    return (int) Math.round(currentDim.width()/2D) - off;
  }

  @Override
  public int y(@NotNull TermDim currentDim) {
    int off = 0;
    if(offLength > 0) {
      off = offLength / 2;
    }
    return (int) Math.round(currentDim.height()/2D) - off;
  }
}
