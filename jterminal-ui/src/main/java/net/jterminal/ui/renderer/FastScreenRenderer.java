package net.jterminal.ui.renderer;

import java.io.IOException;
import java.io.OutputStream;
import net.jterminal.Terminal;
import net.jterminal.TerminalBuffer;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.command.CursorCommand;
import net.jterminal.ui.exception.GraphicsException;
import net.jterminal.ui.graphics.CellBuffer;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.graphics.TerminalState.CursorType;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class FastScreenRenderer extends ScreenRenderer {

  private final OutputStream outputStream = Terminal.FD_OUT;
  private final String resetAnsiCode = "\033[0m";
  private final String initAnsiCode = "\033[H";

  public FastScreenRenderer(Terminal terminal) {
    super(terminal);
  }

  @Override
  public void renderSync(@NotNull CellBuffer cellBuffer,
      @NotNull Terminal terminal, @NotNull TerminalState terminalState) throws GraphicsException {

    TerminalBuffer initBuffer = new TerminalBuffer()
        .command(CursorCommand.home())
        .command(CursorCommand.hide());
    TerminalBuffer releaseBuffer = new TerminalBuffer();
    TermPos cursorPosition = terminalState.cursorPosition();
    if(cursorPosition != null) {
      CursorType cursorType = terminalState.cursorType();
      if(cursorType == CursorType.STATIC || cursorType == CursorType.BLINKING) {
        releaseBuffer.command(CursorCommand.move(cursorPosition))
            .command(CursorCommand.show())
            .command(CursorCommand.blinking(cursorType == CursorType.BLINKING));
      } else {
        int bufx = cursorPosition.x() - 1;
        int bufy = cursorPosition.y() - 1;
        CellData cellData = cellBuffer.read(bufx, bufy);
        if(cellData != null) {
          CellData newCellData = CellData.builder()
              .symbol(cellData.symbol())
              .fonts(cellData.fonts())
              .foregroundColor(TerminalColor.from(cellData.backgroundColor()))
              .backgroundColor(TerminalColor.from(cellData.foregroundColor()))
              .build();
          cellBuffer.write(bufx, bufy, newCellData);
        }
      }
    }
    @NotNull CellData[][] buffer = cellBuffer.to2dArray();
    StringBuilder builder = new StringBuilder();
    builder.append(initBuffer.toString());
    boolean firstRow = true;
    for (CellData[] row : buffer) {
      if(!firstRow) {
        builder.append('\n');
      }
      for (CellData data : row) {
        builder.append(resetAnsiCode)
            .append(data.ansiSequence())
            .append(data.symbol());
      }
      firstRow = false;
    }
    builder.append(releaseBuffer);
    byte[] binary = builder.toString().getBytes();
    try {
      outputStream.write(binary);
      outputStream.flush();
    } catch (IOException e) {
      throw new GraphicsException("Cannot write display output", e);
    }
  }

}
