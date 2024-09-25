package net.jterminal.text.termstring;

import java.util.List;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IndexedStyleData {

  @NotNull TextStyle at(int index);

  @NotNull List<IndexEntry> indexes();

  @NotNull List<IndexEntry> indexes(boolean copy);

  @Nullable TextStyle get(int index);

  int lastIndex();

  void put(int index, @NotNull TextStyle textStyle);

  void set(int index, @Nullable TextStyle textStyle);

  void unset(int index);

  void shift(int offset);

  void insert(int index, @NotNull IndexedStyleData indexedStyleData);

  @NotNull IndexedStyleData clone();

  record IndexEntry(int index, @NotNull TextStyle textStyle) {}

}
