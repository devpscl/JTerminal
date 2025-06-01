package net.jterminal.text.termstring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
import net.jterminal.buffer.ByteBuf;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.TextStyle;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The interface TermString contains a formatted string with attributes at certain positions.
 * The object of the type is final and all changes result in a new object.
 */
public interface TermString {

  /**
   * Raw string without format.
   *
   * @return the string
   */
  @NotNull String raw();

  /**
   * Count characters of string.
   * @see String#length()
   *
   * @return the length
   */
  int length();

  /**
   * Get char at index.
   * @see String#charAt(int)
   * @param index the index
   * @return the char
   */
  char charAt(int index);

  /**
   * Get index of char
   * @see String#indexOf(int)
   * @param c the char
   * @return the index
   */
  int indexOf(char c);

  /**
   * Get index of string sequence.
   * @see String#indexOf(String)
   * @param str the string
   * @return the index
   */
  int indexOf(String str);

  /**
   * Replace char.
   * @see String#replace(char, char)
   * @param from the old char
   * @param to   the new char
   * @return new term string
   */
  @NotNull TermString replace(char from, char to);

  /**
   * Replace string.
   * @see String#replace(CharSequence, CharSequence)
   * @param from the string to replaced
   * @param to   the replacement string
   * @return new term string
   */
  @NotNull TermString replace(@NotNull String from, @NotNull String to);

  /**
   * Replace term string.
   * @see String#replace(CharSequence, CharSequence)
   *
   * @param from the string to replaced
   * @param to   the replacement string
   * @return the new term string
   */
  @NotNull TermString replace(@NotNull String from, @NotNull TermString to);

  /**
   * Replace regex match to string.
   * @see String#replaceAll(String, String)
   * @param pattern the regex pattern as string
   * @param to      the replacement string
   * @return the new term string
   */
  @NotNull TermString replaceAll(@NotNull @RegExp String pattern, @NotNull String to);

