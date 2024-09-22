package net.jterminal.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class TerminalDimension {

  private int width;
  private int height;

  public TerminalDimension(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public TerminalDimension() {
    this(0, 0);
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public @NotNull TerminalDimension width(int width) {
    this.width = width;
    return this;
  }

  public @NotNull TerminalDimension height(int height) {
    this.height = height;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TerminalDimension that = (TerminalDimension) o;
    return width == that.width && height == that.height;
  }

  @Override
  public int hashCode() {
    return Objects.hash(width, height);
  }

  @Override
  public String toString() {
    return "DIM(" + width + ", " + height + ")";
  }

  public @NotNull TerminalDimension clone() {
    return new TerminalDimension(width, height);
  }

}
