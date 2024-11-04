package net.jterminal.text.termstring;

import java.util.Collection;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TermStringBuilder {

  @NotNull TermStringBuilder append(String x);

  @NotNull TermStringBuilder append(char x);

  @NotNull TermStringBuilder append(short x);

  @NotNull TermStringBuilder append(int x);

  @NotNull TermStringBuilder append(long x);

  @NotNull TermStringBuilder append(float x);

  @NotNull TermStringBuilder append(double x);

  @NotNull TermStringBuilder append(boolean x);

  @NotNull TermStringBuilder append(Object x);

  @NotNull TermStringBuilder append(TermString termString);

  @NotNull TermStringBuilder foregroundColor(@Nullable ForegroundColor foregroundColor);

  @NotNull TermStringBuilder backgroundColor(@Nullable BackgroundColor backgroundColor);

  @NotNull TermStringBuilder fonts(TextFont...fonts);

  @NotNull TermStringBuilder fontsSet(TextFont...fonts);

  @NotNull TermStringBuilder fontsUnset(TextFont...fonts);

  @NotNull TermStringBuilder fonts(Collection<TextFont> fonts);

  @NotNull TermStringBuilder fontsSet(Collection<TextFont> fonts);

  @NotNull TermStringBuilder fontsUnset(Collection<TextFont> fonts);

  @NotNull TermStringBuilder appendStyle(@NotNull TextStyle textStyle);

  @NotNull TermStringBuilder appendExplicitStyle(@NotNull TextStyle textStyle);

  @NotNull TermStringBuilder resetStyle();

  @NotNull TermStringBuilder insert(int index, String x);

  @NotNull TermStringBuilder insert(int index, TermString termString);

  @NotNull TermStringBuilder region(int start, int end);

  @NotNull TermStringBuilder erase(int start, int end);

  @NotNull TermStringBuilder replace(int start, int end, String str);

  @NotNull TermStringBuilder replace(int start, int end, TermString termString);

  @NotNull TermString build();



}
