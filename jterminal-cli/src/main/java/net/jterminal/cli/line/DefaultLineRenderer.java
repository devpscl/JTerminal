package net.jterminal.cli.line;

import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.text.command.CursorCommand;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class DefaultLineRenderer implements LineRenderer {

  @Override
  public @NotNull LineView print(@NotNull Terminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer buffer) {
    TerminalDimension winSize = terminal.windowSize();
    TermString termString = view(terminal, lineReader);
    LineView lineView = LineView.create(termString, lineReader.cursor(), winSize);
    TerminalPosition cursorPos = lineView.cursorPos(new TerminalPosition(0, 0));
    buffer.command(CursorCommand.hide())
        .resetStyle()
        .append("\r")
        .cursorSave()
        .append(termString)
        .cursorRestore()
        .cursorDown(cursorPos.y())
        .cursorRight(cursorPos.x())
        .command(CursorCommand.show());
    return lineView;
  }

  @Override
  public void printLegacy(@NotNull Terminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer buffer) {
    TermString termString = legacyView(terminal, lineReader);
    buffer.command(CursorCommand.hide())
        .resetStyle()
        .append("\r")
        .append(termString)
        .append("\n")
        .command(CursorCommand.show());
  }

  @Override
  public void remove(@NotNull Terminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer buffer, @NotNull LineView lineView) {
    int lines = lineView.usedLines();
    TerminalPosition cursorPos = lineView.cursorPos(new TerminalPosition(0, 0));
    buffer.cursorUp(cursorPos.y());
    buffer.cursorSave();
    for (int i = 0; i < lines; i++) {
      buffer.screenClearLine()
          .cursorDown(1);
    }
    buffer.cursorRestore()
            .append("\r");
  }

  @Override
  public @NotNull TermString view(@NotNull Terminal terminal, @NotNull LineReader lineReader) {
    return TermString.value(lineReader.displayingInput());
  }

}
