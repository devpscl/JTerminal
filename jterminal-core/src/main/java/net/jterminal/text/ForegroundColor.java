package net.jterminal.text;

import org.jetbrains.annotations.NotNull;

/**
 * Foreground color.
 */
public interface ForegroundColor {

  /**
   *
   * @return the foreground ansi code
   */
  String getForegroundAnsiCode();

  /**
   * As terminal color.
   *
   * @return the terminal color
   */
  @NotNull TerminalColor asUniversalColor();

}
