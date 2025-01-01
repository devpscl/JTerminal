package net.jterminal.text;

import org.jetbrains.annotations.NotNull;

public interface BackgroundColor {

  String getBackgroundAnsiCode();

  @NotNull TerminalColor asUniversalColor();

}
