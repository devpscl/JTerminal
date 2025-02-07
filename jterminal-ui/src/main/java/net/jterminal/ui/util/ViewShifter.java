package net.jterminal.ui.util;

import static java.lang.Math.min;
import static net.jterminal.ui.util.MathUtil.*;

import org.jetbrains.annotations.NotNull;

public class ViewShifter {

  public enum Type {
    /**recommend for navigate lists*/
    INDEX_SHIFTER,
    /**recommend for navigate text cursor*/
    POINTER_SHIFTER
  }

  private int bufferSize;
  private int viewSize;
  private int shift;
  private int cursor;
  private final Type type;

  public ViewShifter(Type type) {
    this.type = type;
  }

  public @NotNull Type type() {
    return type;
  }

  private int modifiedBufferSize() {
    return type == Type.INDEX_SHIFTER ? nonNegative(bufferSize - 1) : bufferSize;
  }

  private int modifiedViewSize() {
    return type == Type.INDEX_SHIFTER ? nonNegative(viewSize - 1) : viewSize;
  }

  public int maxShift() {
    return nonNegative(modifiedBufferSize() - modifiedViewSize());
  }

  public int bufferSize() {
    return bufferSize;
  }

  public void bufferSize(int size) {
    this.bufferSize = size;
  }

  public int viewSize() {
    return viewSize;
  }

  public void viewSize(int size) {
    if(viewSize == size) {
      return;
    }
    this.viewSize = size;
    cursor(cursor);
  }

  public int cursor() {
    return cursor;
  }

  public int shift() {
    return shift;
  }

  public void cursor(int cursor) {
    this.cursor = range(cursor, 0, modifiedBufferSize());
    final int minShift = nonNegative(this.cursor - modifiedViewSize());
    final int maxShift = nonNegative(this.cursor);
    if(!viewLesserThanBuffer()) {
      shift = 0;
      return;
    }
    shift = range(shift, minShift, maxShift);
  }

  public void shift(int shift) {
    this.shift = range(shift, 0, maxShift());
    final int minCursor = nonNegative(this.shift);
    final int maxCursor = this.shift + modifiedViewSize();
    this.cursor = range(cursor, minCursor, maxCursor);
  }

  public void backwardAll() {
    cursor = 0;
    shift = 0;
  }

  public void forwardAll() {
    cursor = modifiedBufferSize();
    shift = maxShift();
  }

  public void shiftBackward(int n) {
    shift(shift - n);
  }

  public void shiftForward(int n) {
    shift(shift + n);
  }

  public void backward(int n) {
    cursor(cursor - n);
  }

  public void forward(int n) {
    cursor(cursor + n);
  }

  public int bufferStart() {
    return nonNegative(shift);
  }

  public int bufferEnd() {
    return min(shift + viewSize, bufferSize);
  }

  public int viewCursor() {
    return nonNegative(cursor - shift);
  }

  public int viewIndexToBufferIndex(int viewCursor) {
    return bufferStart() + viewCursor;
  }

  public boolean cursorAtEnd() {
    return cursor >= modifiedBufferSize();
  }

  public boolean cursorAtStart() {
    return cursor <= 0;
  }

  public boolean viewCursorAtEnd() {
    return cursor >= modifiedViewSize();
  }

  public boolean viewCursorAtStart() {
    return cursor <= 0;
  }

  public boolean viewGreaterThanBuffer() {
    return modifiedViewSize() > modifiedBufferSize();
  }

  public boolean viewLesserThanBuffer() {
    return modifiedViewSize() < modifiedBufferSize();
  }

}
