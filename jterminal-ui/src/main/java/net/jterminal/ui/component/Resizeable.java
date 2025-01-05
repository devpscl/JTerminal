package net.jterminal.ui.component;

import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public interface Resizeable {

  default void size(@NotNull TermDim size) {
    size.securePositive();
    if(this instanceof Component c) {
      c.size = size;
      c.repaint();
    }
  }

  @NotNull TermDim minimumSize();

  @NotNull TermDim maximumSize();

}
