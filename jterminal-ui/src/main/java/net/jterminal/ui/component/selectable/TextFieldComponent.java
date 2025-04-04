package net.jterminal.ui.component.selectable;

import net.jterminal.input.Mouse.Action;
import net.jterminal.input.Mouse.Button;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.component.ComponentMouseEvent;
import net.jterminal.ui.event.component.ComponentResizeEvent;
import net.jterminal.ui.event.special.TextChangedEvent;
import net.jterminal.ui.graphics.CellData;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.TerminalState;
import net.jterminal.ui.graphics.TerminalState.CursorType;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import net.jterminal.ui.util.MathUtil;
import net.jterminal.ui.util.ViewShifter;
import net.jterminal.ui.util.ViewShifter.Type;
import net.jterminal.util.CharFilter;
import net.jterminal.util.CharFilter.CharType;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;
import static net.jterminal.input.Keyboard.*;

public class TextFieldComponent extends SelectableComponent implements Resizeable  {

  protected static final CharFilter charFilter = new CharFilter(CharType.DIGIT,
      CharType.WHITESPACE, CharType.LETTERS_LOWERCASE,
      CharType.LETTERS_UPPERCASE, CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL);

  protected String value;
  protected final ViewShifter viewShifter = new ViewShifter(Type.POINTER_SHIFTER);
  protected CursorType cursorType = CursorType.BLINKING;
  protected int limitLength = Integer.MAX_VALUE;

  public TextFieldComponent(@NotNull String value) {
    value(value);
    backgroundColor(TerminalColor.GRAY);
    foregroundColor(TerminalColor.BLACK);
  }

  public TextFieldComponent() {
    this("");
  }

  public @NotNull String value() {
    return value;
  }

  public void value(@NotNull String value) {
    synchronized (lock) {
      this.value = value;
      viewShifter.bufferSize(value.length());
      cursor(value.length());
    }
  }

  public void limitLength(int limit) {
    limitLength = MathUtil.nonNegative(limit);
  }

  public int limitLength() {
    return limitLength;
  }

  public void cursor(int cursor) {
    synchronized (lock) {
      viewShifter.cursor(cursor);
      repaint();
    }
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

  protected void postTextChangeEvent() {
    if(eventBus.isListenedTo(TextChangedEvent.class)) {
      TermString text = TermString.value(value);
      eventBus.post(new TextChangedEvent(text));
    }
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
    postTextChangeEvent();
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
    if(viewShifter.bufferSize() >= limitLength) {
      return false;
    }
    StringBuilder stringBuilder = new StringBuilder(value);
    stringBuilder.insert(viewShifter.cursor(), c);
    value = stringBuilder.toString();
    int len = currentDimension().width();
    viewShifter.viewSize(currentDimension().width());
    viewShifter.bufferSize(value.length());
    performMoveRight();
    postTextChangeEvent();
    return true;
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
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(!isSelected()) {
      return;
    }
    switch (event.key()) {
      case KEY_BACKSPACE -> {
        performBackspace();
        event.intercept(true);
      }
      case KEY_ARROW_LEFT -> {
        if(performMoveLeft()) {
          event.intercept(true);
        }
      }
      case KEY_ARROW_RIGHT -> {
        if(performMoveRight()) {
          event.intercept(true);
        }
      }
      default -> {
        if(performCharInput(event.input())) {
          event.intercept(true);
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
