package net.jterminal.text.element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TextElementImpl implements TextElement {

  private String value;
  private final TextStyle textStyle;
  private List<TextElement> childList = null;

  public TextElementImpl(@NotNull String value, @NotNull TextStyle textStyle) {
    this.value = value;
    this.textStyle = textStyle.clone();
  }

  @Override
  public @NotNull TextElement foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.textStyle.foregroundColor(foregroundColor);
    return this;
  }

  @Override
  public @Nullable ForegroundColor foregroundColor() {
    return this.textStyle.foregroundColor();
  }

  @Override
  public @NotNull TextElement backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.textStyle.backgroundColor(backgroundColor);
    return this;
  }

  @Override
  public @Nullable BackgroundColor backgroundColor() {
    return this.textStyle.backgroundColor();
  }

  @Override
  public @NotNull Set<TextFont> fonts() {
    return this.textStyle.fonts();
  }

  @Override
  public @NotNull TextElement fonts(TextFont... fonts) {
    textStyle.font(fonts);
    return this;
  }

  @Override
  public @NotNull TextElement value(@NotNull String value) {
    this.value = value;
    return this;
  }

  @Override
  public @NotNull String value() {
    return value;
  }

  @Override
  public @NotNull TextStyle style() {
    return textStyle;
  }

  @Override
  public @NotNull TextElement child(TextElement... elements) {
    childList = Arrays.asList(elements);
    return this;
  }

  @Override
  public @NotNull List<TextElement> child() {
    if(childList == null) {
      return new ArrayList<>();
    }
    return Collections.unmodifiableList(childList);
  }
}
