package net.jterminal.ui.scrollbar;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.IntRange;
import net.jterminal.ui.util.MathUtil;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

class ScrollBarImpl implements ScrollBar {

  private int index = 1;
  private int count = 1;
  private float shrinkFactor = 1F;
  private int minBarSize = 1;
  private int maxBarSize = 0xFFFF;
  private final Axis axis;
  private ScrollBarStyle style = new DefaultScrollBarStyle();

  public ScrollBarImpl(Axis axis) {
    this.axis = axis;
  }

  @Override
  public int minBarSize() {
    return minBarSize;
  }

  @Override
  public int maxBarSize() {
    return maxBarSize;
  }

  @Override
  public @NotNull ScrollBar minBarSize(int size) {
    this.minBarSize = MathUtil.nonNegative(size);
    return this;
  }

  @Override
  public @NotNull ScrollBar maxBarSize(int size) {
    this.maxBarSize = MathUtil.nonNegative(size);
    return this;
  }

  @Override
  public @NotNull Axis axis() {
    return axis;
  }

  @Override
  public @NotNull ScrollBarStyle style() {
    return style;
  }

  @Override
  public @NotNull ScrollBar style(@NotNull ScrollBarStyle style) {
    this.style = style;
    return this;
  }

  @Override
  public @NotNull ScrollBar update(int index, int max) {
    if(max == -1) {
      this.count = -1;
      this.index = -1;
      return this;
    }
    this.count = Math.max(0, max);
    return index(index);
  }

  @Override
  public @NotNull ScrollBar update(@NotNull ViewShifter shifter, boolean reversed) {
    int max = shifter.maxShift();
    int current = shifter.shift();
    return update(reversed ? max - current : current, max + 1);
  }

  @Override
  public int index() {
    return index;
  }

  @Override
  public int max() {
    return count;
  }

  @Override
  public float barShrinkFactor() {
    return shrinkFactor;
  }

  @Override
  public @NotNull ScrollBar index(int level) {
    this.index = Math.max(0, Math.min(count - 1, level));
    return this;
  }

  @Override
  public @NotNull ScrollBar scrollUp(int count) {
    return index(index - count);
  }

  @Override
  public @NotNull ScrollBar scrollDown(int count) {
    return index(index + count);
  }

  @Override
  public @NotNull ScrollBar indexStep(int index, int size) {
    double x = (double) count / (size - 2) * index;
    return index((int)Math.round(x));
  }

  @Override
  public @NotNull ScrollBar barShrinkFactor(float value) {
    this.shrinkFactor = Math.max(0F, value);
    return this;
  }

  @Override
  public boolean performInteract(@NotNull TermPos originPos, int size) {
    final int x = originPos.x();
    final int y = originPos.y();
    final int start = 2;
    final int end = size - 1;
    final int c = axis == Axis.HORIZONTAL ? x : y;
    if(c == 1) {
      scrollUp(1);
      return true;
    }
    if(c == size) {
      scrollDown(1);
      return true;
    }
    if(c < start || c > end) {
      return false;
    }
    int step = c - 1;
    indexStep(step, size);
    return true;
  }

  @Override
  public @NotNull IntRange createRegion(int size) {

    final int maxBarSize = Math.min(maxBarSize(), size);
    final int endIndex = MathUtil.nonNegative(count - 1);

    double barSizeFactor = MathUtil.nonNegative(
        (1D - (double) endIndex * shrinkFactor / size));
    double rawBarLength = (maxBarSize - minBarSize) * barSizeFactor;
    double barLength = Math.max(minBarSize,
        Math.min(maxBarSize, Math.round(rawBarLength) + minBarSize));
    double halfBarLength = barLength/2D;
    double levelPortion = (double) index / endIndex;
    double regionEnd = size - halfBarLength;
    double offsetPosition = (regionEnd-halfBarLength) * levelPortion;
    double pos = Math.round(offsetPosition + halfBarLength);
    int start = (int) (pos - halfBarLength);
    int end = (int) (pos + halfBarLength);
    return new IntRange(start, end);
  }

  @Override
  public boolean isScrollable() {
    return index >= 0 && count > 1;
  }

  @Override
  public void draw(@NotNull TermGraphics graphics, int size) {
    if(size < 2) {
      return;
    }
    final int regionLength = size - 2;
    IntRange range = createRegion(regionLength);

    graphics.draw(1, 1, style.getPrefixScrollChar(axis));
    if(axis == Axis.VERTICAL) {
      graphics.draw(1, size, style.getSuffixScrollChar(axis));
      for(int idx = 0; idx < regionLength; idx++) {
        char symbol;
        if(idx >= range.start() && idx < range.end()) {
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
      if(idx >= range.start() && idx < range.end()) {
        symbol = style.getActiveScrollChar(axis);
      } else {
        symbol = style.getInactiveScrollChar(axis);
      }
      graphics.draw(idx + 2, 1, symbol);
    }
  }
}
