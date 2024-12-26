package net.jterminal.cli.line;

import java.util.List;
import net.jterminal.cli.history.InputHistory;
import net.jterminal.cli.tab.TabCompleter;
import net.jterminal.util.CharFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface LineReader {

  int FLAG_INSERT_MODE      = 0x1;
  int FLAG_ECHO_MODE        = 0x2;
  int FLAG_QUICK_DELETE     = 0x4;
  int FLAG_PRINT_LEGACY     = 0x8;
  int FLAG_NAVIGABLE_CURSOR = 0x10;

  @Nullable TabCompleter tabCompleter();

  void tabCompleter(@Nullable TabCompleter tabCompleter);

  boolean isTabbing();

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

}