  /**
   * Replace regex match to string.
   * @see String#replaceAll(String, String)
   * @param pattern the regex pattern
   * @param to      the replacement string
   * @return the new term string
   */
  @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull String to);

  /**
   * Replace regex match to string.
   * @see String#replaceAll(String, String)
   * @param pattern the regex pattern as string
   * @param to      the replacement term string
   * @return the new term string
   */
  @NotNull TermString replaceAll(@NotNull @RegExp String pattern, @NotNull TermString to);

  /**
   * Replace regex match to string.
   * @see String#replaceAll(String, String)
   * @param pattern the regex pattern
   * @param to      the replacement term string
   * @return the new term string
   */
  @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull TermString to);

  /**
   * Split term string by regex.
   *
   * @param pattern the regex pattern as string
   * @return copied parts between spliterator
   */
  @NotNull TermString[] split(@NotNull @RegExp String pattern);

  /**
   * Split term string by regex.
   *
   * @param pattern the regex pattern
   * @return copied parts between spliterator
   */
  @NotNull TermString[] split(@NotNull Pattern pattern);

  /**
   * Split term string by single character.
   *
   * @param ch the char
   * @return copied parts between spliterator
   */
  @NotNull TermString[] split(char ch);

  /**
   * Substring term string from start.
   * @see String#substring(int)
   * @param start the start
   * @return the new term string
   */
  @NotNull TermString substring(int start);

  /**
   * Substring term string from start to end.
   * @see String#substring(int, int)
   * @param start the start
   * @param end   the end
   * @return the new term string
   */
  @NotNull TermString substring(int start, int end);

  /**
   * Erase region from start to end
   *
   * @param start the start
   * @param end   the end
   * @return the new term string
   */
  @NotNull TermString erase(int start, int end);

  /**
   * Concat term string to this string.
   * @see String#concat(String)
   * @param termString the term string
   * @return the new term string
   */
  @NotNull TermString concat(@NotNull TermString termString);

  /**
   * Get style format at index.
   *
   * @param index the index
   * @return the text style
   */
  @NotNull TextStyle styleAt(int index);

  /**
   * Wrap string to lines with line limit.
   *
   * @param limit the limit of a line
   * @return term string lines
   */
  @NotNull TermString[] wrapLines(int limit);

  /**
   * Equals.
   *
   * @param str the string
   * @return the boolean
   */
  boolean equals(@Nullable String str);

  /**
   * Equals ignore case.
   *
   * @param str the string
   * @return the boolean
   */
  boolean equalsIgnoreCase(@Nullable String str);

  /**
   * Equals.
   *
   * @param str the term string
   * @return the boolean
   */
  boolean equals(@Nullable TermString str);

  /**
   * Equals ignore case.
   *
   * @param str the term string
   * @return the boolean
   */
  boolean equalsIgnoreCase(@Nullable TermString str);

  /**
   * Is match to regex pattern.
   *
   * @param pattern the pattern
   * @return result of match
   */
  boolean isMatch(@NotNull Pattern pattern);

  /**
   * Check if starts with prefix.
   *
   * @param str prefix
   * @return result of compare
   */
  boolean startsWith(@NotNull String str);

  /**
   * Check if starts with suffix.
   *
   * @param str suffix
   * @return result of compare
   */
  boolean endsWith(@NotNull String str);

  /**
   * @return the indexed style data
   */
  @NotNull IndexedStyleData data();

  /**
   * Convert to term string binary format with default charset.
   * @see ByteBuf#writeTermString(TermString)
   *
   * @return the byte array
   */
  byte[] bytes();

  /**
   * Convert to term string binary format with specific charset.
   * @see ByteBuf#writeTermString(TermString, Charset)
   * @param charset the charset
   * @return the byte array
   */
  byte[] bytes(@NotNull Charset charset);

  /**
   * Bytes of raw string with default charset.
   * @see String#getBytes()
   * @return the byte array
   */
  byte[] rawBytes();

  /**
   * Bytes of raw string with specific charset.
   * @see String#getBytes(Charset)
   * @param charset the charset
   * @return the byte array
   */
  byte[] rawBytes(@NotNull Charset charset);

  /**
   * Copy term string.
   *
   * @return the new term string
   */
  @NotNull TermString copy();

  /**
   * Create term string by normal string sequence without format style.
   *
   * @param x the string value
   * @return the term string
   */
  static @NotNull TermString value(@Nullable String x) {
    return new TermStringImpl(x);
  }

  /**
   * Create term string by string value of object without format style.
   * @see String#valueOf(Object)
   *
   * @param x the object
   * @return the term string
   */
  static @NotNull TermString value(@Nullable Object x) {
    return value(String.valueOf(x));
  }

  /**
   * Create term string by text element. The format remains unchanged.
   *
   * @param x the text element
   * @return the term string
   */
  static @NotNull TermString value(@Nullable TextElement x) {
    return x == null ? empty() : Element2StringConverter.convert(x);
  }

  /**
   * Create new joiner.
   *
   * @return the term string joiner
   */
  static @NotNull TermStringJoiner joiner() {
    return new TermStringJoinerImpl();
  }

  /**
   * Join term string by delimiter.
   * @see String#join(CharSequence, CharSequence...)
   * @param delimiter the delimiter
   * @param strings   the strings
   * @return the term string
   */
  static @NotNull TermString join(@NotNull TermString delimiter, @NotNull TermString...strings) {
    return joiner()
        .addAll(strings)
        .delimiter(delimiter)
        .build();
  }

  /**
   * @return an empty term string
   */
  static @NotNull TermString empty() {
    return value("");
  }

  /**
   * Get from term string binary with default charset.
   * @see ByteBuf#readTermString()
   *
   * @param bytes the bytes
   * @return the term string
   */
  static @NotNull TermString fromBytes(byte[] bytes) {
    return fromBytes(bytes, Charset.defaultCharset());
  }

  /**
   * Get from term string binary with specific charset.
   * @see ByteBuf#readTermString(Charset)
   *
   * @param bytes   the bytes
   * @param charset the charset
   * @return the term string
   */
  static @NotNull TermString fromBytes(byte[] bytes, @NotNull Charset charset) {
    ByteBuf buf = ByteBuf.from(bytes);
    return buf.readTermString(charset);
  }

  /**
   * Create term string with style data.
   *
   * @param value the value
   * @param data  the data
   * @return the term string
   */
  static @NotNull TermString create(@Nullable String value, @Nullable IndexedStyleData data) {
    return new TermStringImpl(value, data);
  }

  static @NotNull TermString createWithForeground(@NotNull String value,
      @Nullable TerminalColor color) {
    if(color == null) {
      return value(value);
    }
    return builder().foregroundColor(color)
        .append(value)
        .foregroundColor(null)
        .build();
  }

  static @NotNull TermString createWithBackground(@NotNull String value,
      @Nullable TerminalColor color) {
    if(color == null) {
      return value(value);
    }
    return builder().backgroundColor(color)
        .append(value)
        .foregroundColor(null)
        .build();
  }

  /**
   * Create new builder with empty start value.
   *
   * @return the term string builder
   */
  static @NotNull TermStringBuilder builder() {
    return new TermStringBuilderImpl(new StringBuilder(), new IndexedStyleDataImpl());
  }

  /**
   * Create new builder with start value.
   *
   * @param value the start value
   * @return the term string builder
   */
  static @NotNull TermStringBuilder builder(@NotNull String value) {
    return new TermStringBuilderImpl(new StringBuilder(value), new IndexedStyleDataImpl());
  }

  /**
   * Create new builder with start value.
   *
   * @param termString the start value
   * @return the term string builder
   */
  static @NotNull TermStringBuilder builder(@NotNull TermString termString) {
    return new TermStringBuilderImpl(new StringBuilder(termString.raw()),
        termString.data().copy());
  }

  /**
   * Create new builder with start value and style data.
   *
   * @param value the start value
   * @param data  the start style data
   * @return the term string builder
   */
  static @NotNull TermStringBuilder builder(@NotNull String value,
      @NotNull IndexedStyleData data) {
    return new TermStringBuilderImpl(value, data.copy());
  }

}
