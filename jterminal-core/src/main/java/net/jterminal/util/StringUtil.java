package net.jterminal.util;

import java.util.Arrays;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StringUtil {

  public static final Pattern NEXT_LINE_PATTERN =
      Pattern.compile("\\r?\\n");

  public static final String EMPTY_STRING = "";

  public static @NotNull String unescapeString(@NotNull String str) {
    return str.replaceAll("\033", "\\\\e");
  }

  public static @NotNull String notNull(@Nullable String str) {
    return notNull(str, "null");
  }

  public static @NotNull String notNull(@Nullable String str, @NotNull String alt) {
    return str == null ? alt : str;
  }

  public static char resolveEscapeChar(char suffix) {
    return switch (suffix) {
      case 'n' -> '\n';
      case '"' -> '\"';
      case '\'' -> '\'';
      case 'e' -> 27;
      case 't' -> '\t';
      case 'f' -> '\f';
      case 'b' -> '\b';
      case 'r' -> '\r';
      case '\\' -> '\\';
      default -> throw new IllegalArgumentException("Invalid escape character: " + suffix);
    };
  }

  public static @NotNull String repeat(char c, int count) {
    char[] arr = new char[count];
    Arrays.fill(arr, c);
    return new String(arr);
  }

  public static int countOfChar(@NotNull String str, char symbol) {
    return (int) str.chars().filter(ch -> ch == symbol).count();
  }

}
