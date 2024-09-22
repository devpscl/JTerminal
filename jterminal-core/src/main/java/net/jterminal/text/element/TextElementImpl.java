package net.jterminal.text.element;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TextElementImpl implements TextElement {

  private String value;
  private ForegroundColor foregroundColor = null;
  private BackgroundColor backgroundColor = null;
  private final EnumSet<TextFont> fontSet;
  private final Set<TextElement> elementSet = new HashSet<>();

  public TextElementImpl(@NotNull String value, @Nullable ForegroundColor fgc,
      @Nullable BackgroundColor bgc, @Nullable EnumSet<TextFont> fontSet) {
    this.value = value;
    this.foregroundColor = fgc;
    this.backgroundColor = bgc;
    this.fontSet = fontSet == null ? EnumSet.noneOf(TextFont.class) : fontSet;

  }

  @Override
  public @NotNull TextElement foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }

  @Override
  public @Nullable ForegroundColor foregroundColor() {
    return foregroundColor;
  }

  @Override
  public @NotNull TextElement backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  @Override
  public @Nullable BackgroundColor backgroundColor() {
    return backgroundColor;
  }

  @Override
  public @NotNull Set<TextFont> fonts() {
    return Collections.unmodifiableSet(fontSet);
  }

  @Override
  public @NotNull TextElement fonts(TextFont... fonts) {
    fontSet.clear();
    Collections.addAll(fontSet, fonts);
    return this;
  }

  @Override
  public @NotNull TextElement value(@NotNull String value) {
    this.value = value;
    return this;
  }

  @Override
  public @Nullable String value() {
    return value;
  }

  @Override
  public @NotNull TextElement child(TextElement... elements) {
    elementSet.clear();
    Collections.addAll(elementSet, elements);
    return this;
  }

  @Override
  public @NotNull Set<TextElement> child() {
    return Collections.unmodifiableSet(elementSet);
  }
}
