package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface Layout {

  void addComponent(@NotNull Component component);

  void removeComponent(@NotNull Component component);

  @NotNull TermPos move(@NotNull Component component,
      @NotNull TermDim containerSize, @NotNull TermDim originalContainerSize);

  @NotNull TermDim resize(@NotNull Component component,
      @NotNull TermDim containerSize, @NotNull TermDim originalContainerSize);


}
