package net.jterminal.text.style;

import org.jetbrains.annotations.NotNull;

/**
 * The enum Text font.
 */
public enum TextFont {

  /**
   * Bold text font.
   */
  BOLD("1m", "22m"),
  /**
   * Underline text font.
   */
  UNDERLINE("4m", "24m"),
  /**
   * Reversed text font.
   */
  REVERSED("7m", "27m"),
  /**
   * Strike text font.
   */
  STRIKE("9m", "29m"),
  /**
   * Blinking text font.
   * Rarely supported.
   */
  BLINKING("5m", "25m"),
  /**
   * Italic text font.
   * Rarely supported
   */
  ITALIC("3m", "23m");

  private final String ansiCode;
  private final String resetAnsiCode;

  TextFont(String id, String resetAnsiCode) {
    this.ansiCode = "\u001b[" + id;
    this.resetAnsiCode = "\u001b[" + resetAnsiCode;
  }

  /**
   * Gets ansi code.
   *
   * @return the ansi code
   */
  public @NotNull String getAnsiCode() {
    return ansiCode;
  }

  /**
   * Gets reset ansi code. The color attributes are retained.
   *
   * @return the reset ansi code
   */
  public @NotNull String getResetAnsiCode() {
    return resetAnsiCode;
  }

  /**
   * Gets ansicode to reset all fonts to default.
   *
   * @return the default font ansicode
   */
  public static @NotNull String getDefaultFont() {
    StringBuilder stringBuilder = new StringBuilder();
    for (TextFont value : values()) {
      stringBuilder.append(value.getResetAnsiCode());
    }
    return stringBuilder.toString();
  }
}
