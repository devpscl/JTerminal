package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.Combiner;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.shape.TermShape;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TermGraphicsImpl implements TermGraphics {

  private final CellBuffer buffer;
  private final int xOff;
  private final int yOff;
  private final int width;
  private final int height;
  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  private TextFont[] fonts;

  public TermGraphicsImpl(@NotNull CellBuffer cellBuffer, int xOff, int yOff,
      int width, int height) {
    this.buffer = cellBuffer;
    this.xOff = xOff;
    this.yOff = yOff;
    this.width = width;
    this.height = height;
    resetStyle();
  }

  @Override
  public @NotNull CellBuffer buffer() {
    return buffer;
  }

  @Override
  public int width() {
    return buffer.width();
  }

  @Override
  public int height() {
    return buffer.height();
  }

  @Override
  public @NotNull TerminalDimension size() {
    return new TerminalDimension(width(), height());
  }

  @Override
  public @NotNull TermGraphics style(@Nullable TextStyle textStyle) {
    if(textStyle == null) {
      resetStyle();
      return this;
    }
    this.foregroundColor = textStyle.foregroundColor();
    this.backgroundColor = textStyle.backgroundColor();
    this.fonts = textStyle.fonts().toArray(new TextFont[0]);
    return this;
  }

  @Override
  public @NotNull TermGraphics resetStyle() {
    this.foregroundColor = null;
    this.backgroundColor = null;
    this.fonts = new TextFont[0];
    return this;
  }

  @Override
  public @NotNull TermGraphics foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }

  @Override
  public @NotNull TermGraphics backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  @Override
  public @NotNull TermGraphics fonts(@NotNull TextFont... fonts) {
    this.fonts = fonts;
    return this;
  }

  @Override
  public @Nullable ForegroundColor foregroundColor() {
    return foregroundColor;
  }

  @Override
  public @Nullable BackgroundColor backgroundColor() {
    return backgroundColor;
  }

  @Override
  public @NotNull TextFont[] fonts() {
    return fonts;
  }

  @Override
  public @NotNull TermGraphics drawEmpty(@NotNull TerminalPosition pos) {
    return drawEmpty(pos.x(), pos.y());
  }

  @Override
  public @NotNull TermGraphics draw(@NotNull TerminalPosition pos, char symbol) {
    return draw(pos.x(), pos.y(), symbol);
  }

  @Override
  public @NotNull TermGraphics drawEmpty(int x, int y) {
    return draw(x, y, CellData.EMPTY_SYMBOL);
  }

  @Override
  public @NotNull TermGraphics draw(int x, int y, char symbol) {
    CellData cellData = CellData.create(symbol, foregroundColor,
        backgroundColor, fonts);
    buffer.write(x + xOff, y + yOff, cellData);
    return this;
  }

  @Override
  public @NotNull TermGraphics draw(@NotNull TerminalPosition pos,
      @NotNull TermGraphics termGraphics) {
    return draw(pos.x(), pos.y(), termGraphics);
  }

  @Override
  public @NotNull TermGraphics draw(@NotNull TerminalPosition pos, @NotNull TerminalDimension dim,
      @NotNull TermGraphics termGraphics) {
    return draw(pos.x(), pos.y(), dim.width(), dim.height(), termGraphics);
  }

  @Override
  public @NotNull TermGraphics draw(int x, int y, @NotNull TermGraphics graphics) {
    TerminalDimension size = graphics.size();
    return draw(x, y, size.width(), size.height(), graphics);
  }

  @Override
  public @NotNull TermGraphics draw(int x, int y, int width, int height,
      @NotNull TermGraphics graphics) {
    final TerminalDimension size = graphics.size();
    final int cols = Math.min(width, size.width());
    final int rows = Math.min(height, size.height());
    buffer.insert(x + xOff, y + yOff, width, height, graphics.buffer());
    return this;
  }

  @Override
  public @NotNull TermGraphics drawLine(@NotNull TerminalPosition from,
      @NotNull TerminalPosition to, char symbol) {
    return drawLine(from.x(), from.y(), to.x(), to.y(), symbol);
  }

  @Override
  public @NotNull TermGraphics drawLine(int x1, int y1, int x2, int y2, char symbol) {
    CellData cellData = CellData.create(symbol, foregroundColor,
        backgroundColor, fonts);
    TerminalPosition pos1 = new TerminalPosition(x1, y1);
    TerminalPosition pos2 = new TerminalPosition(x2, y2);
    final double distance = Math.floor(pos1.distance(pos2));
    final int distanceInt = (int) distance;

    double xStep = (double) (x2 - x1) / distanceInt;
    double yStep = (double) (y2 - y1) / distanceInt;
    for (int step = 0; step <= distanceInt; step++) {
      double xd = x1 + xStep * step;
      double yd = y1 + yStep * step;
      buffer.write((int)xd + xOff, (int)yd + yOff, cellData);
    }
    return this;
  }

  @Override
  public @NotNull TermGraphics drawRect(@NotNull TerminalPosition position,
      @NotNull TerminalDimension dimension, char symbol) {
    return drawRect(position.x(), position.y(), dimension.width(),
        dimension.height(), symbol);
  }

  @Override
  public @NotNull TermGraphics drawRect(int x, int y, int width,
      int height, char symbol) {
    CellData cellData = CellData.create(symbol, foregroundColor,
        backgroundColor, fonts);

    int x2 = x + width - 1;
    int y2 = y + height - 1;
    int minX = Math.min(x, x2) + xOff;
    int minY = Math.min(y, y2) + yOff;
    int maxX = Math.max(x, x2) + xOff;
    int maxY = Math.max(y, y2) + yOff;

    buffer.fill(minX, minY, maxX, minY, cellData);
    buffer.fill(minX, minY, minX, maxY, cellData);
    buffer.fill(maxX, maxY, maxX, minY, cellData);
    buffer.fill(maxX, maxY, minX, maxY, cellData);
    return this;
  }

  @Override
  public @NotNull TermGraphics fillRect(@NotNull TerminalPosition position,
      @NotNull TerminalDimension dimension, char symbol) {
    return fillRect(position.x(), position.y(), dimension.width(), dimension.height(), symbol);
  }

  @Override
  public @NotNull TermGraphics fillRect(int x, int y, int width,
      int height, char symbol) {
    CellData data = CellData.create(symbol, foregroundColor,
        backgroundColor, fonts);

    int x2 = x + width - 1;
    int y2 = y + height - 1;
    int minX = Math.min(x, x2) + xOff;
    int minY = Math.min(y, y2) + yOff;
    int maxX = Math.max(x, x2) + xOff;
    int maxY = Math.max(y, y2) + yOff;

    buffer.fill(minX, minY, maxX, maxY, data);
    return this;
  }

  @Override
  public @NotNull TermGraphics drawString(@NotNull TerminalPosition position,
      @NotNull TermString termString) {
    return drawString(position.x(), position.y(), termString);
  }

  @Override
  public @NotNull TermGraphics drawString(@NotNull TerminalPosition position,
      @NotNull String value) {
    return drawString(position.x(), position.y(), value);
  }

  @Override
  public @NotNull TermGraphics drawString(int x, int y, @NotNull TermString termString) {
    TermString[] lines = termString.split('\n');
    TextStyle style = TextStyle.create(foregroundColor, backgroundColor, fonts);
    for (TermString line : lines) {
      char[] charArray = line.raw().toCharArray();
      int p = x;
      for (int idx = 0; idx < charArray.length; idx++) {
        TextStyle indexedStyle = line.styleAt(idx);
        char c = charArray[idx];
        TextStyle combined = Combiner.combine(indexedStyle, style);
        if(combined.foregroundColor() == TerminalColor.DEFAULT) {
          combined.foregroundColor(foregroundColor);
        }
        if(combined.backgroundColor() == TerminalColor.DEFAULT) {
          combined.backgroundColor(backgroundColor);
        }
        CellData data = CellData.create(c, combined.foregroundColor(),
            combined.backgroundColor(),
            combined.fonts().toArray(new TextFont[0]));
        buffer.write(p + xOff, y + yOff, data);
        p++;
      }
      y++;
    }
    return this;
  }

  @Override
  public @NotNull TermGraphics drawString(int x, int y, @NotNull String value) {
    String[] lines = value.split("\n");
    for (String line : lines) {
      char[] charArray = line.toCharArray();
      int p = x;
      for (char c : charArray) {
        CellData data = CellData.create(c, foregroundColor, backgroundColor, fonts);
        buffer.write(p + xOff, y + yOff, data);
        p++;
      }
      y++;
    }
    return this;
  }

  @Override
  public @NotNull TermGraphics drawShape(@NotNull TerminalPosition pos, @NotNull TermShape shape) {
    TerminalDimension size = shape.size();
    shape.render(this, pos, size);
    return this;
  }

  @Override
  public @NotNull TermGraphics drawShape(int x, int y, @NotNull TermShape shape) {
    return drawShape(new TerminalPosition(x, y), shape);
  }

}
