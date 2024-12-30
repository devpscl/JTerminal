package net.jterminal.ui.renderer;

import java.io.IOException;
import java.io.OutputStream;
import net.jterminal.Terminal;
import net.jterminal.ui.exception.GraphicsException;
import net.jterminal.ui.graphics.CellBuffer;
import net.jterminal.ui.graphics.CellData;
import org.jetbrains.annotations.NotNull;

public class FastScreenRenderer extends ScreenRenderer {

  private final OutputStream outputStream = Terminal.FD_OUT;
  private final String resetAnsiCode = "\033[0m";
  private final String initAnsiCode = "\033[H";

  public FastScreenRenderer(Terminal terminal) {
    super(terminal);
  }

  @Override
  public void renderSync(@NotNull CellBuffer cellBuffer, @NotNull Terminal terminal) throws GraphicsException {
    @NotNull CellData[][] buffer = cellBuffer.to2dArray();
    StringBuilder builder = new StringBuilder();
    builder.append(initAnsiCode);
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
    byte[] binary = builder.toString().getBytes();
    try {
      outputStream.write(binary);
      outputStream.flush();
    } catch (IOException e) {
      throw new GraphicsException("Cannot write display output", e);
    }
  }


}
