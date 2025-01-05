package net.jterminal.ui.layout;

import net.jterminal.ui.component.Component;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class AbsoluteLayout implements Layout {


  @Override
  public void addComponent(@NotNull Component component) {
  }

  @Override
  public void removeComponent(@NotNull Component component) {
  }

  @Override
  public @NotNull TermPos move(@NotNull Component component,
      @NotNull TermDim containerSize, @NotNull TermDim originalSize) {
    return component.position();
  }

  @Override
  public @NotNull TermDim resize(@NotNull Component component,
      @NotNull TermDim containerSize, @NotNull TermDim originalSize) {
    return component.size();
  }
}
