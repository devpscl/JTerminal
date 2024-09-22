package net.jterminal.text.style;

import org.jetbrains.annotations.NotNull;

public enum TextFont {

  BOLD("1m", "22m"),
  UNDERLINE("4m", "24m"),
  REVERSED("7m", "27m"),
  STRIKE("9m", "29m"),
  /**Rarely supported*/
  BLINKING("5m", "25m"),
  /**Rarely supported*/
  ITALIC("3m", "23m");

  private final String ansiCode;
  private final String resetAnsiCode;

  TextFont(String id, String resetAnsiCode) {
    this.ansiCode = "\u001b[" + id;
    this.resetAnsiCode = "\u001b[" + resetAnsiCode;
  }

  public @NotNull String getAnsiCode() {
    return ansiCode;
  }

  public @NotNull String getResetAnsiCode() {
    return resetAnsiCode;
  }

  public static @NotNull String getDefaultFont() {
    StringBuilder stringBuilder = new StringBuilder();
    for (TextFont value : values()) {
      stringBuilder.append(value.getResetAnsiCode());
    }
    return stringBuilder.toString();
  }
}
