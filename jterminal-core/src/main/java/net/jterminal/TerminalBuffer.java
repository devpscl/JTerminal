package net.jterminal;

import net.jterminal.ansi.AnsiCodeSerializer;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.Combiner;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.command.AnsiCommand;
import net.jterminal.text.command.CursorCommand;
import net.jterminal.text.command.ScreenCommand;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TerminalBuffer {

  private final StringBuffer stringBuffer;
  private TextStyle textStyle;

  public TerminalBuffer() {
    stringBuffer = new StringBuffer();
    textStyle = TextStyle.create();
  }

  public TerminalBuffer(@NotNull TerminalBuffer buffer) {
    stringBuffer = new StringBuffer(buffer.stringBuffer);
    textStyle = buffer.textStyle.clone();
  }

  public TerminalBuffer append(Object x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(byte x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(char x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(int x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(long x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(float x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(double x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(boolean x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer append(String x) {
    stringBuffer.append(x);
    return this;
  }

  public TerminalBuffer newLine() {
    stringBuffer.append("\n");
    return this;
  }

  public TerminalBuffer foregroundColor(@Nullable ForegroundColor foregroundColor) {
    textStyle.foregroundColor(foregroundColor);
    return appendStyle();
  }

  public TerminalBuffer backgroundColor(@Nullable BackgroundColor backgroundColor) {
    textStyle.backgroundColor(backgroundColor);
    return appendStyle();
  }

  public TerminalBuffer fonts(TextFont...fonts) {
    textStyle.font(fonts);
    return appendStyle();
  }

  public TerminalBuffer appendFonts(TextFont...fonts) {
    for (TextFont font : fonts) {
      textStyle.fontMap().put(font, true);
    }
    return appendStyle();
  }

  public TerminalBuffer removeFonts(TextFont...fonts) {
    for (TextFont font : fonts) {
      textStyle.fontMap().put(font, false);
    }
    return appendStyle();
  }

  public TerminalBuffer removeAllFonts() {
    return removeFonts(TextFont.values());
  }

  public TerminalBuffer setStyle(@NotNull TextStyle style) {
    textStyle = style;
    return appendStyle();
  }

  public TerminalBuffer resetStyle() {
    return setStyle(TextStyle.create());
  }

  public TerminalBuffer appendStyle(@NotNull TextStyle style) {
    textStyle = Combiner.combine(style, textStyle);
    return appendStyle();
  }

  public TerminalBuffer cursorColumn(int col) {
    return command(CursorCommand.column(col));
  }

  public TerminalBuffer cursorUp(int count) {
    return command(CursorCommand.moveUp(count));
  }

  public TerminalBuffer cursorDown(int count) {
    return command(CursorCommand.moveDown(count));
  }

  public TerminalBuffer cursorLeft(int count) {
    return command(CursorCommand.moveLeft(count));
  }

  public TerminalBuffer cursorRight(int count) {
    return command(CursorCommand.moveRight(count));
  }

  public TerminalBuffer cursor(@NotNull TerminalPosition pos) {
    return command(CursorCommand.move(pos));
  }

  public TerminalBuffer cursorSave() {
    return command(CursorCommand.savePosition());
  }

  public TerminalBuffer cursorRestore() {
    return command(CursorCommand.restorePosition());
  }

  public TerminalBuffer cursorLineUp(int count) {
    return command(CursorCommand.lineUp(count));
  }

  public TerminalBuffer cursorLineDown(int count) {
    return command(CursorCommand.lineDown(count));
  }

  public TerminalBuffer screenClear() {
    return command(ScreenCommand.clear());
  }

  public TerminalBuffer screenScrollUp() {
    return command(ScreenCommand.scrollUp());
  }

  public TerminalBuffer screenScrollDown() {
    return command(ScreenCommand.scrollDown());
  }

  public TerminalBuffer screenClearUp() {
    return command(ScreenCommand.clearUp());
  }

  public TerminalBuffer screenClearDown() {
    return command(ScreenCommand.clearDown());
  }

  public TerminalBuffer screenClearLeft() {
    return command(ScreenCommand.clearLeft());
  }

  public TerminalBuffer screenClearRight() {
    return command(ScreenCommand.clearRight());
  }

  public TerminalBuffer screenClearLine() {
    return command(ScreenCommand.clearLine());
  }

  public TerminalBuffer reset() {
    textStyle = TextStyle.create();
    stringBuffer.replace(0, stringBuffer.length(), "");
    return this;
  }

  public TerminalBuffer command(@NotNull AnsiCommand ansiCommand) {
    stringBuffer.append(ansiCommand.getAnsiCode());
    return this;
  }

  public int length() {
    return stringBuffer.length();
  }

  TerminalBuffer appendStyle() {
    String str = AnsiCodeSerializer.DEFAULT.serialize(textStyle);
    stringBuffer.append(str);
    return this;
  }

  @Override
  public String toString() {
    return stringBuffer.toString();
  }
}
