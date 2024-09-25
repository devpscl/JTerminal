package net.jterminal.text.termstring;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import net.jterminal.text.Combiner;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class IndexedStyleDataImpl implements IndexedStyleData {

  private SortedMap<Integer, TextStyle> map;

  public IndexedStyleDataImpl() {
    this.map = new TreeMap<>();
  }

  public IndexedStyleDataImpl(@NotNull SortedMap<Integer, TextStyle> map) {
    this.map = map;
  }

  void moveTo(@NotNull SortedMap<Integer, TextStyle> anotherMap, int start, int end,
      int shiftOffset) {
    for (Entry<Integer, TextStyle> entry : anotherMap.entrySet()) {
      final int index = entry.getKey();
      if(index >= start && index <= end) {
        anotherMap.put(index + shiftOffset, entry.getValue());
      }
    }
  }

  @Override
  public @NotNull TextStyle at(int index) {
    TextStyle textStyle = TextStyle.create();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      int entryIndex = entry.getKey();
      if(entryIndex <= index) {
        textStyle = Combiner.combine(entry.getValue(), textStyle);
      }
    }
    return textStyle;
  }

  @Override
  public @NotNull List<IndexEntry> indexes() {
    return indexes(false);
  }

  @Override
  public @NotNull List<IndexEntry> indexes(boolean copy) {
    List<IndexEntry> entries = new ArrayList<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      entries.add(new IndexEntry(entry.getKey(),
          copy ? entry.getValue().clone() : entry.getValue()));
    }
    return entries;
  }

  @Override
  public @Nullable TextStyle get(int index) {
    return map.get(index);
  }

  @Override
  public int lastIndex() {
    return map.lastKey();
  }

  @Override
  public void put(int index, @NotNull TextStyle textStyle) {
    TextStyle current = map.getOrDefault(index, TextStyle.create());
    map.put(index, Combiner.combine(textStyle, current));
  }

  @Override
  public void set(int index, @Nullable TextStyle textStyle) {
    if(textStyle == null) {
      unset(index);
      return;
    }
    map.put(index, textStyle);
  }

  @Override
  public void unset(int index) {
    map.remove(index);
  }

  @Override
  public void shift(int offset) {
    SortedMap<Integer, TextStyle> newMap = new TreeMap<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      newMap.put(Math.max(0, entry.getKey() + offset), entry.getValue());
    }
    map = newMap;
  }

  @Override
  public void insert(int index, @NotNull IndexedStyleData indexedStyleData) {
    SortedMap<Integer, TextStyle> newMap = new TreeMap<>();
    moveTo(newMap, 0, index, 0);



    map = newMap;
  }

  @Override
  public @NotNull IndexedStyleData clone() {
    SortedMap<Integer, TextStyle> newMap = new TreeMap<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      newMap.put(entry.getKey(), entry.getValue().clone());
    }
    return new IndexedStyleDataImpl(newMap);
  }
}
