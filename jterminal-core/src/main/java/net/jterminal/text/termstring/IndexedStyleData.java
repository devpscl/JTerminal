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

  void add(int index, @NotNull TextStyle textStyle);

  void set(int index, @Nullable TextStyle textStyle);

  void set(int index, @Nullable TextStyle textStyle, boolean explicitStyle);

  void unset(int index);

  @NotNull IndexedStyleData shift(int offset);

  @NotNull IndexedStyleData sub(int start, int end);

  void mix(@NotNull IndexedStyleData indexedStyleData);

  void clear();

  void assign(@NotNull IndexedStyleData indexedStyleData, boolean copy);

  @NotNull IndexedStyleData copy();

  record IndexEntry(int index, @NotNull TextStyle textStyle) {}

  static IndexedStyleData create() {
    return new IndexedStyleDataImpl();
  }

}
