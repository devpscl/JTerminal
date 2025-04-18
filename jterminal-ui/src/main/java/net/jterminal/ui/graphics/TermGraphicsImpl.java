package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.Combiner;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.shape.TermShape;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TermGraphicsImpl implements TermGraphics {

  private final CellBuffer buffer;
  private final int xOff;
  private final int yOff;
  private final int width;
  private final int height;
  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  private TextFont[] fonts;
  private final TextStyle initStyle;

  public TermGraphicsImpl(@NotNull CellBuffer cellBuffer, int xOff, int yOff,
      int width, int height, @NotNull TextStyle initStyle) {
    this.buffer = cellBuffer;
    this.xOff = xOff;
    this.yOff = yOff;
    this.width = width;
    this.height = height;
    this.initStyle = initStyle;
    style(initStyle);
  }

  @Override
  public @NotNull CellBuffer buffer() {
    return buffer;
  }

  @Override
  public int width() {
    return width;
  }

  @Override
  public int height() {
    return height;
  }

  @Override
  public @NotNull TermDim size() {
    return new TermDim(width(), height());
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
  public @NotNull TextStyle style() {
    return TextStyle.create(foregroundColor, backgroundColor, fonts);
  }

  @Override
  public @NotNull TextStyle initStyle() {
    return initStyle.copy();
  }

  @Override
  public @NotNull TermGraphics resetStyle() {
    style(initStyle);
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
  public @NotNull TermGraphics drawEmpty(@NotNull TermPos pos) {
    return drawEmpty(pos.x(), pos.y());
  }

  @Override
  public @NotNull TermGraphics draw(@NotNull TermPos pos, char symbol) {
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
  public @NotNull TermGraphics draw(@NotNull TermPos pos,
      @NotNull TermGraphics termGraphics) {
    return draw(pos.x(), pos.y(), termGraphics);
  }

  @Override
  public @NotNull TermGraphics draw(@NotNull TermPos pos, @NotNull TermDim dim,
      @NotNull TermGraphics termGraphics) {
    return draw(pos.x(), pos.y(), dim.width(), dim.height(), termGraphics);
  }

  @Override
  public @NotNull TermGraphics draw(int x, int y, @NotNull TermGraphics graphics) {
    TermDim size = graphics.size();
    return draw(x, y, size.width(), size.height(), graphics);
  }

  @Override
  public @NotNull TermGraphics draw(int x, int y, int width, int height,
      @NotNull TermGraphics graphics) {
    final TermDim size = graphics.size();
    final int cols = Math.min(width, size.width());
    final int rows = Math.min(height, size.height());
    buffer.insert(x + xOff, y + yOff, width, height, graphics.buffer());
    return this;
  }

  @Override
  public @NotNull TermGraphics drawLine(@NotNull TermPos from,
      @NotNull TermPos to, char symbol) {
    return drawLine(from.x(), from.y(), to.x(), to.y(), symbol);
  }

  @Override
  public @NotNull TermGraphics drawLine(int x1, int y1, int x2, int y2, char symbol) {
    CellData cellData = CellData.create(symbol, foregroundColor,
        backgroundColor, fonts);
    TermPos pos1 = new TermPos(x1, y1);
    TermPos pos2 = new TermPos(x2, y2);
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
  public @NotNull TermGraphics drawRect(@NotNull TermPos position,
      @NotNull TermDim dimension, char symbol) {
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
  public @NotNull TermGraphics fillRect(@NotNull TermPos position,
      @NotNull TermDim dimension, char symbol) {
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
  public @NotNull TermGraphics drawString(@NotNull TermPos position,
      @NotNull TermString termString) {
    return drawString(position.x(), position.y(), termString);
  }

  @Override
  public @NotNull TermGraphics drawString(@NotNull TermPos position,
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
  public @NotNull TermGraphics drawShape(@NotNull TermPos pos, @NotNull TermShape shape) {
    TermDim size = shape.size();
    shape.render(this, pos, size);
    return this;
  }

  @Override
  public @NotNull TermGraphics drawShape(int x, int y, @NotNull TermShape shape) {
    return drawShape(new TermPos(x, y), shape);
  }

}
