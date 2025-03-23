package net.jterminal.text.style;

import java.util.Collection;
import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The Text style can store attributes about foreground, background and fonts.
 */
public interface TextStyle {

  /**
   * The enum Font option.
   */
  enum FontOption {
    /**
     * Forced set of a font
     */
    SET,
    /**
     * Forced unset of a font
     */
    UNSET,
    /**
     * No change of font state
     */
    IGNORED
  }

  /**
   * Get foreground color. If color is null, the foreground color is currently ignored
   *
   * @return the foreground color
   */
  @Nullable
  ForegroundColor foregroundColor();

  /**
   * Get background color. If color is null, the background color is currently ignored
   *
   * @return the background color
   */
  @Nullable
  BackgroundColor backgroundColor();

  /**
   * Get all forced set font types
   *
   * @return the set of font types
   */
  @NotNull
  Set<TextFont> fonts();

  /**
   * Change the foreground color. If color is null, the foreground color is ignored
   *
   * @param foregroundColor the foreground color
   * @return self
   */
  @NotNull
  TextStyle foregroundColor(@Nullable ForegroundColor foregroundColor);

  /**
   * Change the background color. If color is null, the background color is ignored
   *
   * @param backgroundColor the background color
   * @return self
   */
  @NotNull
  TextStyle backgroundColor(@Nullable BackgroundColor backgroundColor);

  /**
   * Set forced set font types
   *
   * @param textFonts the font types
   * @return self
   */
  @NotNull
  TextStyle font(@NotNull TextFont... textFonts);

  /**
   * Set forced set font types
   *
   * @param textFontSet the font types
   * @return self
   */
  @NotNull
  TextStyle font(@Nullable Collection<TextFont> textFontSet);

  /**
   * Set font option to font types
   *
   * @see FontMap#set(FontOption, TextFont...)
   * @param fontOption the font option
   * @param textFonts  the text fonts
   * @return self
   */
  @NotNull
  TextStyle font(@NotNull FontOption fontOption, @NotNull TextFont... textFonts);

  /**
   * Set font option to font types
   *
   * @see FontMap#set(FontOption, TextFont...)
   * @param fontOption  the font option
   * @param textFontSet the text font set
   * @return self
   */
  @NotNull
  TextStyle font(@NotNull FontOption fontOption, @Nullable Collection<TextFont> textFontSet);

  /**
   * Sets forced font types.
   *
   * @param textFonts the text fonts
   * @return self
   */
  @NotNull
  TextStyle setFont(@NotNull TextFont... textFonts);

  /**
   * Unset forced font types.
   *
   * @param textFonts the text fonts
   * @return self
   */
  @NotNull
  TextStyle unsetFont(@NotNull TextFont... textFonts);

  /**
   * Ignore font types.
   *
   * @param textFonts the text fonts
   * @return self
   */
  @NotNull
  TextStyle ignoreFont(@NotNull TextFont... textFonts);

  /**
   * Assign copied attributes from another text style.
   *
   * @param textStyle the text style
   * @return the text style
   */
  @NotNull TextStyle assignFrom(@NotNull TextStyle textStyle);

  /**
   * Get the font map
   *
   * @return the font map
   */
  @NotNull
  FontMap fontMap();

  /**
   * Copy all attributes.
   *
   * @return the copied text style
   */
  @NotNull
  TextStyle copy();

  /**
   * As explicit style text style with no ignoring colors.
   * Ignored colors (null) are replaced with default color {@link TerminalColor#DEFAULT}.
   *
   * @return the text style
   */
  TextStyle asExplicitStyle();

  /**
   * Get a default textstyle with no fonts and default colors.
   *
   * @return the textstyle
   */
  static @NotNull TextStyle getDefault() {
    FontMap fontMap = new FontMap();
    fontMap.unset(TextFont.values());
    return create(TerminalColor.DEFAULT, TerminalColor.DEFAULT, fontMap);
  }

  /**
   * Create text style. If the color is null, the color will be ignored.
   *
   * @param foregroundColor the foreground color
   * @param backgroundColor the background color
   * @param fonts           the fonts
   * @return the text style
   */
  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      TextFont...fonts) {
    FontMap fontMap = new FontMap();
    fontMap.set(fonts);
    return new TextStyleImpl(foregroundColor, backgroundColor, fontMap);
  }

  /**
   * Create text style. If the color is null, the color will be ignored.
   *
   * @param foregroundColor the foreground color
   * @param backgroundColor the background color
   * @param fontMap         the font map. If null {@code new FontMap()}
   * @return the text style
   */
  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor, @Nullable FontMap fontMap) {
    return new TextStyleImpl(foregroundColor, backgroundColor,
        fontMap == null ? new FontMap() : fontMap);
  }

  /**
   * Create text style with no fonts.
   * If the color is null, the color will be ignored.
   * @param foregroundColor the foreground color
   * @param backgroundColor the background color
   * @return the text style
   */
  static @NotNull TextStyle create(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor) {
    return new TextStyleImpl(foregroundColor, backgroundColor, new FontMap());
  }

  /**
   * Create text style with ignoring colors and no fonts
   *
   * @return the text style
   */
  static @NotNull TextStyle create() {
    return create(null, null);
  }

}
