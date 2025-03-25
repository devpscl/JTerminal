package net.jterminal.cli.line;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.tab.TabCompleter;
import net.jterminal.cli.tab.TabCompletion;
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
  private int flags = DEFAULT_FLAGS;
  private CharFilter charFilter = new CharFilter(CharType.DIGIT,
      CharType.WHITESPACE, CharType.LETTERS_LOWERCASE,
      CharType.LETTERS_UPPERCASE, CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL);
  private LineRenderer lineRenderer = new DefaultLineRenderer();

  int cursor = 0;
  InputHistory inputHistory = null;
  TabCompletion tabCompletion = null;
  TabCompleter tabCompleter = null;
  boolean tabbing = false;

  private final ReentrantLock lineReadLock = new ReentrantLock();
  private final Condition condition = lineReadLock.newCondition();
  private final AtomicReference<String> lastReleasedLine = new AtomicReference<>();

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
  public @Nullable TabCompletion tabCompletion() {
    return tabCompletion;
  }

  protected void tabCompletion(@Nullable TabCompletion tabCompletion) {
    this.tabCompletion = tabCompletion;
    this.tabbing = false;
  }

  public @Nullable TabCompleter tabCompleter() {
    return tabCompleter;
  }

  public void tabCompleter(@Nullable TabCompleter tabCompleter) {
    this.tabCompleter = tabCompleter;
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
      lineReadLock.lock();
      try {
        String str = input();
        lastReleasedLine.set(str);
        condition.signal();
        boolean strBlank = str.isBlank();
        if (!strBlank && inputHistory != null) {
          inputHistory.add(str);
        }
        if(event && terminalSetLock.isSet()) {
          CLITerminal terminal = terminalSetLock.get();
          terminal.lineReading(false);
          terminal.eventReleaseLine(str);
          if(!strBlank) {
            terminal.dispatchCommand(str);
          }
          terminal.lineReading(true);
        }
        clear();
      } finally {
        lineReadLock.unlock();
      }
    }
  }

  @Override
  public void updateDisplay() {
    if(terminalSetLock.isSet()) {
      terminalSetLock.get().updateLine();
    }
  }

  @Override
  public @NotNull String readLine() throws InterruptedException {
    lineReadLock.lock();
    try {
      condition.await();
      return lastReleasedLine.get();
    } finally {
      lineReadLock.unlock();
    }
  }

  @Override
  public @Nullable String readLine(long timeout) throws InterruptedException {
    lineReadLock.lock();
    try {
      if(!condition.await(timeout, TimeUnit.MILLISECONDS)) {
        return null;
      }
      return lastReleasedLine.get();
    } finally {
      lineReadLock.unlock();
    }
  }

  protected void insertTabSuggestion() {
    if(tabCompletion == null) {
      return;
    }
    if(input.length() < tabCompletion.position()) {
      return;
    }
    String newInput = input()
        .substring(0, tabCompletion.position())
        .concat(tabCompletion.suggestion());
    setEditingInput(newInput);
  }

  protected void setEditingInput(@NotNull String str) {
    input.setLength(0);
    input.append(str);
    cursor = str.length();
  }

  protected void performTabEvent() {
    if(tabbing) {
      if(tabCompletion == null) {
        return;
      }
      tabCompletion.next();
      insertTabSuggestion();
      return;
    }
    SetLock<CLITerminal> tsl = terminalSetLock;
    if(!tsl.isSet()) {
      return;
    }
    CLITerminal terminal = tsl.get();
    TabCompletion completion = tabCompleter == null ? null : tabCompleter.generate(input(), cursor);
    if(completion == null || completion.empty()) {
      return;
    }
    tabCompletion = completion;
    insertTabSuggestion();
    tabbing = true;
  }

  protected void performNonTabEvent() {
    tabCompletion(null);
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
