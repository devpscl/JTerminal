package net.jterminal.cli.line;

import java.util.Objects;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.tab.TabCompleter;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.util.CharFilter;
import net.jterminal.util.CharFilter.CharType;
import net.jterminal.util.SetLock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.jterminal.input.Keyboard.*;

public class DefaultLineReader implements LineReader, InternalLineReader {

  private StringBuilder input = new StringBuilder();
  private String inputCache = "";
  private int flags = FLAG_ECHO_MODE | FLAG_NAVIGABLE_CURSOR | FLAG_INSERT_MODE;
  private CharFilter charFilter = new CharFilter(CharType.DIGIT,
      CharType.WHITESPACE, CharType.LETTERS_LOWERCASE,
      CharType.LETTERS_UPPERCASE, CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL);
  private LineRenderer lineRenderer = new DefaultLineRenderer();

  int cursor = 0;
  InputHistory inputHistory = null;
  TabCompleter tabCompleter = null;
  boolean tabbing = false;

  protected final SetLock<CLITerminal> terminalSetLock = new SetLock<>("Terminal");

  protected final Object syncLock = new Object();

  @Override
  public int flags() {
    return flags;
  }

  @Override
  public void flags(int flags) {
    this.flags = flags;
  }

  @Override
  public void addFlags(int flags) {
    flags(this.flags | flags);
  }

  @Override
  public void removeFlags(int flags) {
    flags(this.flags & ~flags);
  }

  @Override
  public @Nullable TabCompleter tabCompleter() {
    return tabCompleter;
  }

  @Override
  public void tabCompleter(@Nullable TabCompleter tabCompleter) {
    this.tabCompleter = tabCompleter;
    this.tabbing = false;
  }

  @Override
  public boolean isTabbing() {
    return tabbing;
  }

  @Override
  public @Nullable InputHistory inputHistory() {
    return inputHistory;
  }

  @Override
  public void inputHistory(@Nullable InputHistory inputHistory) {
    synchronized (syncLock) {
      this.inputHistory = inputHistory;
    }
  }

  @Override
  @NotNull
  public CharFilter charFilter() {
    return charFilter;
  }

  @Override
  public void charFilter(@NotNull CharFilter charFilter) {
    this.charFilter = charFilter;
  }

  @Override
  public int cursor() {
    return Math.max(0, Math.min(input.length(), cursor));
  }

  @Override
  public @NotNull LineRenderer lineRenderer() {
    return lineRenderer;
  }

  @Override
  public void lineRenderer(@NotNull LineRenderer lineRenderer) {
    this.lineRenderer = lineRenderer;
  }

  @Override
  public @NotNull String input() {
    return input.toString();
  }

  @Override
  public @NotNull String displayingInput() {
    if((flags & FLAG_ECHO_MODE) == FLAG_ECHO_MODE) {
      return input();
    }
    return "";
  }

  @Override
  public void input(@NotNull String input) {
    synchronized (syncLock) {
      this.input = new StringBuilder(input);
      this.cursor = input.length();
      performEditEvent();
    }
  }

  @Override
  public void clear() {
    input("");
  }

  @Override
  public void releaseLine(boolean event) {
    synchronized (syncLock) {
      String str = input();
      boolean strBlank = str.isBlank();
      if (!strBlank) {
        inputHistory.add(str);
      }
      if(event && terminalSetLock.isSet()) {
        CLITerminal terminal = terminalSetLock.get();
        terminal.eventReleaseLine(str);
        if(!strBlank) {
          terminal.lineReading(false);
          terminal.dispatchCommand(str);
          terminal.lineReading(true);
        }

      }
      clear();
    }
  }

  @Override
  public void updateDisplay() {
    if(terminalSetLock.isSet()) {
      terminalSetLock.get().updateLine();
    }
  }

  protected void insertTabSuggestion() {
    if(tabCompleter == null) {
      return;
    }
    String newInput = input()
        .substring(0, tabCompleter.position())
        .concat(tabCompleter.suggestion());
    setEditingInput(newInput);
  }

