package net.jterminal.text;

import java.awt.Color;
import net.jterminal.TerminalBuffer;

class TerminalColorImpl implements TerminalColor {

  private final String foregroundColor;
  private final String backgroundColor;

  private final Color color;

  public TerminalColorImpl() {
    this.foregroundColor = "\u001b[39m";
    this.backgroundColor = "\u001b[49m";
    this.color = Color.BLACK;
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
}
