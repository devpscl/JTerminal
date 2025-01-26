package net.jterminal.ui.component.selectable;

import static net.jterminal.input.Keyboard.KEY_ARROW_DOWN;
import static net.jterminal.input.Keyboard.KEY_ARROW_LEFT;
import static net.jterminal.input.Keyboard.KEY_ARROW_RIGHT;
import static net.jterminal.input.Keyboard.KEY_ARROW_UP;
import static net.jterminal.input.Keyboard.KEY_BACKSPACE;
import static net.jterminal.input.Keyboard.KEY_ENTER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.jterminal.input.Keyboard.State;
import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.text.termstring.TermStringBuilder;
import net.jterminal.text.termstring.TermStringJoiner;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.component.scrollbar.VirtualScrollBar;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.special.TextChangedEvent;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.graphics.TerminalState.CursorType;
import net.jterminal.ui.util.Axis;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.ui.util.ViewShifter.Type;
import net.jterminal.util.CharFilter;
import net.jterminal.util.CharFilter.CharType;
import net.jterminal.util.StringUtil;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class TextAreaComponent extends SelectableComponent implements Resizeable {

  private static final CharFilter charFilter = new CharFilter(CharType.DIGIT,
      CharType.WHITESPACE, CharType.LETTERS_LOWERCASE,
      CharType.LETTERS_UPPERCASE, CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL);

  private final List<TermString> lines = new ArrayList<>();
  private final ViewShifter xShifter = new ViewShifter(Type.POINTER_SHIFTER);
  private final ViewShifter yShifter = new ViewShifter(Type.INDEX_SHIFTER);

  private CursorType cursorType = CursorType.BLINKING;

  private VirtualScrollBar verticalScrollbar;
  private VirtualScrollBar horizontalScrollbar;

  public TextAreaComponent() {
    backgroundColor(TerminalColor.GRAY);
    foregroundColor(TerminalColor.BLACK);
  }

  public @NotNull CursorType cursorType() {
    return cursorType;
  }

  public void cursorType(@NotNull CursorType cursorType) {
    this.cursorType = cursorType;
    repaint();
  }

  public @NotNull VirtualScrollBar attachScrollBar(@NotNull Axis axis) {
    synchronized (lock) {
      if(axis == Axis.VERTICAL) {
        verticalScrollbar = new VirtualScrollBar(axis);
        updateScrollBar();
        repaint();
        return verticalScrollbar;
      }
      horizontalScrollbar = new VirtualScrollBar(axis);
      updateScrollBar();
      repaint();
      return horizontalScrollbar;
    }
  }

  public void detachScrollBar(@NotNull Axis axis) {
    synchronized (lock) {
      if(axis == Axis.VERTICAL) {
        verticalScrollbar = null;
        repaint();
        return;
      }
      horizontalScrollbar = null;
      repaint();
    }
  }

  public void updateScrollBar() {
    fixXShifter();
    if(verticalScrollbar != null) {
      verticalScrollbar.setup(yShifter, false);
      verticalScrollbar.endShrinkLevel(xShifter.viewSize());
    }
    if(horizontalScrollbar != null) {
      horizontalScrollbar.setup(xShifter, false);
      horizontalScrollbar.endShrinkLevel(xShifter.viewSize());
    }
  }

  private void updateShifters() {
    xShifter.bufferSize(maxLineWidth());
    yShifter.bufferSize(lines.size());
  }

  private void fixXShifter() {
    int line = cursorLine();
    int max = getLine(line).length();
    xShifter.cursor(Math.min(max, xShifter.cursor()));
  }

  private int maxLineWidth() {
    int count = 0;
    int len;
    for (TermString line : lines) {
      len = line.length();
      count = Math.max(count, len);
    }
    return count;
  }

  private @NotNull TermString getLine(int index) {
    if(lines.size() <= index) {
      return TermString.empty();
    }
    return lines.get(index);
  }

  private void setLine(int index, @NotNull TermString termString) {
    while(lines.size() <= index) {
      lines.add(TermString.empty());
    }
    lines.set(index, termString);
  }

  private void insertLine(int index, @NotNull TermString value) {
    lines.add(index, value);
  }

  private void removeLine(int index) {
    lines.remove(index);
    if(lines.isEmpty()) {
      lines.add(index, TermString.empty());
    }
  }

  public int cursorChar() {
    return xShifter.cursor();
  }

  public int cursorLine() {
    return yShifter.cursor();
  }

  public void text(@NotNull String text) {
    synchronized (lock) {
      String[] array = StringUtil.NEXT_LINE_PATTERN.split(text);
      lines.clear();
      for (String s : array) {
        lines.add(TermString.value(s));
      }
      if(lines.isEmpty()) {
        lines.add(TermString.empty());
      }
      updateShifters();
      repaint();
      postTextChangeEvent();
    }
  }

  public void text(@NotNull TermString termString) {
    synchronized (lock) {
      @NotNull TermString[] split = termString.split(StringUtil.NEXT_LINE_PATTERN);
      lines.clear();
      lines.addAll(Arrays.asList(split));
      if(lines.isEmpty()) {
        lines.add(TermString.empty());
      }
      updateShifters();
      repaint();
      postTextChangeEvent();
    }
  }

  public @NotNull TermString text() {
    synchronized (lock) {
      TermStringJoiner joiner = TermString.joiner();
      for (TermString line : lines) {
        joiner.add(line);
      }
      joiner.delimiter("\n");
      return joiner.build();
    }
  }

  public @NotNull List<TermString> viewLines() {
    List<TermString> list = new ArrayList<>();
    int startX = xShifter.bufferStart();
    int endX = xShifter.bufferEnd();
    int startY = yShifter.bufferStart();
    int endY = yShifter.bufferEnd();
    for(int y = startY; y < endY; y++) {
      TermString line = getLine(y);
      int safeStartX = Math.min(line.length(), startX);
      int safeEndX = Math.min(line.length(), endX);
      TermString termString = line.substring(safeStartX, safeEndX);
      list.add(termString);
    }
    return list;
  }

  private void updateViewSize() {
    int width = currentDimension().width();
    int height = currentDimension().height();
    if(verticalScrollbar != null) {
      width--;
    }
    if(horizontalScrollbar != null) {
      height--;
    }
    xShifter.viewSize(width);
    yShifter.viewSize(height);
    fixXShifter();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    updateViewSize();
    updateScrollBar();
    final TextStyle initStyle = graphics.style();
    TermDim termDim = currentDimension();
    int off = 1;
    for (TermString termString : viewLines()) {
      graphics.drawString(1, off++, termString);
    }
    int width = currentDimension().width();
    int height = currentDimension().height();
    if(verticalScrollbar != null && yShifter.viewLesserThanBuffer()) {
      verticalScrollbar.size(height - 1);
      TermGraphics innerGraphics = TermGraphics.create(1, height - 1);
      innerGraphics.style(initStyle);
      verticalScrollbar.draw(innerGraphics);
      graphics.draw(width, 1, innerGraphics);
    }
    if(horizontalScrollbar != null && xShifter.viewLesserThanBuffer()) {
      horizontalScrollbar.size(width - 1);
      TermGraphics innerGraphics = TermGraphics.create(width - 1, 1);
      innerGraphics.style(initStyle);
      horizontalScrollbar.draw(innerGraphics);
      graphics.draw(1, height, innerGraphics);
    }
  }

  @Override
  public void updateState(@NotNull TerminalState terminalState) {
    super.updateState(terminalState);
    int x = xShifter.viewCursor();
    int y = yShifter.viewCursor();
    terminalState.cursorPosition(new TermPos().addX(x).addY(y));
    terminalState.cursorType(cursorType);
  }


  protected void performMoveLeft() {
    xShifter.backward(1);
    fixXShifter();
    repaint();
  }

  protected void performMoveRight() {
    xShifter.forward(1);
    fixXShifter();
    repaint();
  }

  protected void performMoveUp() {
    yShifter.backward(1);
    fixXShifter();
    repaint();
  }

  protected void performMoveDown() {
    yShifter.forward(1);
    fixXShifter();
    repaint();
  }

  protected void postTextChangeEvent() {
    if(eventBus.isListenedTo(TextChangedEvent.class)) {
      TermString text = text();
      eventBus.post(new TextChangedEvent(text));
    }
  }

  protected void performNewLine() {
    int lineIndex = cursorLine();
    int charIndex = cursorChar();
    TermString line = getLine(lineIndex);
    charIndex = Math.min(line.length(), charIndex);
    TermString partLeft = line.substring(0, charIndex);
    setLine(lineIndex, partLeft);
    TermString partRight = line.substring(charIndex, line.length());
    insertLine(lineIndex + 1, partRight);
    updateShifters();
    xShifter.backwardAll();
    yShifter.forward(1);
    repaint();
    postTextChangeEvent();
  }

  protected void performBackspace() {
    int lineIndex = cursorLine();
    TermString line = getLine(lineIndex);
    if(cursorChar() > 0) {
      TermStringBuilder builder = TermString.builder(line);
      builder.deleteAt(cursorChar() - 1);
      TermString content = builder.build();
      setLine(lineIndex, content);
      updateShifters();
      performMoveLeft();
      postTextChangeEvent();
      return;
    }
    if(lineIndex <= 0) {
      return;
    }
    TermString upperLine = getLine(lineIndex - 1);
    int upperLineLength = upperLine.length();
    TermString newLineValue = upperLine.concat(line);
    removeLine(lineIndex);
    setLine(lineIndex - 1, newLineValue);
    yShifter.backward(1);
    xShifter.cursor(upperLineLength);
    updateShifters();
    repaint();
    postTextChangeEvent();
  }

  protected void performCharInput(char c) {
    if(!charFilter.isAccept(c)) {
      return;
    }
    int lineIndex = cursorLine();
    TermString line = getLine(lineIndex);
    TermStringBuilder builder = TermString.builder(line);
    builder.insert(cursorChar(), String.valueOf(c));
    TermString content = builder.build();
    setLine(lineIndex, content);
    updateShifters();
    performMoveRight();
    postTextChangeEvent();
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    if(event.state() == State.CONTROL) {
      return;
    }
    updateViewSize();
    switch (event.key()) {
      case KEY_BACKSPACE -> {
        performBackspace();
        event.intercept(true);
      }
      case KEY_ARROW_LEFT -> {
        performMoveLeft();
        event.intercept(true);
      }
      case KEY_ARROW_RIGHT -> {
        performMoveRight();
        event.intercept(true);
      }
      case KEY_ARROW_UP -> {
        performMoveUp();
        event.intercept(true);
      }
      case KEY_ARROW_DOWN -> {
        performMoveDown();
        event.intercept(true);
      }
      case KEY_ENTER -> {
        performNewLine();
        event.intercept(true);
      }
      default -> {
        performCharInput(event.input());
        event.intercept(true);
      }
    }
  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);
    updateViewSize();
    if(event.action() == Action.PRESS && event.button() == Button.LEFT) {
      TermPos position = event.position();
      int x = position.x() - 1;
      int y = position.y() - 1;

      int bufX = xShifter.viewIndexToBufferIndex(x);
      int bufY = yShifter.viewIndexToBufferIndex(y);
      xShifter.cursor(bufX);
      yShifter.cursor(bufY);
      fixXShifter();
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_UP) {
      yShifter.shiftBackward(1);
      fixXShifter();
      repaint();
      return;
    }
    if(event.action() == Action.WHEEL_DOWN) {
      yShifter.shiftForward(1);
      fixXShifter();
      repaint();
    }
  }
}
