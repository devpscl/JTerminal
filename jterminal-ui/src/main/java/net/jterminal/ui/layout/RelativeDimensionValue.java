package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class RelativeDimensionValue implements DimensionValue {

  private final @Nullable Component source;
  private final @NotNull Anchor anchor;
  private final boolean dockingAnchor;

  public RelativeDimensionValue(@Nullable Component source,
      @NotNull Anchor anchor, boolean dockingAnchor) {
    this.source = source;
    this.anchor = anchor;
    this.dockingAnchor = dockingAnchor;
  }

  @Override
  public int width(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    if(anchor == Anchor.TOP || anchor == Anchor.BOTTOM) {
      return 1;
    }
    if(source == null) {
      return 0;
    }
    source.recalculateProperties();
    TermPos termPos = source.currentPosition();
    int value = termPos.x() - pos.x();
    if(anchor == Anchor.RIGHT) {
      value += source.currentDimension().width();
      return dockingAnchor ? value + 1 : value;
    }
    return dockingAnchor ? value : value + 1;
  }

  @Override
  public int height(@NotNull TermDim currentDim, @NotNull TermPos pos) {
    if(anchor == Anchor.LEFT || anchor == Anchor.RIGHT) {
      return 1;
    }
    if(source == null) {
      if(anchor == Anchor.TOP) {
        return 0;
      }
      return currentDim.height() - pos.y() + TermPos.AXIS_ORIGIN;
    }
    source.recalculateProperties();
    TermPos termPos = source.currentPosition();
    int value = termPos.y() - pos.y();
    if(anchor == Anchor.BOTTOM) {
      value += source.currentDimension().height();
      return dockingAnchor ? value + 1 : value;
    }
    return dockingAnchor ? value : value + 1;
  }
}
