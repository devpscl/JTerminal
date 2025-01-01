package net.jterminal.ui.graphics;

import java.util.Arrays;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;

public class CellBuffer {

  private final CellData[][] buffer;

  private final int width;
  private final int height;
  private final CellData defaultData;

  public CellBuffer(int width, int height) {
    this(width, height, CellData.create());
  }

  public CellBuffer(int width, int height, @NotNull CellData defaultData) {
    this.width = Math.max(0, width);
    this.height = Math.max(0, height);
    this.buffer = new CellData[this.height][];
    this.defaultData = defaultData;
    clear();
  }

  public CellBuffer(@NotNull TerminalDimension dim) {
    this(dim.width(), dim.height());
  }

  public CellBuffer(@NotNull TerminalDimension dim, @NotNull CellData defaultData) {
    this(dim.width(), dim.height(), defaultData);
  }

  public void clear() {
    for (int idx = 0; idx < height; idx++) {
      CellData[] row = new CellData[width];
      Arrays.fill(row, defaultData);
      buffer[idx] = row;
    }
  }

  public boolean contains(@NotNull TerminalPosition pos) {
    return contains(pos.x(), pos.y());
  }

  public boolean contains(int x, int y) {
    return x < width && y < height && x >= 0 && y >= 0;
  }

  public void write(int x, int y, @NotNull CellData cellData) {
    if(!contains(x, y)) {
      return;
    }
    writeUnsafe(x, y, cellData);
  }

  public void write(@NotNull TerminalPosition pos, @NotNull CellData cellData) {
    if(!contains(pos)) {
      return;
    }
    writeUnsafe(pos.x(), pos.y(), cellData);
  }

  public void writeUnsafe(int x, int y, @NotNull CellData cellData) {
    buffer[y][x] = cellData;
  }

  public void fill(@NotNull TerminalPosition from,
      @NotNull TerminalPosition to,
      @NotNull CellData cellData) {
    fill(from.x(), from.y(), to.x(), to.y(), cellData);
  }

  public void fill(int x1, int y1, int x2, int y2, @NotNull CellData cellData) {
    int sx = Math.min(x1,x2);
    int ex = Math.max(x1,x2);
    if(sx >= width || sx < 0) {
      return;
    }
    int sy = Math.min(y1,y2);
    int ey = Math.max(y1,y2);
    if(sy >= height || sy < 0) {
      return;
    }
    fillUnsafe(sx, sy, ex, ey, cellData);
  }

  public void fillUnsafe(int x1, int y1, int x2, int y2, @NotNull CellData cellData) {
    for(int xp = x1; xp <= x2; xp++) {
      for(int yp = y1; yp <= y2; yp++) {
        buffer[yp][xp] = cellData;
      }
    }
  }

  public void insert(int x, int y, int width, int height, @NotNull CellBuffer buffer) {
    if(x >= this.width || x < 0 || width < 0) {
      return;
    }
    if(y >= this.height || y < 0 || height < 0) {
      return;
    }
    int rcols = this.width - x;
    int rrows = this.height - y;
    int cols = Math.min(width, rcols);
    int rows = Math.min(height, rrows);
    insertUnsafe(x, y, Math.min(cols, buffer.width), Math.min(rows, buffer.height), buffer);
  }

  public void insertUnsafe(int x, int y, int width, int height, @NotNull CellBuffer cellBuffer) {
    for(int yp = 0; yp < height; yp++) {
      System.arraycopy(cellBuffer.buffer[yp], 0, buffer[y + yp], x, width);
    }
  }

  public @NotNull CellData[][] to2dArray() {
    return buffer;
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }
}
