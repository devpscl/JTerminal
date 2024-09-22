package net.jterminal.text.command;

import org.jetbrains.annotations.NotNull;

class ScreenCommandImpl implements ScreenCommand {

  private final String csi;

  ScreenCommandImpl(@NotNull String esc) {
    this.csi = "\u001b" + esc;
  }

  @Override
  public @NotNull String getAnsiCode() {
    return csi;
  }

}
