package net.jterminal.text;

import java.awt.Color;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Terminal color.
 */
public interface TerminalColor extends ForegroundColor, BackgroundColor {

  /**
   * The constant BLACK.
   */
  TerminalColor BLACK = XtermColor.COLOR_0;
  /**
   * The constant DARK_RED.
   */
  TerminalColor DARK_RED = XtermColor.COLOR_1;
  /**
   * The constant DARK_GREEN.
   */
  TerminalColor DARK_GREEN = XtermColor.COLOR_2;
  /**
   * The constant DARK_YELLOW.
   */
  TerminalColor DARK_YELLOW = XtermColor.COLOR_3;
  /**
   * The constant DARK_BLUE.
   */
  TerminalColor DARK_BLUE = XtermColor.COLOR_4;
  /**
   * The constant DARK_PURPLE.
   */
  TerminalColor DARK_PURPLE = XtermColor.COLOR_5;
  /**
   * The constant DARK_AQUA.
   */
  TerminalColor DARK_AQUA = XtermColor.COLOR_6;
  /**
   * The constant GRAY.
   */
  TerminalColor GRAY = XtermColor.COLOR_7;
  /**
   * The constant DARK_GRAY.
   */
  TerminalColor DARK_GRAY = XtermColor.COLOR_8;
  /**
   * The constant RED.
   */
  TerminalColor RED = XtermColor.COLOR_9;
  /**
   * The constant GREEN.
   */
  TerminalColor GREEN = XtermColor.COLOR_10;
  /**
   * The constant YELLOW.
   */
  TerminalColor YELLOW = XtermColor.COLOR_11;
  /**
   * The constant BLUE.
   */
  TerminalColor BLUE = XtermColor.COLOR_12;
  /**
   * The constant PURPLE.
   */
  TerminalColor PURPLE = XtermColor.COLOR_13;
  /**
   * The constant AQUA.
   */
  TerminalColor AQUA = XtermColor.COLOR_14;
  /**
   * The constant WHITE.
   */
  TerminalColor WHITE = XtermColor.COLOR_15;
  /**
   * The constant DEFAULT.
   */
  TerminalColor DEFAULT = new TerminalColorImpl();

  /**
   * Convert color to awt color.
   *
   * @return the awt color
   */
  Color toColor();

  /**
   * Is default color.
   *
   * @return the boolean
   */
  boolean isDefault();

  /**
   * Converts color to a 256bit color.
   *
   * @return the xterm color
   */
  default @NotNull XtermColor asXtermColor() {
    return XtermColor.getNearestTo(toColor());
  }

  /**
   * From rgb color.
   *
   * @param red   the red
   * @param green the green
   * @param blue  the blue
   * @return the terminal color
   */
  static @NotNull TerminalColor from(int red, int green, int blue) {
    return new TerminalColorImpl(red, green, blue);
  }

  /**
   * From awt color.
   *
   * @param color the awt color
   * @return the terminal color
   */
  static @NotNull TerminalColor from(Color color) {
    return new TerminalColorImpl(color);
  }

  /**
   * From rgb color.
   *
   * @param rgb the rgb
   * @return the terminal color
   */
  static @NotNull TerminalColor from(int rgb) {
    return new TerminalColorImpl(new Color(rgb));
  }

  /**
   * From terminal color.
   *
   * @param hexCode the hex code
   * @return the terminal color
   */
  static @NotNull TerminalColor from(String hexCode) {
    return new TerminalColorImpl(Color.decode(hexCode));
  }

  @Contract("null -> null; !null -> !null")
  static @Nullable TerminalColor from(@Nullable ForegroundColor foregroundColor) {
    if(foregroundColor == null) {
      return null;
    }
    return foregroundColor.asUniversalColor();
  }

  @Contract("null -> null; !null -> !null")
  static @Nullable TerminalColor from(@Nullable BackgroundColor backgroundColor) {
    if(backgroundColor == null) {
      return null;
    }
    return backgroundColor.asUniversalColor();
  }

}
