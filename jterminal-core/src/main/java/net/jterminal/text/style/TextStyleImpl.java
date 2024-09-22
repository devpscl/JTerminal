package net.jterminal.text.style;

import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TextStyleImpl implements TextStyle {

  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  private final FontMap fontMap;

  public TextStyleImpl(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      @NotNull FontMap fontMap) {
    this.foregroundColor = foregroundColor;
    this.backgroundColor = backgroundColor;
    this.fontMap = fontMap;
  }

  @Override
  public @Nullable ForegroundColor foregroundColor() {
    return foregroundColor;
  }

  @Override
  public @Nullable BackgroundColor backgroundColor() {
    return backgroundColor;
  }

  @Override
  public @NotNull Set<TextFont> fonts() {
    return fontMap.fonts();
  }

  @Override
  public @NotNull TextStyle foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }

  @Override
  public @NotNull TextStyle backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  @Override
  public @NotNull TextStyle font(@NotNull TextFont... textFonts) {
    fontMap.unset(TextFont.values());
    fontMap.set(textFonts);
    return this;
  }

  @Override
  public @NotNull TextStyle font(@Nullable Set<TextFont> textFontSet) {
    fontMap.unset(TextFont.values());
    if(textFontSet == null) {
      return this;
    }
    for (TextFont textFont : textFontSet) {
      fontMap.set(textFont);
    }
    return this;
  }

  @Override
  public @NotNull TextStyle font(@NotNull FontOption fontOption, @NotNull TextFont... textFonts) {
    fontMap.set(fontOption, textFonts);
    return this;
  }

  @Override
  public @NotNull TextStyle font(@NotNull FontOption fontOption,
      @Nullable Set<TextFont> textFontSet) {
    if(textFontSet == null) {
      return this;
    }
    fontMap.set(fontOption, textFontSet.toArray(new TextFont[0]));
    return this;
  }

  @Override
  public @NotNull TextStyle setFont(@NotNull TextFont... textFonts) {
    fontMap.set(textFonts);
    return this;
  }

  @Override
  public @NotNull TextStyle unsetFont(@NotNull TextFont... textFonts) {
    fontMap.unset(textFonts);
    return this;
  }

  @Override
  public @NotNull TextStyle ignoreFont(@NotNull TextFont... textFonts) {
    fontMap.ignore(textFonts);
    return this;
  }

  @Override
  public @NotNull FontMap fontMap() {
    return fontMap;
  }

  @Override
  public @NotNull TextStyle clone() {
    return new TextStyleImpl(foregroundColor, backgroundColor, new FontMap(fontMap));
  }

  @Override
  public boolean equals(@NotNull TextStyle other) {
    if(!(other instanceof TextStyleImpl impl)) {
      return false;
    }
    return foregroundColor == impl.foregroundColor &&
        backgroundColor == impl.backgroundColor &&
        fontMap.equals(((TextStyleImpl) other).fontMap);
  }
}
