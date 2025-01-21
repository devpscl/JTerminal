package net.jterminal.text.termstring;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.jterminal.buffer.ByteBuf;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TermStringImpl implements TermString {

  private final String value;
  private final IndexedStyleData indexedStyleData;

  public TermStringImpl(@Nullable String x) {
    this(x, new IndexedStyleDataImpl());
  }

  public TermStringImpl(@Nullable String value, IndexedStyleData indexedStyleData) {
    this.value = value == null ? "null" : value;
    this.indexedStyleData = indexedStyleData == null ?
        new IndexedStyleDataImpl() : indexedStyleData;
  }

  @Override
  public @NotNull String raw() {
    return value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int index) {
    return value.charAt(index);
  }

  @Override
  public int indexOf(char c) {
    return value.indexOf(c);
  }

  @Override
  public int indexOf(String str) {
    return value.indexOf(str);
  }

  @Override
  public @NotNull TermString replace(char from, char to) {
    return new TermStringImpl(value.replace(from, to), indexedStyleData.copy());
  }

  @Override
  public @NotNull TermString replace(@NotNull String from, @NotNull String to) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    int index;
    while ((index = value.indexOf(from)) != -1) {
      int end = index + from.length();
      builder.replace(index, end, to);
    }
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString replace(@NotNull String from, @NotNull TermString to) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    int index;
    while ((index = value.indexOf(from)) != -1) {
      int end = index + from.length();
      builder.replace(index, end, to);
    }
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull String pattern, @NotNull String to) {
    return replaceAll(Pattern.compile(pattern), to);
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull String to) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    Matcher matcher = pattern.matcher(value);
    while(matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      builder.replace(start, end, to);
    }
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull String pattern, @NotNull TermString to) {
    return replaceAll(Pattern.compile(pattern), to);
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull TermString to) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    Matcher matcher = pattern.matcher(value);
    while(matcher.find()) {
      int start = matcher.start();
      int end = matcher.end();
      builder.replace(start, end, to);
    }
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString[] split(@NotNull String pattern) {
    return split(Pattern.compile(pattern));
  }

  @Override
  public @NotNull TermString[] split(@NotNull Pattern pattern) {
    List<TermString> strings = new ArrayList<>();
    Matcher matcher = pattern.matcher(value);
    int index = 0;
    int start;
    int end;
    while(matcher.find()) {
      start = matcher.start();
      end = matcher.end();
      strings.add(substring(index, start));
      index = end;
    }
    end = length();
    if(index < end) {
      strings.add(substring(index, end));
    }
    return strings.toArray(new TermString[0]);
  }

  @Override
  public @NotNull TermString[] split(char ch) {
    List<TermString> strings = new ArrayList<>();
    char[] charArray = value.toCharArray();

    int index = 0;
    int start;
    int end;
    for (int idx = 0; idx < charArray.length; idx++) {
      if(ch == charArray[idx]) {
        start = idx;
        end = start + 1;
        strings.add(substring(index, start));
        index = end;
      }
    }
    end = length();
    if(index < end) {
      strings.add(substring(index, end));
    }
    return strings.toArray(new TermString[0]);
  }

  @Override
  public @NotNull TermString substring(int start) {
    return substring(start, value.length());
  }

  @Override
  public @NotNull TermString substring(int start, int end) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    builder.region(start, end);
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString erase(int start, int end) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    builder.erase(start, end);
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TermString concat(@NotNull TermString termString) {
    UnsafeTermStringBuilder builder = (UnsafeTermStringBuilder)
        TermString.builder(value, indexedStyleData);
    builder.append(termString);
    return builder.getNoCopy();
  }

  @Override
  public @NotNull TextStyle styleAt(int index) {
    return indexedStyleData.at(index);
  }

  @Override
  public boolean equals(@Nullable String str) {
    return value.equals(str);
  }

  @Override
  public boolean equalsIgnoreCase(@Nullable String str) {
    return value.equals(str);
  }

  @Override
  public boolean equals(@Nullable TermString str) {
    if(str == null) {
      return false;
    }
    return str.equals(value) && str.data().equals(indexedStyleData);
  }

  @Override
  public boolean equalsIgnoreCase(@Nullable TermString str) {
    if(str == null) {
      return false;
    }
    return str.equalsIgnoreCase(value) && str.data().equals(indexedStyleData);
  }

  @Override
  public boolean isMatch(@NotNull Pattern pattern) {
    Matcher matcher = pattern.matcher(value);
    return matcher.find() && matcher.start() == 0 && matcher.end() == value.length();
  }

  @Override
  public boolean startsWith(@NotNull String str) {
    return value.startsWith(str);
  }

  @Override
  public boolean endsWith(@NotNull String str) {
    return value.endsWith(str);
  }

  @Override
  public @NotNull IndexedStyleData data() {
    return indexedStyleData;
  }

  @Override
  public byte[] bytes() {
    return bytes(Charset.defaultCharset());
  }

  @Override
  public byte[] bytes(@NotNull Charset charset) {
    ByteBuf buf = ByteBuf.builder()
        .dynamicCapacityAddition(128)
        .capacity(128)
        .build();
    buf.writeTermString(this, charset);
    return buf.trim().byteArray();
  }

  @Override
  public byte[] rawBytes() {
    return value.getBytes();
  }

  @Override
  public byte[] rawBytes(@NotNull Charset charset) {
    return value.getBytes(charset);
  }

  @Override
  public @NotNull TermString copy() {
    return new TermStringImpl(value, indexedStyleData.copy());
  }
}
