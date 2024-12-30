package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.shape.TermShape;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TermGraphics {

  @NotNull CellBuffer buffer();

  int width();

  int height();

  @NotNull TerminalDimension size();

  @NotNull TermGraphics style(@Nullable TextStyle textStyle);

  @NotNull TermGraphics resetStyle();

  @NotNull TermGraphics foregroundColor(@Nullable ForegroundColor foregroundColor);

  @NotNull TermGraphics backgroundColor(@Nullable BackgroundColor backgroundColor);

  @NotNull TermGraphics fonts(@NotNull TextFont...fonts);

  @Nullable ForegroundColor foregroundColor();

  @Nullable BackgroundColor backgroundColor();

  @NotNull TextFont[] fonts();

  @NotNull TermGraphics drawEmpty(@NotNull TerminalPosition pos);

  @NotNull TermGraphics draw(@NotNull TerminalPosition pos, char symbol);

  @NotNull TermGraphics drawEmpty(int x, int y);

  @NotNull TermGraphics draw(int x, int y, char symbol);

  @NotNull TermGraphics draw(@NotNull TerminalPosition pos, @NotNull TermGraphics termGraphics);

  @NotNull TermGraphics draw(@NotNull TerminalPosition pos, @NotNull TerminalDimension dim,
      @NotNull TermGraphics termGraphics);

  @NotNull TermGraphics draw(int x, int y, @NotNull TermGraphics graphics);

  @NotNull TermGraphics draw(int x, int y, int width, int height, @NotNull TermGraphics graphics);

  @NotNull TermGraphics drawLine(@NotNull TerminalPosition from,
      @NotNull TerminalPosition to, char symbol);

  @NotNull TermGraphics drawLine(int x1, int y1, int x2, int y2, char symbol);

  @NotNull TermGraphics drawRect(@NotNull TerminalPosition position,
      @NotNull TerminalDimension dimension, char symbol);

  @NotNull TermGraphics drawRect(int x, int y, int width,
      int height, char symbol);

  @NotNull TermGraphics fillRect(@NotNull TerminalPosition position,
      @NotNull TerminalDimension dimension, char symbol);

  @NotNull TermGraphics fillRect(int x, int y, int width, int height, char symbol);

  @NotNull TermGraphics drawString(@NotNull TerminalPosition position,
      @NotNull TermString termString);

  @NotNull TermGraphics drawString(@NotNull TerminalPosition position,
      @NotNull String value);

  @NotNull TermGraphics drawString(int x, int y, @NotNull TermString termString);

  @NotNull TermGraphics drawString(int x, int y, @NotNull String value);

  @NotNull TermGraphics drawShape(@NotNull TerminalPosition pos, @NotNull TermShape shape);

  @NotNull TermGraphics drawShape(int x, int y, @NotNull TermShape shape);

  static @NotNull TermGraphics create(@NotNull TerminalDimension dim) {
    return create(dim.width(), dim.height());
  }

  static @NotNull TermGraphics create(int width, int height) {
    CellBuffer buffer = new CellBuffer(width, height);
    return new TermGraphicsImpl(buffer, -1, -1, width, height);
  }

  static @NotNull TermGraphics from(@NotNull CellBuffer buffer) {
    return new TermGraphicsImpl(buffer, -1, -1,
        buffer.width(), buffer.height());
  }

}
