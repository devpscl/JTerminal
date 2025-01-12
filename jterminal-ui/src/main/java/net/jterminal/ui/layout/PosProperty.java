package net.jterminal.ui.layout;

import java.util.Arrays;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.layout.Layout.PositionValue;
import net.jterminal.ui.util.Axis;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class PosProperty {

  private final PositionValue positionValue;
  private final Modifier[] modifiers;

  public PosProperty() {
    this(Layout.origin());
  }

  public PosProperty(@NotNull PositionValue positionValue, Modifier...modifiers) {
    this.positionValue = positionValue;
    this.modifiers = modifiers;
  }

  public @NotNull PositionValue positionValue() {
    return positionValue;
  }

  public Modifier[] modifiers() {
    return modifiers;
  }

  public int calculateX(@NotNull TermDim dimension) {
    int value = positionValue.x(dimension.clone());
    for (Modifier modifier : modifiers) {
      value = modifier.get(dimension.clone(), value, Axis.HORIZONTAL);
    }
    value = Math.max(1, Math.min(value, dimension.width()));
    return value;
  }

  public int calculateY(@NotNull TermDim dimension) {
    int value = positionValue.y(dimension.clone());
    for (Modifier modifier : modifiers) {
      value = modifier.get(dimension.clone(), value, Axis.VERTICAL);
    }
    value = Math.max(1, Math.min(value, dimension.height()));
    return value;
  }

  public @NotNull PosProperty copy() {
    return new PosProperty(positionValue, Arrays.copyOf(modifiers, modifiers.length));
  }

}
