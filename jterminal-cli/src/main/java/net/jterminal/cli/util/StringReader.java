package net.jterminal.cli.util;

import net.jterminal.annotation.NoThreadSafety;
import net.jterminal.cli.exception.StringException;
import net.jterminal.util.CharFilter;
import org.jetbrains.annotations.NotNull;

@NoThreadSafety
public class StringReader {

  private final char[] array;
  private int position;

  public StringReader(@NotNull String value) {
    this.array = value.toCharArray();
  }

  public StringReader(char[] array) {
    this.array = array;
  }

  public int position() {
    return position;
  }

  public void position(int position) {
    this.position = Math.max(0, Math.min(array.length, position));
  }

  public int peek() {
    return peek(0);
  }

  public int peek(int offset) {
    int pos = position + offset;
    if(pos < 0 || pos >= array.length) {
      return -1;
    }
    return array[pos];
  }

  public int read(char[] array) {
    return read(array, 0, array.length);
  }

  public int read(char[] array, int off, int len) {
    len = len - off;
    if(len < 0) {
      return 0;
    }
    len = Math.min(remaining(), len);
    for (int idx = off; idx < len; idx++) {
      array[idx] = this.array[position++];
    }
    return len;
  }

  public @NotNull String readString(int len) {
    char[] arr = new char[len];
    int outLen = read(arr, 0, len);
    return new String(arr, 0, outLen);
  }

  public int read() {
    if(position < array.length) {
      return array[position++];
    }
    return 0;
  }

  public void skip() {
    skip(1);
  }

  public void skip(int n) {
    position = Math.min(position + n, array.length);
  }

  public int skipAllWhitespaces() {
    int count = 0;
    while (Character.isWhitespace(peek())) {
      skip();
      count = 1;
    }
    return count;
  }

  public char peekChar() {
    return peekChar(0);
  }

  public char peekChar(int offset) {
    return (char) peek(offset);
  }

  public char readChar() {
    return (char) read();
  }

  public int remaining() {
    return array.length - position;
  }

  public boolean avail() {
    return position < array.length;
  }

  public boolean isNextWhitespace() {
    int peek = peek();
    return Character.isWhitespace(peek);
  }

  public boolean isNextDigit() {
    return isNextDigit(0);
  }

  public boolean isNextDigit(int off) {
    int peek = peek(off);
    return Character.isDigit(peek) || peek == '-' || peek == '.';
  }

  public boolean isNextEscapeChar() {
    return isNextEscapeChar(0);
  }

  public boolean isNextEscapeChar(int off) {
    int peek = peek(off);
    return peek == '\n';
  }

  public boolean isNextQuoted() {
    int peek = peek();
    return peek == '\'' || peek == '\"';
  }

  public int readInt() throws StringException {
    int len = 0;
    while (isNextDigit(len)) {
      len++;
    }
    final int pos = position;
    String value = readString(len);
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      throw new StringException(e.getMessage(), pos);
    }
  }

  public long readLong() throws StringException {
    int len = 0;
    while (isNextDigit(len)) {
      len++;
    }
    final int pos = position;
    String value = readString(len);
    try {
      return Long.parseLong(value);
    } catch (NumberFormatException e) {
      throw new StringException(e.getMessage(), pos);
    }
  }

  public double readDouble() throws StringException {
    int len = 0;
    while (isNextDigit(len)) {
      len++;
    }
    final int pos = position;
    String value = readString(len);
    try {
      return Double.parseDouble(value);
    } catch (NumberFormatException e) {
      throw new StringException(e.getMessage(), pos);
    }
  }

  public @NotNull String read(@NotNull CharFilter charFilter) {
    StringBuilder stringBuilder = new StringBuilder();
    int peekCh;
    while(charFilter.isAccept(peekCh = peek()) && avail()) {
      stringBuilder.append((char) peekCh);
    }
    return stringBuilder.toString();
  }

  public @NotNull String readWhile(int endChar, boolean ignoreEscape) {
    StringBuilder stringBuilder = new StringBuilder();
    int ch;
    boolean escaped = false;
    while(avail()) {
      ch = read();
      if(ignoreEscape && escaped) {
        stringBuilder.append((char) ch);
        escaped = false;
        continue;
      }
      if(ch == '\\') {
        escaped = true;
      }
      if(ch == endChar) {
        stringBuilder.append((char) endChar);
        break;
      }
      stringBuilder.append((char) ch);
    }
    return stringBuilder.toString();
  }

  public @NotNull String readQuoted(int quoteChar) {
    StringBuilder stringBuilder = new StringBuilder();
    if(peek() != quoteChar) {
      return stringBuilder.toString();
    }
    char first = readChar();
    stringBuilder.append(first);
    String content = readWhile(quoteChar, true);
    stringBuilder.append(content);
    return stringBuilder.toString();
  }

  public @NotNull String readNonWhitespace() {
    skipAllWhitespaces();
    return read(CharFilter.NON_WHITESPACE_FILTER);
  }

}