  protected void setEditingInput(@NotNull String str) {
    input.setLength(0);
    input.append(str);
    cursor = str.length();
  }

  protected void performTabEvent() {
    if(tabbing) {
      if(tabCompleter == null) {
        return;
      }
      tabCompleter.next();
      insertTabSuggestion();
      return;
    }
    SetLock<CLITerminal> tsl = terminalSetLock;
    if(!tsl.isSet()) {
      return;
    }

    CLITerminal terminal = tsl.get();
    TabCompleter completer = terminal.generateTabCompleter(input(), cursor);
    if(completer == null || completer.empty()) {
      return;
    }
    tabCompleter = completer;
    insertTabSuggestion();
    tabbing = true;
  }

  protected void performNonTabEvent() {
    tabCompleter(null);
  }

  protected void performEditEvent() {
    inputCache = input.toString();
    if(inputHistory != null) {
      inputHistory.unselect();
    }
    performNonTabEvent();
  }

  protected void performHistoryGoUp() {
    performNonTabEvent();
    if(inputHistory == null || inputHistory.empty()) {
      return;
    }
    if(!inputHistory.selectPrevHistory()) {
      return;
    }
    int historyCursor = inputHistory.cursor();
    String value = inputHistory.get(historyCursor);
    setEditingInput(Objects.requireNonNull(value));
  }

  protected void performHistoryGoDown() {
    performNonTabEvent();
    if(inputHistory == null || inputHistory.empty()) {
      return;
    }
    if(!inputHistory.selectNextHistory()) {
      return;
    }
    if(inputHistory.isAnySelected()) {
      int historyCursor = inputHistory.cursor();
      String value = inputHistory.get(historyCursor);
      setEditingInput(Objects.requireNonNull(value));
    } else {
      setEditingInput(inputCache);
    }

  }

  protected void performCharInput(char inputChar) {
    if(charFilter.isAccept(inputChar)) {
      if((flags & FLAG_INSERT_MODE) == FLAG_INSERT_MODE) {
        input.insert(cursor++, inputChar);
      } else {
        if(cursor >= input.length()) {
          input.append(inputChar);
        } else {
          input.setCharAt(cursor, inputChar);
        }
        cursor++;
      }
      performEditEvent();
    }
  }

  @Override
  public @NotNull SetLock<CLITerminal> getSetLock() {
    return terminalSetLock;
  }

  @Override
  public void handleKeyboardEvent(@NotNull KeyboardInputEvent inputEvent) {
    synchronized (syncLock) {
      switch (inputEvent.key()) {
        case KEY_ARROW_UP -> performHistoryGoUp();
        case KEY_ARROW_DOWN -> performHistoryGoDown();
        case KEY_ARROW_LEFT -> {
          if(cursor > 0 && (flags & FLAG_NAVIGABLE_CURSOR) == FLAG_NAVIGABLE_CURSOR) {
            cursor--;
          }
          performNonTabEvent();
        }
        case KEY_ARROW_RIGHT -> {
          if(cursor < input.length() && (flags & FLAG_NAVIGABLE_CURSOR) == FLAG_NAVIGABLE_CURSOR) {
            cursor++;
          }
          performNonTabEvent();
        }
        case KEY_BACKSPACE -> {
          if(cursor <= 0) {
            break;
          }
          if((flags & FLAG_QUICK_DELETE) == FLAG_QUICK_DELETE) {
            input.delete(0, input.length());
            cursor = 0;
            break;
          }
          input.deleteCharAt(cursor - 1);
          cursor--;
          performEditEvent();
        }
        case KEY_DELETE -> {
          if(cursor >= input.length()) {
            break;
          }
          input.deleteCharAt(cursor);
          performEditEvent();
        }
        case KEY_ENTER -> {
          releaseLine(true);
          performNonTabEvent();
        }
        case KEY_TAB -> performTabEvent();
        default -> {
          final char inputChar = inputEvent.input();
          performCharInput(inputChar);
        }
      }
    }
  }
}
