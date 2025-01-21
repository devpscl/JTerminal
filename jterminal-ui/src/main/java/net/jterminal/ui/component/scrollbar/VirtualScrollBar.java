package net.jterminal.ui.component.scrollbar;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.MathUtil;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class VirtualScrollBar {

  private final Axis axis;
  private int size = 0;
  private int endShrinkLevel = 30;
  private int currentLevel = 0;
  private int totalLevel = 0;
  private int minBarSize = 1;
  private int maxBarSize = Integer.MAX_VALUE;
  private ScrollBarStyle style = new DefaultScrollBarStyle();

  public VirtualScrollBar(@NotNull Axis axis) {
    this.axis = axis;
  }

  public @NotNull ScrollBarStyle scrollBarStyle() {
    return style;
  }

  public void scrollBarStyle(@NotNull ScrollBarStyle scrollBarStyle) {
    this.style = scrollBarStyle;
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

  public void setup(@NotNull ViewShifter viewShifter, boolean reversed) {
    int maxOffset = viewShifter.maxShift();
    int offset = viewShifter.shift();
    totalLevel(maxOffset);
    currentLevelIndex(reversed ? maxOffset - offset : offset);
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
    final int safeCurrentLevel = MathUtil.nonNegative(Math.min(currentLevel, totalLevel - 1));

    double levelShrinkQuote =
        MathUtil.nonNegative(1D - ((double) levelEndIndex / endShrinkLevel));
    double offsetLength = (maxSize - minBarSize) * levelShrinkQuote;
    int barLength = Math.max(minBarSize, (int) Math.floor(offsetLength) + minBarSize);

    int halfBarLength = (int) Math.floor(barLength/2D);
    double halfBarLengthD = barLength/2D;
    double levelPortion = (double) safeCurrentLevel / levelEndIndex;
    double regionEnd = scrollableSize - halfBarLengthD;
    double offsetPosition = (regionEnd-halfBarLengthD) * levelPortion;

    int pos = (int) Math.floor(offsetPosition + halfBarLength);

    int start = pos - halfBarLength;
    int end = pos + halfBarLength;

    return new ScrollBarActiveRegion(start, end);
  }

  public void draw(@NotNull TermGraphics graphics) {
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
      return offset.copy().addShift(new TermPos(1, size));
    }
    return offset.copy().addShift(new TermPos(size, 1));
  }

  public record ScrollBarActiveRegion(int start, int end) {}

}
