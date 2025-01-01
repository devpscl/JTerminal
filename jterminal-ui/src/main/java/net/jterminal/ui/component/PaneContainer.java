package net.jterminal.ui.component;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TerminalDimension;
import org.jetbrains.annotations.NotNull;

public class PaneContainer extends Container implements Resizeable {

  private TerminalDimension minimumSize = new TerminalDimension(1, 1);
  private TerminalDimension maximumSize = new TerminalDimension(Integer.MAX_VALUE,
      Integer.MAX_VALUE);

  @Override
  public void paint(@NotNull TermGraphics graphics, @NotNull Component component) {
    ComponentGraphics.prepare(component);
    ComponentGraphics.draw(graphics, component);
  }

  @Override
  public @NotNull TerminalDimension minimumSize() {
    return minimumSize;
  }

  @Override
  public @NotNull TerminalDimension maximumSize() {
    return maximumSize;
  }

  public void minimumSize(@NotNull TerminalDimension minimumSize) {
    this.minimumSize = minimumSize;
  }

  public void maximumSize(@NotNull TerminalDimension maximumSize) {
    this.maximumSize = maximumSize;
  }
}
