package net.jterminal.ui.component.tool;

import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class FullContainerViewArea implements ContainerViewArea {

  @Override
  public @NotNull TermPos origin(@NotNull TermDim containerSize) {
    return new TermPos(1, 1);
  }

  @Override
  public @NotNull TermDim dimension(@NotNull TermDim containerSize) {
    return containerSize;
  }
}
