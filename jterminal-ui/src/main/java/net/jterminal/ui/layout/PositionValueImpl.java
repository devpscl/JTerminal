package net.jterminal.ui.layout;

import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

final class PositionValueImpl implements PositionValue {

  private final int x;
  private final int y;

  public PositionValueImpl(int x, int y) {
    this.x = x;
    this.y = y;
  }

  @Override
  public int x(@NotNull TermDim currentDim) {
    return x;
  }

  @Override
  public int y(@NotNull TermDim currentDim) {
    return y;
  }
}
