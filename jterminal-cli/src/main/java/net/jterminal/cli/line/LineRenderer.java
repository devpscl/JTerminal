package net.jterminal.cli.line;

import java.io.IOException;
import net.jterminal.TerminalBuffer;
import net.jterminal.cli.CLITerminal;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public interface LineRenderer {

  @NotNull LineView print(@NotNull CLITerminal terminal,
      @NotNull LineReader lineReader, @NotNull TerminalBuffer terminalBuffer)
      throws IOException;

  void printLegacy(@NotNull CLITerminal terminal,
      @NotNull LineReader lineReader, @NotNull TerminalBuffer terminalBuffer)
      throws IOException;

  void remove(@NotNull CLITerminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer terminalBuffer, @NotNull LineView lineView)
      throws IOException;

  @NotNull LineView view(@NotNull CLITerminal terminal, @NotNull LineReader lineReader);

  @NotNull TermString legacyView(@NotNull CLITerminal terminal, @NotNull LineReader lineReader);

}
