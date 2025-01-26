package net.jterminal.ui.component;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.graphics.CellBuffer;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class ComponentGraphics {

  public static @NotNull TermGraphics createGraphics(@NotNull TermDim size,
      @NotNull Component component) {
    ForegroundColor foregroundColor = component.foregroundColor();
    BackgroundColor backgroundColor = component.backgroundColor();
    CellData cellData = CellData.empty(foregroundColor, backgroundColor);
    CellBuffer buffer = new CellBuffer(size, cellData);
    TermGraphics graphics = TermGraphics.from(buffer,
        TextStyle.create(foregroundColor, backgroundColor));
    graphics.style(TextStyle.create(foregroundColor, backgroundColor));
    return graphics;
  }

  public static void prepare(@NotNull Component component) {
    component.recalculateProperties();
  }

  public static void draw(@NotNull TermGraphics graphics, @NotNull Component component) {
    synchronized (component.lock) {
      ForegroundColor foregroundColor = component.foregroundColor();
      BackgroundColor backgroundColor = component.backgroundColor();
      CellData cellData = CellData.empty(foregroundColor, backgroundColor);
      TermGraphics innerGraphics = createGraphics(component.currentDimension(), component);
      component.paint(innerGraphics);
      graphics.draw(component.currentPosition(), innerGraphics);
    }
  }

}
