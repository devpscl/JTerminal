package net.jterminal.text.style;

import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextStyle {

  enum FontOption {
    SET,
    UNSET,
    IGNORED
  }

  @Nullable
  ForegroundColor foregroundColor();

  @Nullable
  BackgroundColor backgroundColor();

  @NotNull
  Set<TextFont> fonts();

  @NotNull
  TextStyle foregroundColor(@Nullable ForegroundColor foregroundColor);

  @NotNull
  TextStyle backgroundColor(@Nullable BackgroundColor backgroundColor);

  @NotNull
  TextStyle font(@NotNull TextFont... textFonts);

  @NotNull
  TextStyle font(@Nullable Set<TextFont> textFontSet);

  @NotNull
  TextStyle font(@NotNull FontOption fontOption, @NotNull TextFont... textFonts);

  @NotNull
  TextStyle font(@NotNull FontOption fontOption, @Nullable Set<TextFont> textFontSet);

  @NotNull
  TextStyle setFont(@NotNull TextFont... textFonts);

  @NotNull
  TextStyle unsetFont(@NotNull TextFont... textFonts);

  @NotNull
  TextStyle ignoreFont(@NotNull TextFont... textFonts);

  @NotNull
  FontMap fontMap();

  @NotNull
  TextStyle clone();

  boolean equals(@NotNull TextStyle other);

  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      TextFont...fonts) {
    FontMap fontMap = new FontMap();
    fontMap.set(fonts);
    return new TextStyleImpl(foregroundColor, backgroundColor, fontMap);
  }

  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor, @Nullable FontMap fontMap) {
    return new TextStyleImpl(foregroundColor, backgroundColor,
        fontMap == null ? new FontMap() : fontMap);
  }

  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor) {
    return new TextStyleImpl(foregroundColor, backgroundColor, new FontMap());
  }

  static @NotNull TextStyle create() {
    return create(null, null);
  }

}
