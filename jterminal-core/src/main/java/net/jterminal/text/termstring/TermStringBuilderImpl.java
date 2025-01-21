package net.jterminal.text.termstring;

import java.util.Collection;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.style.TextStyle.FontOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TermStringBuilderImpl implements UnsafeTermStringBuilder {

  private final StringBuilder sb;
  private final IndexedStyleData data;

  public TermStringBuilderImpl(@NotNull String value, @NotNull IndexedStyleData data) {
    this(new StringBuilder(value), data);
  }

  public TermStringBuilderImpl(@NotNull StringBuilder stringBuilder,
      @NotNull IndexedStyleData indexedStyleData) {
    sb = stringBuilder;
    data = indexedStyleData;
  }

  @Override
  public @NotNull TermStringBuilder append(@NotNull String str) {
    sb.append(str);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(char x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(short x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(int x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(long x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(float x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(double x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(boolean x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(Object x) {
    sb.append(x);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder append(TermString termString) {
    int length = sb.length();
    sb.append(termString.raw());
    data.mix(termString.data().shift(length));
    return this;
  }

  @Override
  public @NotNull TermStringBuilder foregroundColor(@Nullable ForegroundColor foregroundColor) {
    return appendStyle(TextStyle.create(foregroundColor == null ?
        TerminalColor.DEFAULT : foregroundColor, null));
  }

  @Override
  public @NotNull TermStringBuilder backgroundColor(@Nullable BackgroundColor backgroundColor) {
    return appendStyle(TextStyle.create(null,
        backgroundColor == null ? TerminalColor.DEFAULT : backgroundColor));
  }

  @Override
  public @NotNull TermStringBuilder fonts(TextFont... fonts) {
    return appendStyle(TextStyle.create()
        .font(fonts));
  }

  @Override
  public @NotNull TermStringBuilder fontsSet(TextFont... fonts) {
    return appendStyle(TextStyle.create()
        .font(FontOption.SET, fonts));
  }

  @Override
  public @NotNull TermStringBuilder fontsUnset(TextFont... fonts) {
    return appendStyle(TextStyle.create()
        .font(FontOption.UNSET, fonts));
  }

  @Override
  public @NotNull TermStringBuilder fonts(Collection<TextFont> fonts) {
    return appendStyle(TextStyle.create()
        .font(fonts));
  }

  @Override
  public @NotNull TermStringBuilder fontsSet(Collection<TextFont> fonts) {
    return appendStyle(TextStyle.create()
        .font(FontOption.SET, fonts));
  }

  @Override
  public @NotNull TermStringBuilder fontsUnset(Collection<TextFont> fonts) {
    return appendStyle(TextStyle.create()
        .font(FontOption.UNSET, fonts));
  }


  @Override
  public @NotNull TermStringBuilder insert(int index, String x) {
    if(x == null) {
      x = "null";
    }
    sb.insert(index, x);
    IndexedStyleData prefix = data.sub(-1, index);
    IndexedStyleData suffix = data.sub(index, -1).shift(x.length());
    prefix.mix(suffix);
    data.clear();
    data.assign(prefix, false);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder insert(int index, TermString termString) {
    if(termString == null) {
      return insert(index, (String) null);
    }
    String value = termString.raw();
    sb.insert(index, value);
    IndexedStyleData prefix = data.sub(-1, index);
    IndexedStyleData insertData = termString.data().shift(index);
    IndexedStyleData suffix = data.sub(index, -1).shift(value.length());
    prefix.mix(insertData);
    prefix.mix(suffix);
    data.clear();
    data.assign(prefix, false);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder insertStyle(int index,
      @NotNull TextStyle textStyle, boolean explicit) {
    data.add(index, explicit ? textStyle.asExplicitStyle() : textStyle);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder region(int start, int end) {
    String str = sb.substring(start, end);
    sb.setLength(0);
    sb.append(str);
    IndexedStyleData newData = data.sub(start, end).shift(-start);
    data.clear();
    data.assign(newData, false);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder erase(int start, int end) {
    return replace(start, end, "");
  }

  @Override
  public @NotNull TermStringBuilder replace(int start, int end, String str) {
    if(str == null) {
      str = "null";
    }
    sb.replace(start, end, str);
    IndexedStyleData prefix = data.sub(-1, start);
    IndexedStyleData suffix = data.sub(end, -1).shift(-(end-start) + str.length());
    prefix.mix(suffix);
    data.clear();
    data.assign(prefix, false);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder replace(int start, int end, TermString termString) {
    if(termString == null) {
      return replace(start, end, (String) null);
    }
    String value = termString.raw();
    sb.replace(start, end, value);

    IndexedStyleData prefix = data.sub(-1, start);
    IndexedStyleData insertData = termString.data().shift(start);
    IndexedStyleData suffix = data.sub(end, -1).shift(-(end-start) + value.length());
    prefix.mix(insertData);
    prefix.mix(suffix);
    data.clear();
    data.assign(prefix, false);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder deleteAt(int index) {
    sb.deleteCharAt(index);
    IndexedStyleData prefix = data.sub(-1, index);
    IndexedStyleData suffix = data.sub(index + 1, -1).shift(-1);
    prefix.mix(suffix);
    data.clear();
    data.assign(prefix, false);
    return this;
  }

  @Override
  public @NotNull TextStyle getStyle(int pos) {
    return data.at(pos);
  }

  @Override
  public @NotNull TermStringBuilder appendStyle(@NotNull TextStyle textStyle) {
    data.add(sb.length(), textStyle);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder appendExplicitStyle(@NotNull TextStyle textStyle) {
    data.set(sb.length(), textStyle, true);
    return this;
  }

  @Override
  public @NotNull TermStringBuilder resetStyle() {
    data.set(sb.length(), TextStyle.getDefault());
    return this;
  }

  @Override
  public @NotNull TermString build() {
    return new TermStringImpl(sb.toString(), data.copy());
  }

  public @NotNull TermString getNoCopy() {
    return new TermStringImpl(sb.toString(), data);
  }

  @Override
  @NotNull
  public IndexedStyleData data() {
    return data;
  }

  @Override
  public @NotNull StringBuilder stringBuilder() {
    return sb;
  }

}
