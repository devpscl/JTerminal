package net.jterminal.text.termstring;

import java.nio.charset.Charset;
import java.util.regex.Pattern;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TermStringImpl implements TermString {

  private String value;


  @Override
  public @NotNull String raw() {
    return "";
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public char charAt(int index) {
    return 0;
  }

  @Override
  public int indexOf(char c) {
    return 0;
  }

  @Override
  public int indexOf(String str) {
    return 0;
  }

  @Override
  public @NotNull TermString replace(char from, char to) {
    return null;
  }

  @Override
  public @NotNull TermString replace(@NotNull String from, @NotNull String to) {
    return null;
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull String pattern, @NotNull String to) {
    return null;
  }

  @Override
  public @NotNull TermString replaceAll(@NotNull Pattern pattern, @NotNull String to) {
    return null;
  }

  @Override
  public @NotNull TermString substring(int start) {
    return null;
  }

  @Override
  public @NotNull TermString substring(int start, int end) {
    return null;
  }

  @Override
  public @NotNull TermString concat(@NotNull TermString termString) {
    return null;
  }

  @Override
  public @NotNull TextStyle styleAt(int index) {
    return null;
  }

  @Override
  public boolean equals(@Nullable String str) {
    return false;
  }

  @Override
  public boolean equalsIgnoreCase(@Nullable String str) {
    return false;
  }

  @Override
  public boolean equals(@Nullable TermString str) {
    return false;
  }

  @Override
  public boolean equalsIgnoreCase(@Nullable TermString str) {
    return false;
  }

  @Override
  public boolean isMatch(@NotNull Pattern pattern) {
    return false;
  }

  @Override
  public boolean startsWith(@NotNull String str) {
    return false;
  }

  @Override
  public boolean endsWith(@NotNull String str) {
    return false;
  }

  @Override
  public byte[] bytes() {
    return new byte[0];
  }

  @Override
  public byte[] bytes(@NotNull Charset charset) {
    return new byte[0];
  }

  @Override
  public byte[] rawBytes() {
    return new byte[0];
  }

  @Override
  public byte[] rawBytes(@NotNull Charset charset) {
    return new byte[0];
  }
}
