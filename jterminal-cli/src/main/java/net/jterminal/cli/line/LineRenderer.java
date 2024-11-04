package net.jterminal.cli.line;

import java.io.IOException;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;

public interface LineRenderer {

  @NotNull LineView print(@NotNull Terminal terminal,
      @NotNull LineReader lineReader, @NotNull TerminalBuffer terminalBuffer)
      throws IOException;

  void printLegacy(@NotNull Terminal terminal,
      @NotNull LineReader lineReader, @NotNull TerminalBuffer terminalBuffer)
      throws IOException;

  void remove(@NotNull Terminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer terminalBuffer, @NotNull LineView lineView)
      throws IOException;

  @NotNull LineView view(@NotNull Terminal terminal, @NotNull LineReader lineReader);

  @NotNull TermString legacyView(@NotNull Terminal terminal, @NotNull LineReader lineReader);

}
