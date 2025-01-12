package net.jterminal.ui.component;

import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;

public interface Resizeable {

  @NotNull Component asComponent();

  default void width(int value, Layout.Modifier...modifiers) {
    asComponent().setWidth(value, modifiers);
  }

  default void width(Layout.DimensionValue positionValue, Layout.Modifier...modifiers) {
    asComponent().setWidth(positionValue, modifiers);
  }

  default void height(int value, Layout.Modifier...modifiers) {
    asComponent().setHeight(value, modifiers);
  }

  default void height(Layout.DimensionValue positionValue, Layout.Modifier...modifiers) {
    asComponent().setHeight(positionValue, modifiers);
  }

}
