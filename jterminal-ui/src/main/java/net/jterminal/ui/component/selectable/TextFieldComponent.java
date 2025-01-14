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
  private int cursor;
  private int cursorOffset;
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
    cursor(value.length());
  }

  public void cursor(int cursor) {
    this.cursor = cursor;
    this.cursorOffset = 0;
    repaint();
  }

  public int cursor() {
    return cursor;
  }

  public @NotNull String displayValue() {
    int len = currentDimension().width();
    if(this.value.length() <= len) {
      return value;
    }
    int start = Math.max(0, value.length() - len - cursorOffset);
    int end = value.length() - cursorOffset;
    return value.substring(start, end);
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    TermDim dim = currentDimension();
    graphics.fillRect(1, 1, dim.width(), 1, CellData.EMPTY_SYMBOL);
    graphics.drawString(1, 1, displayValue());
  }

  protected void performBackspace() {
    if(cursor <= 0 || value.isEmpty()) {
      return;
    }
    StringBuilder stringBuilder = new StringBuilder(value);
    stringBuilder.deleteCharAt(cursor - 1);
    this.value = stringBuilder.toString();
    performMoveLeft();
  }

  protected boolean performMoveLeft() {
    if(cursor <= 0) {
      return false;
    }
    cursor--;
    int len = currentDimension().width();
    if(this.value.length() <= len) {
      cursorOffset = 0;
      repaint();
      return true;
    }
    int start = value.length() - len - cursorOffset;
    if(cursor < start) {
      cursorOffset++;
    }
    repaint();
    return true;
  }

  protected boolean performMoveRight() {
    if(cursor >= value.length()) {
      return false;
    }
    cursor++;
    int end = value.length() - cursorOffset;
    if(cursor > end && cursorOffset > 0) {
      cursorOffset--;
    }
    repaint();
    return true;
  }

  protected boolean performCharInput(char c) {
    if(!charFilter.isAccept(c)) {
      return false;
    }
    StringBuilder stringBuilder = new StringBuilder(value);
    stringBuilder.insert(cursor, c);
    value = stringBuilder.toString();
    int len = currentDimension().width();
    if(cursor < value.length() && len < value.length()) {
      cursorOffset++;
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
    int len = currentDimension().width();
    int start = Math.max(0, value.length() - len - cursorOffset);
    cursor = Math.min(start + position.x() - 1, value.length());
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
    int len = currentDimension().width();
    int start = Math.max(0, value.length() - len - cursorOffset);

    terminalState.cursorPosition(new TermPos(cursor + 1 - start, 1));
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
