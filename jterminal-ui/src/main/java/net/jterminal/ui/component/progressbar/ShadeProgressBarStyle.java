package net.jterminal.ui.component.progressbar;

import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class ShadeProgressBarStyle implements ProgressBarStyle {

  @Override
  public void paint(@NotNull TermGraphics graphics, int width, float progress) {
    int splitWidth = Math.round(width * progress);
    for (int w = 0; w < width; w++) {
      if(w >= splitWidth) {
        graphics.draw(w + 1, 1, '▒');
        continue;
      }
      graphics.draw(w + 1, 1, '█');
    }
  }
}
