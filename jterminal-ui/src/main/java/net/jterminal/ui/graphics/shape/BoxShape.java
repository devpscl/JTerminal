package net.jterminal.ui.graphics.shape;

import java.util.Arrays;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.BoxCharacter;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxShape implements TermShape {

  private final byte[][] data;
  private final TermDim dimension;

  public BoxShape(@NotNull TermDim dimension) {
    this.dimension = dimension;
    this.data = new byte[dimension.height()][];
    for (int row = 0; row < dimension.height(); row++) {
      byte[] rowArr = new byte[dimension.width()];
      Arrays.fill(rowArr, (byte) 0);
      data[row] = rowArr;
    }
  }

  private byte convertTo2Bit(@Nullable Type type) {
    return type == null ? 0 : (byte) (type.ordinal() + 1);
  }

  private @Nullable Type convertFrom2Bit(int bits) {
    if(bits > Type.values().length || bits < 1) {
      return null;
    }
    return Type.values()[bits-1];
  }

  protected void write(int x, int y, @NotNull BoxCharEntry entry) {
    int data = 0;
    data |= convertTo2Bit(entry.top);
    data |= convertTo2Bit(entry.right) << 2;
    data |= convertTo2Bit(entry.bottom) << 4;
    data |= convertTo2Bit(entry.left) << 6;
    this.data[y-1][x-1] = (byte) data;
  }

  protected @NotNull BoxCharEntry read(int x, int y) {
    int data = this.data[y-1][x-1];
    if(data == 0) {
      return new BoxCharEntry();
    }
    Type top = convertFrom2Bit(data & 0x3);
    Type right = convertFrom2Bit((data >> 2) & 0x3);
    Type bottom = convertFrom2Bit((data >> 4) & 0x3);
    Type left = convertFrom2Bit((data >> 6) & 0x3);
    return new BoxCharEntry(top, right, bottom, left);
  }

  public char charAt(int x, int y) {
    BoxCharEntry entry = read(x, y);
    return BoxCharacter.createChar(entry.top, entry.right, entry.bottom, entry.left);
  }

  public void addBox(int x1, int y1, int x2, int y2, @NotNull Type type) {
    addHorizontal(x1, y1, x2, type);
    addHorizontal(x1, y2, x2, type);
    addVertical(x1, y1, y2, type);
    addVertical(x2, y1, y2, type);
  }

  public void addVertical(int x, int y1, int y2, @NotNull Type type) {
    int minY = Math.min(y1, y2);
    int maxY = Math.max(y1, y2);
    for (int y = minY; y <= maxY; y++) {
      BoxCharEntry entry = read(x, y);
      if(y != minY) {
        entry.top = type;
      }
      if(y != maxY) {
        entry.bottom = type;
      }
      write(x, y, entry);
    }
  }

  public void addHorizontal(int x1, int y, int x2, @NotNull Type type) {
    int minX = Math.min(x1, x2);
    int maxX = Math.max(x1, x2);
    for(int x = minX; x <= maxX; x++) {
      BoxCharEntry entry = read(x, y);
      if(x != minX) {
        entry.left = type;
      }
      if(x != maxX) {
        entry.right = type;
      }
      write(x, y, entry);
    }
  }

  @Override
  public @NotNull TermDim size() {
    return dimension;
  }

  @Override
  public void render(@NotNull TermGraphics graphics, @NotNull TermPos pos,
      @NotNull TermDim dim) {
    int width = dim.width();
    int height = dim.height();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int xpos = pos.x() + x;
        int ypos = pos.y() + y;
        if(data[ypos-1][xpos-1] == 0) {
          continue;
        }
        char c = charAt(xpos, ypos);
        graphics.draw(xpos, ypos, c);
      }
    }
  }

  protected static class BoxCharEntry {

    public Type top;
    public Type right;
    public Type bottom;
    public Type left;

    public BoxCharEntry(
        @Nullable Type top,
        @Nullable Type right,
        @Nullable Type bottom,
        @Nullable Type left) {
      this.top = top;
      this.right = right;
      this.bottom = bottom;
      this.left = left;
    }

    public BoxCharEntry() {}

  }

}
