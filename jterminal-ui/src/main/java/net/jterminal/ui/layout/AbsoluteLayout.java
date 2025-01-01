package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class AbsoluteLayout implements Layout {


  @Override
  public void addComponent(@NotNull Component component) {
  }

  @Override
  public void removeComponent(@NotNull Component component) {
  }

  @Override
  public @NotNull TerminalPosition move(@NotNull Component component,
      @NotNull TerminalDimension containerSize, @NotNull TerminalDimension originalSize) {
    return component.position();
  }

  @Override
  public @NotNull TerminalDimension resize(@NotNull Component component,
      @NotNull TerminalDimension containerSize, @NotNull TerminalDimension originalSize) {
    return component.size();
  }
}
