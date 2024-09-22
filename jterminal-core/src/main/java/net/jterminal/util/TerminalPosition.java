package net.jterminal.util;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public class TerminalPosition {

  private int x;
  private int y;

  public TerminalPosition() {
    this(1, 1);
  }

  public TerminalPosition(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public @NotNull TerminalPosition x(int x) {
    this.x = x;
    return this;
  }

  public @NotNull TerminalPosition y(int y) {
    this.y = y;
    return this;
  }

  public @NotNull TerminalPosition addX(int x) {
    this.x += x;
    return this;
  }

  public @NotNull TerminalPosition addY(int y) {
    this.y += y;
    return this;
  }

  public @NotNull TerminalPosition subtractX(int x) {
    this.x -= x;
    return this;
  }

  public @NotNull TerminalPosition subtractY(int y) {
    this.y -= y;
    return this;
  }

  public @NotNull TerminalPosition multiplyX(int x) {
    this.x *= x;
    return this;
  }

  public @NotNull TerminalPosition multiplyY(int y) {
    this.y *= y;
    return this;
  }

  public @NotNull TerminalPosition divideX(int x) {
    this.x /= x;
    return this;
  }

  public @NotNull TerminalPosition divideY(int y) {
    this.y /= y;
    return this;
  }

  public @NotNull TerminalPosition add(@NotNull TerminalPosition other) {
    this.x += other.x();
    this.y += other.y();
    return this;
  }

  public @NotNull TerminalPosition subtract(@NotNull TerminalPosition other) {
    this.x -= other.x();
    this.y -= other.y();
    return this;
  }

  public @NotNull TerminalPosition multiply(@NotNull TerminalPosition other) {
    this.x *= other.x();
    this.y *= other.y();
    return this;
  }

  public @NotNull TerminalPosition divide(@NotNull TerminalPosition other) {
    this.x /= other.x();
    this.y /= other.y();
    return this;
  }

  public @NotNull TerminalPosition add(int value) {
    this.x += value;
    this.y += value;
    return this;
  }

  public @NotNull TerminalPosition subtract(int value) {
    this.x -= value;
    this.y -= value;
    return this;
  }

  public @NotNull TerminalPosition multiply(int value) {
    this.x *= value;
    this.y *= value;
    return this;
  }

  public @NotNull TerminalPosition divide(int value) {
    this.x /= value;
    this.y /= value;
    return this;
  }

  public double distance(@NotNull TerminalPosition other) {
    return Math.sqrt(Math.pow(this.x - other.x(), 2) + Math.pow(this.y - other.y(), 2));
  }

  public @NotNull TerminalPosition reset() {
    this.x = 1;
    this.y = 1;
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
    TerminalPosition that = (TerminalPosition) o;
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

  public @NotNull TerminalPosition clone() {
    return new TerminalPosition(this.x, this.y);
  }

}
