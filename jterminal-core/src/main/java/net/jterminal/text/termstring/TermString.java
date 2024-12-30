package net.jterminal.text.termstring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
import net.jterminal.buffer.ByteBuf;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.TextStyle;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TermString {

  @NotNull String raw();

  int length();

  char charAt(int index);

  int indexOf(char c);

  int indexOf(String str);

  @NotNull TermString replace(char from, char to);

  @NotNull TermString replace(@NotNull String from, @NotNull String to);

  @NotNull TermString replace(@NotNull String from, @NotNull TermString to);

  @NotNull TermString replaceAll(@NotNull @RegExp String pattern, @NotNull String to);

  @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull String to);

  @NotNull TermString replaceAll(@NotNull @RegExp String pattern, @NotNull TermString to);

  @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull TermString to);

  @NotNull TermString[] split(@NotNull @RegExp String pattern);

  @NotNull TermString[] split(@NotNull Pattern pattern);

  @NotNull TermString[] split(char ch);

  @NotNull TermString substring(int start);

  @NotNull TermString substring(int start, int end);

  @NotNull TermString erase(int start, int end);

  @NotNull TermString concat(@NotNull TermString termString);

  @NotNull TextStyle styleAt(int index);

  boolean equals(@Nullable String str);

  boolean equalsIgnoreCase(@Nullable String str);

  boolean equals(@Nullable TermString str);

  boolean equalsIgnoreCase(@Nullable TermString str);

  boolean isMatch(@NotNull Pattern pattern);

  boolean startsWith(@NotNull String str);

  boolean endsWith(@NotNull String str);

  @NotNull IndexedStyleData data();

  byte[] bytes();

  byte[] bytes(@NotNull Charset charset);

  byte[] rawBytes();

  byte[] rawBytes(@NotNull Charset charset);

  @NotNull TermString clone();

  static @NotNull TermString value(@Nullable String x) {
    return new TermStringImpl(x);
  }

  static @NotNull TermString value(@Nullable Object x) {
    return value(String.valueOf(x));
  }

  static @NotNull TermString value(@Nullable TextElement x) {
    return x == null ? empty() : Element2StringConverter.convert(x);
  }

  static @NotNull TermString empty() {
    return value("");
  }

  static @NotNull TermString fromBytes(byte[] bytes) {
    return fromBytes(bytes, Charset.defaultCharset());
  }

  static @NotNull TermString fromBytes(byte[] bytes, @NotNull Charset charset) {
    ByteBuf buf = ByteBuf.from(bytes);
    return buf.readTermString(charset);
  }

  static @NotNull TermString create(@Nullable String value, @Nullable IndexedStyleData data) {
    return new TermStringImpl(value, data);
  }

  static @NotNull TermStringBuilder builder() {
    return new TermStringBuilderImpl(new StringBuilder(), new IndexedStyleDataImpl());
  }

  static @NotNull TermStringBuilder builder(@NotNull String value) {
    return new TermStringBuilderImpl(new StringBuilder(value), new IndexedStyleDataImpl());
  }

  static @NotNull TermStringBuilder builder(@NotNull TermString termString) {
    return new TermStringBuilderImpl(new StringBuilder(termString.raw()),
        termString.data().clone());
  }

  static @NotNull TermStringBuilder builder(@NotNull String value,
      @NotNull IndexedStyleData data) {
    return new TermStringBuilderImpl(value, data.clone());
  }

}
