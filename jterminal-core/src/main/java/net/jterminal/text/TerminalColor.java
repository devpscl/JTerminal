package net.jterminal.text;

import java.awt.Color;
import org.jetbrains.annotations.NotNull;

public interface TerminalColor extends ForegroundColor, BackgroundColor {

  TerminalColor BLACK = XtermColor.COLOR_0;
  TerminalColor DARK_RED = XtermColor.COLOR_1;
  TerminalColor DARK_GREEN = XtermColor.COLOR_2;
  TerminalColor DARK_YELLOW = XtermColor.COLOR_3;
  TerminalColor DARK_BLUE = XtermColor.COLOR_4;
  TerminalColor DARK_PURPLE = XtermColor.COLOR_5;
  TerminalColor DARK_AQUA = XtermColor.COLOR_6;
  TerminalColor GRAY = XtermColor.COLOR_7;
  TerminalColor DARK_GRAY = XtermColor.COLOR_8;
  TerminalColor RED = XtermColor.COLOR_9;
  TerminalColor GREEN = XtermColor.COLOR_10;
  TerminalColor YELLOW = XtermColor.COLOR_11;
  TerminalColor BLUE = XtermColor.COLOR_12;
  TerminalColor PURPLE = XtermColor.COLOR_13;
  TerminalColor AQUA = XtermColor.COLOR_14;
  TerminalColor WHITE = XtermColor.COLOR_15;
  TerminalColor DEFAULT = new TerminalColorImpl();

  Color toColor();

  default @NotNull XtermColor asXtermColor() {
    return XtermColor.getNearestTo(toColor());
  }

  static @NotNull TerminalColor from(int red, int green, int blue) {
    return new TerminalColorImpl(red, green, blue);
  }

  static @NotNull TerminalColor from(Color color) {
    return new TerminalColorImpl(color);
  }

  static @NotNull TerminalColor from(int rgb) {
    return new TerminalColorImpl(new Color(rgb));
  }

  static @NotNull TerminalColor from(String hexCode) {
    return new TerminalColorImpl(Color.decode(hexCode));
  }

}
