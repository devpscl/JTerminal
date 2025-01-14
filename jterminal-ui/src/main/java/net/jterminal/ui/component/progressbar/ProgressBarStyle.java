package net.jterminal.ui.component.progressbar;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public interface ProgressBarStyle {

  void paint(@NotNull TermGraphics graphics, int width, float progress);

}
