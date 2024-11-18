package net.jterminal.cli.util;

import net.jterminal.annotation.NoThreadSafety;
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

  public void skipAllWhitespaces() {
    while (Character.isWhitespace(peek())) {
      skip();
    }
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
    int peek = peek();
    return Character.isDigit(peek);
  }

  public boolean isNextStringSequence() {
    int peek = peek();
    return peek == '\'' || peek == '\"';
  }

  public int readInt() throws NumberFormatException {
    int len = 0;
    while (Character.isDigit(peek(len))) {
      len++;
    }
    String value = readString(len);
    return Integer.parseInt(value);
  }

  public long readLong() throws NumberFormatException {
    int len = 0;
    while (Character.isDigit(peek(len))) {
      len++;
    }
    String value = readString(len);
    return Long.parseLong(value);
  }

  public double readDouble() throws NumberFormatException {
    int len = 0;
    int buf;
    while ((buf = peek(len)) != 0 && (Character.isDigit(buf) || buf == '.')) {
      len++;
    }
    String value = readString(len);
    return Double.parseDouble(value);
  }


}
