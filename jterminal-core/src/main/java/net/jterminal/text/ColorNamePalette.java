package net.jterminal.text;

import java.awt.Color;
import org.jetbrains.annotations.NotNull;

public enum ColorNamePalette implements TerminalColor {

  BLACK(XtermColor.COLOR_0),
  DARK_RED(XtermColor.COLOR_1),
  DARK_GREEN(XtermColor.COLOR_2),
  DARK_YELLOW(XtermColor.COLOR_3),
  DARK_BLUE(XtermColor.COLOR_4),
  DARK_PURPLE(XtermColor.COLOR_5),
  DARK_AQUA(XtermColor.COLOR_6),
  GRAY(XtermColor.COLOR_7),
  DARK_GRAY(XtermColor.COLOR_8),
  RED(XtermColor.COLOR_9),
  GREEN(XtermColor.COLOR_10),
  YELLOW(XtermColor.COLOR_11),
  BLUE(XtermColor.COLOR_12),
  PURPLE(XtermColor.COLOR_13),
  AQUA(XtermColor.COLOR_14),
  WHITE(XtermColor.COLOR_15),
  HOT_PINK(XtermColor.COLOR_206),
  PINK(XtermColor.COLOR_218),
  DARK_ORANGE(XtermColor.COLOR_208),
  GOLD(XtermColor.COLOR_178),
  GREEN_YELLOW(XtermColor.COLOR_154),
  ROSY_BROWN(XtermColor.COLOR_138),
  MEDIUM_SPRING_GREEN(XtermColor.COLOR_49),
  MEDIUM_PURPLE(XtermColor.COLOR_141),
  BLUE_VIOLET(XtermColor.COLOR_57),
  CADET_BLUE(XtermColor.COLOR_72),
  MEDIUM_TURQUOISE(XtermColor.COLOR_80),
  AQUAMARINE(XtermColor.COLOR_86),
  LIGHT_CYAN(XtermColor.COLOR_152),
  DARK_CYAN(XtermColor.COLOR_36),
  TAN(XtermColor.COLOR_180),
  LIGHT_CORAL(XtermColor.COLOR_210),
  TURQUOISE(XtermColor.COLOR_45),
  BROWN(XtermColor.COLOR_94),
  ORANGE(XtermColor.COLOR_214),
  INDIAN_RED(XtermColor.COLOR_167),
  ORCHID(XtermColor.COLOR_170);

  private final XtermColor color;

  ColorNamePalette(@NotNull XtermColor color) {
    this.color = color;
  }

  @Override
  public String getBackgroundAnsiCode() {
    return color.getBackgroundAnsiCode();
  }

  @Override
  public String getForegroundAnsiCode() {
    return color.getForegroundAnsiCode();
  }

  @Override
  public Color toColor() {
    return color.toColor();
  }

  @Override
  public boolean isDefault() {
    return false;
  }

  @Override
  public @NotNull XtermColor asXtermColor() {
    return color;
  }

}
