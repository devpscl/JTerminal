package net.jterminal.text.command;

import org.jetbrains.annotations.NotNull;

class CursorCommandImpl implements CursorCommand {

  private final String csi;

  CursorCommandImpl(@NotNull String esc) {
    this.csi = "\u001b" + esc;
  }

  @Override
  public @NotNull String getAnsiCode() {
    return csi;
  }

}
