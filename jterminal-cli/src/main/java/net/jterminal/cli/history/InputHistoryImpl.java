package net.jterminal.cli.history;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputHistoryImpl implements InputHistory {

  private int cursor;
  private final List<String> history;
  private final boolean allowMultipleInputs;

  public InputHistoryImpl(int cursor, List<String> history, boolean allowMultipleInputs) {
    this.cursor = cursor;
    this.history = history;
    this.allowMultipleInputs = allowMultipleInputs;
  }

  @Override
  public void add(@NotNull String input) {
    synchronized (history) {
      if(!allowMultipleInputs) {
        history.removeIf(input::equals);
      }
      history.add(input);
      cursor = history.size();
    }
  }

  @Override
  public @Nullable String get(int index) {
    synchronized (history) {
      if(index < 0 || index >= history.size()) {
        return null;
      }
      return history.get(index);
    }
  }

  @Override
  public int cursor() {
    return cursor;
  }

  @Override
  public void cursor(int cursor) {
    synchronized (history) {
      if(cursor == -1) {
        this.cursor = history.size();
        return;
      }
      this.cursor = Math.max(0, Math.min(history.size(), cursor));
    }
  }

  @Override
  public boolean empty() {
    return count() == 0;
  }

  @Override
  public boolean selectPrevHistory() {
    if(cursor <= 0) {
      return false;
    }
    cursor(cursor - 1);
    return true;
  }

  @Override
  public boolean selectNextHistory() {
    if(cursor >= history.size()) {
      return false;
    }
    cursor(cursor + 1);
    return true;
  }

  @Override
  public boolean isAnySelected() {
    return cursor < history.size();
  }

  @Override
  public void unselect() {
    cursor = history.size();
  }

  @Override
  public int count() {
    return history.size();
  }

  @Override
  public void clear() {
    synchronized (history) {
      history.clear();
      cursor = 0;
    }
  }

  @Override
  public @NotNull InputHistory copy() {
    return copy(allowMultipleInputs);
  }

  @Override
  public @NotNull InputHistory copy(boolean allowMultipleInputs) {
    return new InputHistoryImpl(cursor, new ArrayList<>(history), allowMultipleInputs);
  }

  @Override
  public @NotNull Iterator<String> iterator() {
    synchronized (history) {
      return new ArrayList<>(history).iterator();
    }
  }
}
