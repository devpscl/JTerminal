package net.jterminal.text;

import org.jetbrains.annotations.NotNull;

/**
 * Background color.
 */
public interface BackgroundColor {

  /**
   *
   * @return the background ansi code
   */
  String getBackgroundAnsiCode();

  /**
   * As terminal color.
   *
   * @return the terminal color
   */
  @NotNull TerminalColor asUniversalColor();

}
