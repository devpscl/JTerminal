package net.jterminal.cli.line;

import net.jterminal.TerminalBuffer;
import net.jterminal.cli.CLITerminal;
import net.jterminal.text.command.CursorCommand;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class DefaultLineRenderer implements LineRenderer {

  @Override
  public @NotNull LineView print(@NotNull CLITerminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer buffer, boolean cursor) {
    LineView lineView = view(terminal, lineReader);
    TermString termString = lineView.view();
    TermPos cursorPos = lineView.cursorPos(new TermPos(0, 0));
    buffer.command(CursorCommand.hide())
        .resetStyle()
        .append("\r")
        .cursorSave()
        .append(termString);
    if(cursor) {
      buffer.cursorRestore()
          .cursorDown(cursorPos.y())
          .cursorRight(cursorPos.x());
    }
    buffer.command(CursorCommand.show());
    return lineView;
  }

  @Override
  public void remove(@NotNull CLITerminal terminal, @NotNull LineReader lineReader,
      @NotNull TerminalBuffer buffer, @NotNull LineView lineView) {
    int lines = lineView.usedLines();
    TermPos cursorPos = lineView.cursorPos(new TermPos(0, 0));
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
  public @NotNull LineView view(@NotNull CLITerminal terminal, @NotNull LineReader lineReader) {
    TermDim winSize = terminal.windowSize();
    TermString termString = TermString.value(lineReader.displayingInput());
    termString = terminal.modifyCommandLineView(termString, lineReader.displayingInput());
    int cursor = (lineReader.flags() & LineReader.FLAG_ECHO_MODE) != 0
        ? lineReader.cursor() : 0;
    return LineView.create(termString, cursor, winSize, termString.length());
  }

}
