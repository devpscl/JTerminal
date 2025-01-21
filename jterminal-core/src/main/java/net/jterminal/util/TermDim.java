package net.jterminal.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class TermDim {

  private int width;
  private int height;

  public TermDim(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public TermDim() {
    this(0, 0);
  }

  public int width() {
    return width;
  }

  public int height() {
    return height;
  }

  public int cols() {
    return width;
  }

  public int rows() {
    return height;
  }

  public void securePositive() {
    if(width < 0) {
      throw new IllegalArgumentException("Negative width: " + width + " < 0");
    }
    if(height < 0) {
      throw new IllegalArgumentException("Negative height: " + height + " < 0");
    }
  }

  public @NotNull TermDim width(int width) {
    this.width = width;
    return this;
  }

  public @NotNull TermDim height(int height) {
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
    TermDim that = (TermDim) o;
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

  public @NotNull TermDim copy() {
    return new TermDim(width, height);
  }

}
