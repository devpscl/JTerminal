package net.jterminal.ui.scrollbar;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.IntRange;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface ScrollBar {

  int minBarSize();

  int maxBarSize();

  @NotNull ScrollBar minBarSize(int size);

  @NotNull ScrollBar maxBarSize(int size);

  @NotNull Axis axis();

  @NotNull ScrollBarStyle style();

  @NotNull ScrollBar style(@NotNull ScrollBarStyle style);

  @NotNull ScrollBar update(int index, int max);

  @NotNull ScrollBar update(@NotNull ViewShifter shifter, boolean reversed);

  int index();

  int max();

  float barShrinkFactor();

  @NotNull ScrollBar index(int index);

  @NotNull ScrollBar scrollUp(int count);

  @NotNull ScrollBar scrollDown(int count);

  @NotNull ScrollBar indexStep(int index, int size);

  @NotNull ScrollBar barShrinkFactor(float value);

  boolean performInteract(@NotNull TermPos originPos, int size);

  @NotNull IntRange createRegion(int size);

  boolean isScrollable();

  void draw(@NotNull TermGraphics graphics, int size);

  static @NotNull ScrollBar create(@NotNull Axis axis) {
    return new ScrollBarImpl(axis);
  }


}
