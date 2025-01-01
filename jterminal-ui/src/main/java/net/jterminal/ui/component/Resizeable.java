package net.jterminal.ui.component;

import net.jterminal.util.TerminalDimension;
import org.jetbrains.annotations.NotNull;

public interface Resizeable {

  default void size(@NotNull TerminalDimension size) {
    size.securePositive();
    if(this instanceof Component c) {
      c.size = size;
      c.repaint();
    }
  }

}
