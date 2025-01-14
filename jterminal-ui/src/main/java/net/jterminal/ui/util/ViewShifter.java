package net.jterminal.ui.util;

public class ViewShifter {

  private int bufferSize;
  private int viewSize;
  private int offset;
  private int cursor;

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

  public int offset() {
    return offset;
  }

  public void cursor(int cursor) {
    this.cursor = Math.max(0, Math.min(bufferSize, cursor));
    int minOffset = Math.max(0, bufferSize - this.cursor - viewSize);
    int maxOffset = Math.max(0, bufferSize - this.cursor);
    this.offset = Math.min(maxOffset, Math.max(minOffset, offset));
  }

  public void offset(int offset) {
    this.offset = Math.max(0, Math.min(bufferSize - viewSize, offset));
    int minCursor = bufferSize - this.offset - viewSize;
    int maxCursor = bufferSize - this.offset;
    this.cursor = Math.min(maxCursor, Math.max(minCursor, cursor));
  }

  public void backwardAll() {
    cursor = 0;
    offset = bufferSize - viewSize;
  }

  public void forwardAll() {
    cursor = bufferSize;
    offset = 0;
  }

  public void shiftBackward(int n) {
    offset(offset + n);
  }

  public void shiftForward(int n) {
    offset(offset - n);
  }

  public void backward(int n) {
    cursor(cursor - n);
  }

  public void forward(int n) {
    cursor(cursor + n);
  }

  public int bufferStart() {
    return Math.max(0, bufferSize - viewSize - offset);
  }

  public int bufferEnd() {
    return bufferSize - offset;
  }

  public int viewCursor() {
    return cursor - Math.max(0, bufferSize - viewSize - offset);
  }

  public int viewIndexToBufferIndex(int viewCursor) {
    return bufferStart() + viewCursor;
  }

  public boolean cursorAtEnd() {
    return cursor >= bufferSize;
  }

  public boolean cursorAtStart() {
    return cursor <= 0;
  }

  public boolean viewCursorAtEnd() {
    return cursor >= viewSize;
  }

  public boolean viewCursorAtStart() {
    return cursor <= 0;
  }

  public boolean viewGreaterThanBuffer() {
    return viewSize >= bufferSize;
  }

  public boolean viewLesserThanBuffer() {
    return viewSize <= bufferSize;
  }

}
