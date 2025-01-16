package net.jterminal.ui.component.selectable;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentResizeEvent;
import net.jterminal.ui.event.component.SelectableComponentKeyEvent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.graphics.TerminalState.CursorType;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.util.CharFilter;
import net.jterminal.util.CharFilter.CharType;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import static net.jterminal.input.Keyboard.*;

public class TextFieldComponent extends SelectableComponent implements Resizeable  {

  private static final CharFilter charFilter = new CharFilter(CharType.DIGIT,
      CharType.WHITESPACE, CharType.LETTERS_LOWERCASE,
      CharType.LETTERS_UPPERCASE, CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL);

  private String value;
  private final ViewShifter viewShifter = new ViewShifter(true);
  private CursorType cursorType = CursorType.BLINKING;

  public TextFieldComponent() {
    value("");
    backgroundColor(TerminalColor.WHITE);
    foregroundColor(TerminalColor.BLACK);
  }

  public @NotNull String value() {
    return value;
  }

  public void value(@NotNull String value) {
    this.value = value;
    viewShifter.bufferSize(value.length());
    cursor(value.length());
  }

  public void cursor(int cursor) {
    viewShifter.cursor(cursor);
    repaint();
  }

  public int cursor() {
    return viewShifter.cursor();
  }

  public @NotNull ViewShifter viewShifter() {
    return viewShifter;
  }

  public @NotNull String displayValue() {
    viewShifter.viewSize(currentDimension().width());
    int start = viewShifter.bufferStart();
    int end = viewShifter.bufferEnd();
    return value.substring(start, end);
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    TermDim dim = currentDimension();
    graphics.fillRect(1, 1, dim.width(), 1, CellData.EMPTY_SYMBOL);
    graphics.drawString(1, 1, displayValue());
  }

  protected void performBackspace() {
    viewShifter.viewSize(currentDimension().width());
    if(viewShifter.cursor() <= 0 || value.isEmpty()) {
      return;
    }
    StringBuilder stringBuilder = new StringBuilder(value);
    stringBuilder.deleteCharAt(viewShifter.cursor() - 1);
    this.value = stringBuilder.toString();
    viewShifter.bufferSize(value.length());
    performMoveLeft();
  }

  protected boolean performMoveLeft() {
    viewShifter.viewSize(currentDimension().width());
    viewShifter.backward(1);
    repaint();
    return true;
  }

  protected boolean performMoveRight() {
    viewShifter.viewSize(currentDimension().width());
    viewShifter.forward(1);
    repaint();
    return true;
  }

  protected boolean performCharInput(char c) {
    if(!charFilter.isAccept(c)) {
      return false;
    }
    StringBuilder stringBuilder = new StringBuilder(value);
    stringBuilder.insert(viewShifter.cursor(), c);
    value = stringBuilder.toString();
    int len = currentDimension().width();
    viewShifter.viewSize(currentDimension().width());
    viewShifter.bufferSize(value.length());
    if(!viewShifter.cursorAtEnd() && viewShifter.viewLesserThanBuffer()) {
      viewShifter.shiftBackward(1);
    }

    performMoveRight();
    return true;
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {

  }

  @Override
  public void processMouseEvent(@NotNull ComponentMouseEvent event) {
    super.processMouseEvent(event);
    if (event.action() != Action.PRESS || event.button() != Button.LEFT) {
      return;
    }
    TermPos position = event.position();
    viewShifter.viewSize(currentDimension().width());
    int cursor = viewShifter.viewIndexToBufferIndex(position.x() - 1);
    viewShifter.cursor(cursor);
    repaint();
  }

  @Override
  public void processKeyEvent(@NotNull SelectableComponentKeyEvent event) {
    super.processKeyEvent(event);
    switch (event.key()) {
      case KEY_BACKSPACE -> {
        performBackspace();
        event.interceptInput(true);
      }
      case KEY_ARROW_LEFT -> {
        if(performMoveLeft()) {
          event.interceptInput(true);
        }
      }
      case KEY_ARROW_RIGHT -> {
        if(performMoveRight()) {
          event.interceptInput(true);
        }
      }
      default -> {
        if(performCharInput(event.input())) {
          event.interceptInput(true);
        }
      }
    }
  }

  @Override
  public void updateState(@NotNull TerminalState terminalState) {
    viewShifter.viewSize(currentDimension().width());
    int cursor = viewShifter.viewCursor();

    terminalState.cursorPosition(new TermPos(cursor + 1, 1));
    terminalState.cursorType(CursorType.BLINKING);
  }

  @Override
  public void processResizeEvent(@NotNull ComponentResizeEvent event) {
    value(value);
  }

  @Deprecated
  @Override
  public void height(int value, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }

  @Deprecated
  @Override
  public void height(DimensionValue positionValue, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }

  public @NotNull CursorType cursorType() {
    return cursorType;
  }

  public void cursorType(@NotNull CursorType cursorType) {
    this.cursorType = cursorType;
    repaint();
  }
}
