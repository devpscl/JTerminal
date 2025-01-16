package net.jterminal.ui.component.scrollbar;

import net.jterminal.ui.util.Axis;
import org.jetbrains.annotations.NotNull;

public class DefaultScrollBarStyle implements ScrollBarStyle {

  @Override
  public char getActiveScrollChar(@NotNull Axis axis) {
    return '▓';
  }

  @Override
  public char getInactiveScrollChar(@NotNull Axis axis) {
    return '░';
  }

  @Override
  public char getPrefixScrollChar(@NotNull Axis axis) {
    return axis == Axis.VERTICAL ? '▲' : '◀';
  }

  @Override
  public char getSuffixScrollChar(@NotNull Axis axis) {
    return axis == Axis.VERTICAL ? '▼' : '▶';
  }
}
