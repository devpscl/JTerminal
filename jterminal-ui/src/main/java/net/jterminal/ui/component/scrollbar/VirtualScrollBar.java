package net.jterminal.ui.component.scrollbar;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VirtualScrollBar {

  private final Axis axis;
  private int size = 0;
  private int endShrinkLevel = 10;
  private int currentLevel = 0;
  private int totalLevel = 0;
  private int minBarSize = 1;
  private int maxBarSize = Integer.MAX_VALUE;

  public VirtualScrollBar(@NotNull Axis axis) {
    this.axis = axis;
  }

  public @NotNull Axis axis() {
    return axis;
  }

  public void size(int size) {
    this.size = size;
  }

  public int size() {
    return size;
  }

  public int totalLevel() {
    return totalLevel;
  }

  public void totalLevel(int totalLevel) {
    this.totalLevel = Math.max(0, totalLevel);
    currentLevel = Math.min(this.totalLevel, currentLevel);
  }

  public int endShrinkLevel() {
    return endShrinkLevel;
  }

  public void endShrinkLevel(int endShrinkLevel) {
    this.endShrinkLevel = Math.max(0, endShrinkLevel);
  }

  public int currentLevelIndex() {
    return currentLevel;
  }

  public void currentLevelIndex(int index) {
    this.currentLevel = Math.max(0, Math.min(index, this.totalLevel));
  }

  public int minBarSize() {
    return minBarSize;
  }

  public int maxBarSize() {
    return maxBarSize;
  }

  public void minBarSize(int minBarSize) {
    this.minBarSize = Math.max(0, minBarSize);
  }

  public void maxBarSize(int maxBarSize) {
    this.maxBarSize = Math.max(0, maxBarSize);
  }

  public void scrollUp(int count) {
    currentLevelIndex(currentLevel - count);
  }

  public void scrollDown(int count) {
    currentLevelIndex(currentLevel + count);
  }

  public @NotNull ScrollBarActiveRegion createActiveRegion() {
    final int scrollableSize = size - 2;
    final int maxSize = Math.min(maxBarSize, scrollableSize);
    final int levelEndIndex = totalLevel - 1;

    double levelShrinkQuote = Math.min(0, 1D - ((double) levelEndIndex / endShrinkLevel));
    double offsetLength = (maxSize - minBarSize) * levelShrinkQuote;
    int barLength = Math.max(minBarSize, (int) Math.floor(offsetLength) + minBarSize);
    int halfBarLength = (int) Math.floor(barLength/2D);
    double halfBarLengthD = barLength/2D;
    double levelPortion = (double) currentLevel / levelEndIndex;
    double regionEnd = scrollableSize - halfBarLengthD;
    double offsetPosition = (regionEnd-halfBarLengthD) * levelPortion;

    int pos = (int) Math.floor(offsetPosition + halfBarLength);

    int start = pos - halfBarLength;
    int end = pos + halfBarLength;

    return new ScrollBarActiveRegion(start, end);
  }

  public void draw(@NotNull TermPos pos, @NotNull TermGraphics graphics,
      @NotNull ScrollBarStyle style) {
    draw(pos.x(), pos.y(), graphics, style);
  }

  public void draw(int x, int y, @NotNull TermGraphics graphics,
      @NotNull ScrollBarStyle style) {
    if(size < 2) {
      return;
    }
    ScrollBarActiveRegion activeRegion = createActiveRegion();
    int regionLength = size - 2;
    graphics.draw(1, 1, style.getPrefixScrollChar(axis));
    if(axis == Axis.VERTICAL) {
      graphics.draw(1, size, style.getSuffixScrollChar(axis));
      for(int idx = 0; idx < regionLength; idx++) {
        char symbol;
        if(idx >= activeRegion.start() && idx <= activeRegion.end()) {
          symbol = style.getActiveScrollChar(axis);
        } else {
          symbol = style.getInactiveScrollChar(axis);
        }
        graphics.draw(1, idx + 2, symbol);
      }
      return;
    }
    graphics.draw(size, 1, style.getSuffixScrollChar(axis));
    for(int idx = 0; idx < regionLength; idx++) {
      char symbol;
      if(idx >= activeRegion.start() && idx <= activeRegion.end()) {
        symbol = style.getActiveScrollChar(axis);
      } else {
        symbol = style.getInactiveScrollChar(axis);
      }
      graphics.draw(idx + 2, 1, symbol);
    }
  }

  public @NotNull TermPos suffixCharPosition(@NotNull TermPos offset) {
    if(axis == Axis.VERTICAL) {
      return offset.clone().addShift(new TermPos(1, size));
    }
    return offset.clone().addShift(new TermPos(size, 1));
  }

  public record ScrollBarActiveRegion(int start, int end) {}

}
