package net.jterminal.text;

import java.awt.Color;

class TerminalColorImpl implements TerminalColor {

  private final String foregroundColor;
  private final String backgroundColor;

  private final Color color;
  private final boolean defaultColor;

  public TerminalColorImpl() {
    this.foregroundColor = "\u001b[39m";
    this.backgroundColor = "\u001b[49m";
    this.color = Color.BLACK;
    defaultColor = true;
  }

  public TerminalColorImpl(int red, int green, int blue) {
    this.color = new Color(red, green, blue);
    foregroundColor = "\u001b[38;2;"
        + red + ";"
        + green + ";"
        + blue + "m";
    backgroundColor = "\u001b[48;2;"
        + red + ";"
        + green + ";"
        + blue + "m";
    defaultColor = false;
  }

  public TerminalColorImpl(Color color) {
    this.color = color;
    int red = color.getRed();
    int green = color.getGreen();
    int blue = color.getBlue();
    foregroundColor = "\u001b[38;2;"
        + red + ";"
        + green + ";"
        + blue + "m";
    backgroundColor = "\u001b[48;2;"
        + red + ";"
        + green + ";"
        + blue + "m";
    defaultColor = false;
  }

  @Override
  public String getForegroundAnsiCode() {
    return foregroundColor;
  }

  @Override
  public String getBackgroundAnsiCode() {
    return backgroundColor;
  }

  @Override
  public Color toColor() {
    return color;
  }

  @Override
  public boolean isDefault() {
    return defaultColor;
  }

  @Override
  public String toString() {
    if(defaultColor) {
      return "DEFAULT";
    }
    return color.toString();
  }
}
