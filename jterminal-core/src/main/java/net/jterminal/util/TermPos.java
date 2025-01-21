package net.jterminal.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class TermPos {

  public static final int AXIS_ORIGIN = 1;

  private int x;
  private int y;

  public TermPos() {
    this(AXIS_ORIGIN, AXIS_ORIGIN);
  }

  public TermPos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public int col() {
    return x;
  }

  public int row() {
    return y;
  }

  public void secureOriginPositive() {
    if(x < AXIS_ORIGIN) {
      throw new IllegalArgumentException("Invalid position: (x) " + x + " < " + AXIS_ORIGIN);
    }
    if(y < AXIS_ORIGIN) {
      throw new IllegalArgumentException("Invalid position: (y) " + y + " < " + AXIS_ORIGIN);
    }
  }

  public void securePositive() {
    if(x < 0) {
      throw new IllegalArgumentException("Invalid position: (x) " + x + " is negative");
    }
    if(y < 0) {
      throw new IllegalArgumentException("Invalid position: (y) " + y + " is negative");
    }
  }

  public @NotNull TermPos x(int x) {
    this.x = x;
    return this;
  }

  public @NotNull TermPos y(int y) {
    this.y = y;
    return this;
  }

  public @NotNull TermPos addX(int x) {
    this.x += x;
    return this;
  }

  public @NotNull TermPos addY(int y) {
    this.y += y;
    return this;
  }

  public @NotNull TermPos subtractX(int x) {
    this.x -= x;
    return this;
  }

  public @NotNull TermPos subtractY(int y) {
    this.y -= y;
    return this;
  }

  public @NotNull TermPos multiplyX(int x) {
    this.x *= x;
    return this;
  }

  public @NotNull TermPos multiplyY(int y) {
    this.y *= y;
    return this;
  }

  public @NotNull TermPos divideX(int x) {
    this.x /= x;
    return this;
  }

  public @NotNull TermPos divideY(int y) {
    this.y /= y;
    return this;
  }

  public @NotNull TermPos add(@NotNull TermPos other) {
    this.x += other.x();
    this.y += other.y();
    return this;
  }

  public @NotNull TermPos addShift(@NotNull TermPos other) {
    this.x += other.x() - AXIS_ORIGIN;
    this.y += other.y() - AXIS_ORIGIN;
    return this;
  }

  public @NotNull TermPos addShift(@NotNull TermDim other) {
    this.x += other.width() - AXIS_ORIGIN;
    this.y += other.height() - AXIS_ORIGIN;
    return this;
  }

  public @NotNull TermPos subtract(@NotNull TermPos other) {
    this.x -= other.x();
    this.y -= other.y();
    return this;
  }

  public @NotNull TermPos subtractShift(@NotNull TermPos other) {
    this.x -= other.x() - AXIS_ORIGIN;
    this.y -= other.y() - AXIS_ORIGIN;
    return this;
  }

  public @NotNull TermPos subtractShift(@NotNull TermDim other) {
    this.x -= other.width() - AXIS_ORIGIN;
    this.y -= other.height() - AXIS_ORIGIN;
    return this;
  }

  public @NotNull TermPos multiply(@NotNull TermPos other) {
    this.x *= other.x();
    this.y *= other.y();
    return this;
  }

  public @NotNull TermPos divide(@NotNull TermPos other) {
    this.x /= other.x();
    this.y /= other.y();
    return this;
  }

  public @NotNull TermPos add(int value) {
    this.x += value;
    this.y += value;
    return this;
  }

  public @NotNull TermPos subtract(int value) {
    this.x -= value;
    this.y -= value;
    return this;
  }

  public @NotNull TermPos multiply(int value) {
    this.x *= value;
    this.y *= value;
    return this;
  }

  public @NotNull TermPos divide(int value) {
    this.x /= value;
    this.y /= value;
    return this;
  }

  public double distance(@NotNull TermPos other) {
    return Math.sqrt(Math.pow(this.x - other.x(), 2) + Math.pow(this.y - other.y(), 2));
  }

  public boolean isXOrigin() {
    return x == AXIS_ORIGIN;
  }

  public boolean isYOrigin() {
    return y == AXIS_ORIGIN;
  }

  public @NotNull TermPos reset() {
    this.x = AXIS_ORIGIN;
    this.y = AXIS_ORIGIN;
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
    TermPos that = (TermPos) o;
    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "POS(" + x + ", " + y + ")";
  }

  public @NotNull TermPos copy() {
    return new TermPos(this.x, this.y);
  }

}
