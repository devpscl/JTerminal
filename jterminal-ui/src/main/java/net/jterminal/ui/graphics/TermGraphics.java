package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.shape.TermShape;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TermGraphics {

  @NotNull CellBuffer buffer();

  int width();

  int height();

  @NotNull TermDim size();

  @NotNull TermGraphics style(@Nullable TextStyle textStyle);

  @NotNull TextStyle style();

  @NotNull TextStyle initStyle();

  @NotNull TermGraphics resetStyle();

  @NotNull TermGraphics foregroundColor(@Nullable ForegroundColor foregroundColor);

  @NotNull TermGraphics backgroundColor(@Nullable BackgroundColor backgroundColor);

  @NotNull TermGraphics fonts(@NotNull TextFont...fonts);

  @Nullable ForegroundColor foregroundColor();

  @Nullable BackgroundColor backgroundColor();

  @NotNull TextFont[] fonts();

  @NotNull TermGraphics drawEmpty(@NotNull TermPos pos);

  @NotNull TermGraphics draw(@NotNull TermPos pos, char symbol);

  @NotNull TermGraphics drawEmpty(int x, int y);

  @NotNull TermGraphics draw(int x, int y, char symbol);

  @NotNull TermGraphics draw(@NotNull TermPos pos, @NotNull TermGraphics termGraphics);

  @NotNull TermGraphics draw(@NotNull TermPos pos, @NotNull TermDim dim,
      @NotNull TermGraphics termGraphics);

  @NotNull TermGraphics draw(int x, int y, @NotNull TermGraphics graphics);

  @NotNull TermGraphics draw(int x, int y, int width, int height, @NotNull TermGraphics graphics);

  @NotNull TermGraphics drawLine(@NotNull TermPos from,
      @NotNull TermPos to, char symbol);

  @NotNull TermGraphics drawLine(int x1, int y1, int x2, int y2, char symbol);

  @NotNull TermGraphics drawRect(@NotNull TermPos position,
      @NotNull TermDim dimension, char symbol);

  @NotNull TermGraphics drawRect(int x, int y, int width,
      int height, char symbol);

  @NotNull TermGraphics fillRect(@NotNull TermPos position,
      @NotNull TermDim dimension, char symbol);

  @NotNull TermGraphics fillRect(int x, int y, int width, int height, char symbol);

  @NotNull TermGraphics drawString(@NotNull TermPos position,
      @NotNull TermString termString);

  @NotNull TermGraphics drawString(@NotNull TermPos position,
      @NotNull String value);

  @NotNull TermGraphics drawString(int x, int y, @NotNull TermString termString);

  @NotNull TermGraphics drawString(int x, int y, @NotNull String value);

  @NotNull TermGraphics drawShape(@NotNull TermPos pos, @NotNull TermShape shape);

  @NotNull TermGraphics drawShape(int x, int y, @NotNull TermShape shape);

  static @NotNull TermGraphics create(@NotNull TermDim dim) {
    return create(dim.width(), dim.height());
  }

  static @NotNull TermGraphics create(@NotNull TermDim dim, @NotNull CellData cellData) {
    return create(dim.width(), dim.height(), cellData);
  }

  static @NotNull TermGraphics create(int width, int height) {
    return create(width, height, TextStyle.create());
  }

  static @NotNull TermGraphics create(int width, int height, @NotNull CellData cellData) {
    return create(width, height, cellData, TextStyle.create());
  }

  static @NotNull TermGraphics from(@NotNull CellBuffer buffer) {
    return from(buffer, TextStyle.create());
  }

  static @NotNull TermGraphics create(@NotNull TermDim dim, @NotNull CellData cellData,
      @NotNull TextStyle initStyle) {
    return create(dim.width(), dim.height(), cellData, initStyle);
  }

  static @NotNull TermGraphics create(@NotNull TermDim dim, @NotNull TextStyle initStyle) {
    return create(dim.width(), dim.height(), initStyle);
  }

  static @NotNull TermGraphics create(int width, int height, @NotNull TextStyle initStyle) {
    CellBuffer buffer = new CellBuffer(width, height);
    return new TermGraphicsImpl(buffer, -1, -1, width, height, initStyle);
  }

  static @NotNull TermGraphics create(int width, int height,
      @NotNull CellData cellData, @NotNull TextStyle initStyle) {
    CellBuffer buffer = new CellBuffer(width, height, cellData);
    return new TermGraphicsImpl(buffer, -1, -1, width, height, initStyle);
  }

  static @NotNull TermGraphics from(@NotNull CellBuffer buffer,
      @NotNull TextStyle initStyle) {
    return new TermGraphicsImpl(buffer, -1, -1,
        buffer.width(), buffer.height(), initStyle);
  }

  static @NotNull TermGraphics transfer(@NotNull TermGraphics another,
      @NotNull TextStyle initStyle) {
    return from(another.buffer(), initStyle);
  }

}
