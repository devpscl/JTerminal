package net.jterminal.text.termstring;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import net.jterminal.text.Combiner;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class IndexedStyleDataImpl implements IndexedStyleData {

  private final SortedMap<Integer, TextStyle> map;

  public IndexedStyleDataImpl() {
    this.map = new TreeMap<>();
  }

  public IndexedStyleDataImpl(@NotNull SortedMap<Integer, TextStyle> map) {
    this.map = map;
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
    return map.get(index).clone();
  }

  @Override
  public int lastIndex() {
    return map.lastKey();
  }

  @Override
  public void add(int index, @NotNull TextStyle textStyle) {
    TextStyle current = map.getOrDefault(index, TextStyle.create());
    map.put(index, Combiner.combine(textStyle, current));
  }

  @Override
  public void set(int index, @Nullable TextStyle textStyle) {
    set(index, textStyle, false);
  }

  @Override
  public void set(int index, @Nullable TextStyle textStyle, boolean explicitStyle) {
    if(textStyle == null) {
      if(explicitStyle) {
        map.put(index, TextStyle.getDefault());
        return;
      }
      unset(index);
      return;
    }
    if(explicitStyle) {
      textStyle = textStyle.asExplicitStyle();
    }
    map.put(index, textStyle);
  }

  @Override
  public void unset(int index) {
    map.remove(index);
  }

  @Override
  public @NotNull IndexedStyleData shift(int offset) {
    SortedMap<Integer, TextStyle> newMap = new TreeMap<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      newMap.put(Math.max(0, entry.getKey() + offset), entry.getValue());
    }
    return new IndexedStyleDataImpl(newMap);
  }

  @Override
  public @NotNull IndexedStyleData sub(int start, int end) {
    if(start == -1) {
      start = 0;
    }
    if(end == -1) {
      end = Integer.MAX_VALUE;
    }
    IndexedStyleData newData = new IndexedStyleDataImpl(new TreeMap<>());
    final TextStyle styleAtStart = at(start);
    newData.add(start, styleAtStart);
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      final int index = entry.getKey();
      if(index >= start && index < end) {
        TextStyle textStyle = entry.getValue();
        newData.add(Math.max(0, index), textStyle);
      }
    }
    return newData;
  }

  @Override
  public void mix(@NotNull IndexedStyleData indexedStyleData) {
    IndexedStyleDataImpl impl = (IndexedStyleDataImpl) indexedStyleData;
    for (Entry<Integer, TextStyle> entry : impl.map.entrySet()) {
      add(entry.getKey(), entry.getValue());
    }
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public void assign(@NotNull IndexedStyleData indexedStyleData, boolean copy) {
    IndexedStyleDataImpl impl = (IndexedStyleDataImpl) indexedStyleData;
    for (Entry<Integer, TextStyle> entry : impl.map.entrySet()) {
      map.put(entry.getKey(), copy ? entry.getValue().clone() : entry.getValue());
    }
  }

  @Override
  public @NotNull IndexedStyleData clone() {
    SortedMap<Integer, TextStyle> newMap = new TreeMap<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      newMap.put(entry.getKey(), entry.getValue().clone());
    }
    return new IndexedStyleDataImpl(newMap);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    IndexedStyleDataImpl impl = (IndexedStyleDataImpl) o;
    Set<Entry<Integer, TextStyle>> first = map.entrySet();
    Set<Entry<Integer, TextStyle>> second = impl.map.entrySet();
    if(first.size() != second.size()) {
      return false;
    }
    Iterator<Entry<Integer, TextStyle>> iteratorFirst = first.iterator();
    Iterator<Entry<Integer, TextStyle>> iteratorSecond = second.iterator();
    while (iteratorFirst.hasNext() && iteratorSecond.hasNext()) {
      Entry<Integer, TextStyle> firstEntry = iteratorFirst.next();
      Entry<Integer, TextStyle> secondEntry = iteratorSecond.next();
      if(!Objects.equals(firstEntry.getKey(), secondEntry.getKey())) {
        return false;
      }
      if(!Objects.equals(firstEntry.getValue(), secondEntry.getValue())) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(map);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("IndexedStyleDataImpl{");
    List<String> strings = new ArrayList<>();
    for (Entry<Integer, TextStyle> entry : map.entrySet()) {
      strings.add("[" + entry.getKey() + "]:" + entry.getValue());
    }
    sb.append(String.join("; ", strings));
    return sb.append("}").toString();
  }
}
