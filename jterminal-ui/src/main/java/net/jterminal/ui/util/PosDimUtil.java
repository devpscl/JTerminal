package net.jterminal.ui.util;

import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class PosDimUtil {

  public static @NotNull TerminalPosition add(@NotNull TerminalPosition pos1,
      @NotNull TerminalPosition pos2) {
    return pos1.clone().add(pos2).subtract(1);
  }

  public static @NotNull TerminalPosition add(@NotNull TerminalPosition pos1,
      @NotNull TerminalDimension dim1) {
    return pos1.clone().addX(dim1.width()).addY(dim1.height()).subtract(1);
  }

  public static @NotNull TerminalPosition subtract(@NotNull TerminalPosition pos1,
      @NotNull TerminalPosition pos2) {
    return pos1.clone().subtract(pos2).add(1);
  }

}
