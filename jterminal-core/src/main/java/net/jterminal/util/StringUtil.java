package net.jterminal.util;

import org.jetbrains.annotations.NotNull;

public class StringUtil {

  public static @NotNull String unescapeString(@NotNull String str) {
    return str.replaceAll("\033", "\\\\e");
  }

}
