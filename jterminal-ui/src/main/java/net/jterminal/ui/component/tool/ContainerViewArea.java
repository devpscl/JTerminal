package net.jterminal.ui.component.tool;

import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public interface ContainerViewArea {

  @NotNull TermPos origin(@NotNull TermDim containerSize);

  @NotNull TermDim dimension(@NotNull TermDim containerSize);

}
