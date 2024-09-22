package net.jterminal.text.command;

import org.jetbrains.annotations.NotNull;

public interface AnsiCommand {

  @NotNull String getAnsiCode();

}
