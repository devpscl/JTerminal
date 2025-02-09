package net.jterminal.test.example;

import java.awt.Color;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.XtermColor;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class Color256Screen extends TermScreen {

  private final ExampleApp instance;
  private ButtonComponent buttonComponent;

  public Color256Screen(ExampleApp instance) {
    this.instance = instance;
  }

  public void create() {
    setMouseInputEnabled(true);
    buttonComponent = new ButtonComponent("Back to home");
    buttonComponent.action(instance::openHomeScreen);
    add(buttonComponent);
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    super.paint(graphics);
    TermGraphics innerGraphics = TermGraphics.create(graphics.width() - 2,
        graphics.height() - 2);
    paintColorMap(innerGraphics);
    graphics.draw(2, 2, innerGraphics);
  }

  private void paintColorMap(@NotNull TermGraphics graphics) {
    int width = graphics.width();
    int height = graphics.height();
    XtermColor[] arr = XtermColor.values();
    float[] hsb = new float[3];
    final int len = 6;
    int cols = width/len;
    for (int idx = 0; idx < arr.length; idx++) {
      XtermColor color = arr[idx];
      Color javaColor = color.toColor();
      int y = Math.min(height, idx / cols) + 1;
      int x = Math.min(width, (idx % cols) * len) + 1;
      graphics.backgroundColor(color);
      graphics.drawRect(x, y, len, 1, CellData.EMPTY_SYMBOL);
      Color.RGBtoHSB(javaColor.getRed(), javaColor.getGreen(), javaColor.getBlue(), hsb);
      float brightness = hsb[2];
      if(brightness > 0.5) {
        graphics.foregroundColor(TerminalColor.BLACK);
      } else {
        graphics.foregroundColor(TerminalColor.WHITE);
      }
      graphics.drawString(x, y, String.valueOf(idx));
    }
  }
}
