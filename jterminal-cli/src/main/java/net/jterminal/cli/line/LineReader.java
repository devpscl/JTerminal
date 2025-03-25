package net.jterminal.cli.line;

import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.tab.TabCompleter;
import net.jterminal.cli.tab.TabCompletion;
import net.jterminal.util.CharFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LineReader {

  int FLAG_INSERT_MODE      = 0x1;
  int FLAG_ECHO_MODE        = 0x2;
  int FLAG_QUICK_DELETE     = 0x4;
  int FLAG_KEEP_LINE        = 0x8;
  int FLAG_NAVIGABLE_CURSOR = 0x10;

  int DEFAULT_FLAGS = FLAG_ECHO_MODE | FLAG_NAVIGABLE_CURSOR
      | FLAG_INSERT_MODE | FLAG_KEEP_LINE;

  @Nullable TabCompletion tabCompletion();

  boolean isTabbing();

  @Nullable TabCompleter tabCompleter();

  void tabCompleter(@Nullable TabCompleter tabCompleter);

  @Nullable InputHistory inputHistory();

  void inputHistory(@Nullable InputHistory inputHistory);

  @NotNull CharFilter charFilter();

  void charFilter(@NotNull CharFilter charFilter);

  int flags();

  void flags(int flags);

  void addFlags(int flags);

  void removeFlags(int flags);

  int cursor();

  @NotNull LineRenderer lineRenderer();

  void lineRenderer(@NotNull LineRenderer lineRenderer);

  @NotNull String input();

  @NotNull String displayingInput();

  void input(@NotNull String input);

  void clear();

  void releaseLine(boolean event);

  void updateDisplay();

  @NotNull String readLine() throws InterruptedException;

  @Nullable String readLine(long timeout) throws InterruptedException;

}
