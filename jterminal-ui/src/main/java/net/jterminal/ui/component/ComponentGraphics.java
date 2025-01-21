package net.jterminal.ui.component;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextStyle;
import net.jterminal.ui.graphics.CellBuffer;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import org.jetbrains.annotations.NotNull;

public class ComponentGraphics {

  public static void prepare(@NotNull Component component) {
    component.recalculateProperties();
  }

  public static void draw(@NotNull TermGraphics graphics, @NotNull Component component) {
    ForegroundColor foregroundColor = component.foregroundColor();
    BackgroundColor backgroundColor = component.backgroundColor();
    CellData cellData = CellData.empty(foregroundColor, backgroundColor);
    CellBuffer buffer = new CellBuffer(component.currentDimension(), cellData);
    TermGraphics innerGraphics = TermGraphics.from(buffer,
        TextStyle.create(foregroundColor, backgroundColor));
    innerGraphics.foregroundColor(foregroundColor);
    innerGraphics.backgroundColor(backgroundColor);
    component.paint(innerGraphics);
    graphics.draw(component.currentPosition(), innerGraphics);
  }

}
