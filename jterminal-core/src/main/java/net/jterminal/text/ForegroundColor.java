package net.jterminal.text;

import org.jetbrains.annotations.NotNull;

public interface ForegroundColor {

  String getForegroundAnsiCode();

  @NotNull TerminalColor asUniversalColor();

}
