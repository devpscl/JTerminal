package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface Layout {

  static @NotNull PositionValue center() {
    return new CenterPositionValue();
  }

  static @NotNull DimensionValue fill() {
    return new FillDimensionValue(1.0F);
  }

  static @NotNull DimensionValue fill(float percent) {
    return new FillDimensionValue(percent);
  }

  static @NotNull PositionValue origin() {
    return new OriginPositionValue();
  }

  static @NotNull PositionValue relative(@NotNull Anchor anchor) {
    return new RelativePositionValue(null, anchor);
  }

  static @NotNull PositionValue relative(@NotNull Component component, @NotNull Anchor anchor) {
    return new RelativePositionValue(component, anchor);
  }

  static @NotNull Modifier scale(@Range(from = 0, to = 1) float val) {
    return new ScaleModifier(val);
  }

  static @NotNull Modifier offset(int off) {
    return new OffsetModifier(off);
  }

  static @NotNull Modifier min(int value) {
    return new MinimumModifier(value);
  }

  static @NotNull Modifier max(int value) {
    return new MaximumModifier(value);
  }

  interface PositionValue {

    int x(@NotNull TermDim currentDim);

    int y(@NotNull TermDim currentDim);

  }

  interface DimensionValue {

    int width(@NotNull TermDim currentDim, @NotNull TermPos pos);

    int height(@NotNull TermDim currentDim, @NotNull TermPos pos);

  }

  interface Modifier {

    int get(@NotNull TermDim dim, int value, @NotNull Axis axis);

  }

  static @NotNull PositionValue createPositionValue(int x, int y) {
    return new PositionValueImpl(x, y);
  }

  static @NotNull DimensionValue createDimensionValue(int w, int h) {
    return new DimensionValueImpl(w, h);
  }

}
