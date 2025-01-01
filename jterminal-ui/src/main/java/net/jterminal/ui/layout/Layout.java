package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public interface Layout {

  void addComponent(@NotNull Component component);

  void removeComponent(@NotNull Component component);

  @NotNull TerminalPosition move(@NotNull Component component,
      @NotNull TerminalDimension containerSize, @NotNull TerminalDimension originalContainerSize);

  @NotNull TerminalDimension resize(@NotNull Component component,
      @NotNull TerminalDimension containerSize, @NotNull TerminalDimension originalContainerSize);


}
