package net.jterminal.ui.util;


import java.util.AbstractList;
import java.util.Objects;

public class IntRange extends AbstractList<Integer> {

  private final int start;
  private final int end;

  public IntRange(int start, int end) {
    this.start = start;
    this.end = end;
  }

  @Override
  public Integer get(int index) {
    return start + index;
  }

  @Override
  public int size() {
    return end-start;
  }

  public int start() {
    return start;
  }

  public int end() {
    return end;
  }

  public boolean contains(int x) {
    return x >= start && x <= end;
  }

  @Override
  public String toString() {
    return "IntRange{" +
        "start=" + start +
        ", end=" + end +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    IntRange integers = (IntRange) o;
    return start == integers.start && end == integers.end;
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), start, end);
  }

}
