package net.jterminal.ui.component.progressbar;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class LegacyProgressBarStyle implements ProgressBarStyle {

  @Override
  public void paint(@NotNull TermGraphics graphics, int width, float progress) {
    int progressWidth = Math.max(0,  width - 2);
    graphics.draw(1, 1, '[');
    int splitWidth = Math.round(progressWidth * progress);
    for (int w = 0; w < progressWidth; w++) {
      if(w >= splitWidth) {
        graphics.draw(w + 2, 1, ' ');
        continue;
      }
      graphics.draw(w + 2, 1, '=');
    }
    graphics.draw(progressWidth + 2, 1, ']');
  }
}
