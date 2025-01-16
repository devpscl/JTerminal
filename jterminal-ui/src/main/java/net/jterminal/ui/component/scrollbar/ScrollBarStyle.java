package net.jterminal.ui.component.scrollbar;

import net.jterminal.ui.util.Axis;
import org.jetbrains.annotations.NotNull;

public interface ScrollBarStyle {

  char getActiveScrollChar(@NotNull Axis axis);

  char getInactiveScrollChar(@NotNull Axis axis);

  char getPrefixScrollChar(@NotNull Axis axis);

  char getSuffixScrollChar(@NotNull Axis axis);

}
