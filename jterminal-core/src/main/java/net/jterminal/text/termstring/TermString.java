package net.jterminal.text.termstring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
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

  @NotNull TermString replaceAll(@NotNull @RegExp String pattern, @NotNull String to);

  @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull String to);

  @NotNull TermString substring(int start);

  @NotNull TermString substring(int start, int end);

  @NotNull TermString concat(@NotNull TermString termString);

  @NotNull TextStyle styleAt(int index);

  boolean equals(@Nullable String str);

  boolean equalsIgnoreCase(@Nullable String str);

  boolean equals(@Nullable TermString str);

  boolean equalsIgnoreCase(@Nullable TermString str);

  boolean isMatch(@NotNull Pattern pattern);

  boolean startsWith(@NotNull String str);

  boolean endsWith(@NotNull String str);

  @NotNull StyleIndexedData data();

  byte[] bytes();

  byte[] bytes(@NotNull Charset charset);

  byte[] rawBytes();

  byte[] rawBytes(@NotNull Charset charset);

}
