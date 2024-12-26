package net.jterminal.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;

public class CharFilter {

  public static final CharFilter WORD_FILTER = new CharFilter(
      CharType.LETTERS_UPPERCASE, CharType.LETTERS_LOWERCASE
  );
  public static final CharFilter NON_WHITESPACE_FILTER = new CharFilter(
      CharType.LETTERS_UPPERCASE, CharType.LETTERS_LOWERCASE, CharType.DIGIT,
      CharType.REGULAR_SYMBOL, CharType.OTHER_SYMBOL
  );

  public enum CharType {
    ISO_CONTROL,
    REGULAR_SYMBOL,
    DIGIT,
    LETTERS_LOWERCASE,
    LETTERS_UPPERCASE,
    WHITESPACE,
    OTHER_SYMBOL;

    final int bitf = 1 << ordinal();
  }

  private final int bitflag;
  private final List<Predicate<Integer>> customFilters = new ArrayList<>();

  public CharFilter(@NotNull CharType...allowedTypes) {
    int bs = 0;
    for (@NotNull CharType allowedType : allowedTypes) {
      bs |= allowedType.bitf;
    }
    bitflag = bs;
  }

  public void addFilter(@NotNull Predicate<Integer> filter) {
    customFilters.add(filter);
  }

  public boolean isAllowed(@NotNull CharType charType) {
    return (bitflag & charType.bitf) == charType.bitf;
  }

  public boolean isAccept(char ch) {
    return isAccept((int) ch);
  }

  public boolean isAccept(int ch) {
    if(Character.isLowerCase(ch)) {
      return isAllowed(CharType.LETTERS_LOWERCASE);
    }
    if(Character.isUpperCase(ch)) {
      return isAllowed(CharType.LETTERS_UPPERCASE);
    }
    if(Character.isDigit(ch)) {
      return isAllowed(CharType.DIGIT);
    }
    if(Character.isISOControl(ch)) {
      return isAllowed(CharType.ISO_CONTROL);
    }
    if(Character.isWhitespace(ch)) {
      return isAllowed(CharType.WHITESPACE);
    }
    if(((ch >= 33 && ch <= 47)
        || (ch >= 58 && ch <= 64)
        || (ch >= 91 && ch <= 96)
        || (ch >= 123 && ch <= 126))) {
      return isAllowed(CharType.REGULAR_SYMBOL);
    }
    if(!customFilters.isEmpty()) {
      for (Predicate<Integer> filter : customFilters) {
        if(!filter.test(ch)) {
          return false;
        }
      }
    }
    return isAllowed(CharType.OTHER_SYMBOL);
  }

  public @NotNull String replaceAll(@NotNull String value, char replacement) {
    return new String(replaceAll(value.toCharArray(), replacement));
  }

  public char @NotNull [] replaceAll(char @NotNull [] arr, char c) {
    for (int idx = 0; idx < arr.length; idx++) {
      if(!isAccept(arr[idx])) {
        arr[idx] = c;
      }
    }
    return arr;
  }

  public boolean isAccept(@NotNull String value) {
    return isAccept(value.toCharArray());
  }

  public boolean isAccept(char @NotNull [] arr) {
    for (char c : arr) {
      if (!isAccept(c)) {
        return false;
      }
    }
    return true;
  }

}
