package net.jterminal.ui.renderer;

import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.Terminal;
import net.jterminal.ui.UITerminal;
import net.jterminal.ui.exception.GraphicsException;
import net.jterminal.ui.graphics.CellBuffer;
import org.jetbrains.annotations.NotNull;

public abstract class ScreenRenderer  {

  private final ReentrantLock lock = new ReentrantLock();
  protected final Terminal terminal;

  public ScreenRenderer(Terminal terminal) {
    this.terminal = terminal;
  }

  public void render(@NotNull CellBuffer cellBuffer) {
    lock.lock();
    try {
      renderSync(cellBuffer, terminal);
    } catch (GraphicsException e) {
      UITerminal.LOGGER.error("Failed to draw screen", e);
    } finally {
      lock.unlock();
    }
  }

  public abstract void renderSync(@NotNull CellBuffer cellBuffer, @NotNull Terminal terminal)
      throws GraphicsException;

}
