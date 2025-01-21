package net.jterminal.ui.layout;

import java.util.Arrays;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class DimProperty {

  private final DimensionValue dimensionValue;
  private final Modifier[] modifiers;

  public DimProperty() {
    this(Layout.createDimensionValue(1, 1));
  }

  public DimProperty(@NotNull DimensionValue dimensionValue, Modifier...modifiers) {
    this.dimensionValue = dimensionValue;
    this.modifiers = modifiers;
  }

  public @NotNull DimensionValue dimensionValue() {
    return dimensionValue;
  }

  public Modifier[] modifiers() {
    return modifiers;
  }

  public int calculateWidth(@NotNull TermDim dimension, @NotNull TermPos position) {
    int value = dimensionValue.width(dimension.copy(), position.copy());
    for (Modifier modifier : modifiers) {
      value = modifier.get(dimension.copy(), value, Axis.HORIZONTAL);
    }
    value = Math.max(0, Math.min(value, dimension.width() - position.x() + TermPos.AXIS_ORIGIN));
    return value;
  }

  public int calculateHeight(@NotNull TermDim dimension, @NotNull TermPos position) {
    int value = dimensionValue.height(dimension.copy(), position.copy());
    for (Modifier modifier : modifiers) {
      value = modifier.get(dimension.copy(), value, Axis.VERTICAL);
    }
    value = Math.max(0, Math.min(value, dimension.height() - position.y() + TermPos.AXIS_ORIGIN));
    return value;
  }

  public @NotNull DimProperty copy() {
    return new DimProperty(dimensionValue, Arrays.copyOf(modifiers, modifiers.length));
  }

}
