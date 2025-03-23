package net.jterminal.text.style;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Set;
import net.jterminal.text.style.TextStyle.FontOption;
import org.jetbrains.annotations.NotNull;

/**
 * The Font map.
 */
public class FontMap extends EnumMap<TextFont, Boolean> {

  /**
   * Instantiates a new Font map with no fonts
   */
  public FontMap() {
    super(TextFont.class);
  }

  /**
   * Instantiates a new Font map from source.
   *
   * @param m enum map
   */
  public FontMap(EnumMap<TextFont, ? extends Boolean> m) {
    super(m);
  }

  /**
   * Gets the option of font.
   * @see FontOption
   *
   * @param font the font
   * @return the font option
   */
  public @NotNull FontOption optionOf(@NotNull TextFont font) {
    if(containsKey(font)) {
      return get(font) ? FontOption.SET : FontOption.UNSET;
    }
    return FontOption.IGNORED;
  }

  /**
   * Set option to multiple font type
   *
   * @param fontOption the font option
   * @param textFonts  the text fonts
   */
  public void set(@NotNull FontOption fontOption, TextFont...textFonts) {
    switch (fontOption) {
      case SET -> set(textFonts);
      case UNSET -> unset(textFonts);
      case IGNORED -> ignore(textFonts);
    }
  }

  /**
   * Force set fonts.
   *
   * @param textFonts the text fonts
   */
  public void set(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      put(textFont, true);
    }
  }

  /**
   * Force unset fonts.
   *
   * @param textFonts the text fonts
   */
  public void unset(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      put(textFont, false);
    }
  }

  /**
   * Ignore fonts. The font types are retained
   *
   * @param textFonts the text fonts
   */
  public void ignore(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      remove(textFont);
    }
  }

  /**
   *
   * @return the set of forced fonts
   */
  public @NotNull Set<TextFont> fonts() {
    return keySet();
  }

  /**
   * Get fonts by one option.
   *
   * @param filter the filter option
   * @return the set of font types
   */
  public @NotNull Set<TextFont> fonts(@NotNull FontOption filter) {
    Set<TextFont> fontSet = new HashSet<>();
    switch (filter) {
      case SET -> {
        for (Entry<TextFont, Boolean> entry : entrySet()) {
          if(entry.getValue()) {
            fontSet.add(entry.getKey());
          }
        }
      }
      case UNSET -> {
        for (Entry<TextFont, Boolean> entry : entrySet()) {
          if(!entry.getValue()) {
            fontSet.add(entry.getKey());
          }
        }
      }
      case IGNORED -> {
        for (TextFont value : TextFont.values()) {
          if(!containsKey(value)) {
            fontSet.add(value);
          }
        }
      }
    }
    return fontSet;
  }

  /**
   * Equals to another font map
   *
   * @param other the other
   * @return the boolean
   */
  public boolean equals(@NotNull FontMap other) {
    if(size() != other.size()) {
      return false;
    }
    for (TextFont value : TextFont.values()) {
      if(get(value) != other.get(value)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public String toString() {
    int fontSetBit = 0;
    int fontUnsetBit = 0;
    for (TextFont font : fonts(FontOption.SET)) {
      fontSetBit |= font.ordinal();
    }
    for (TextFont font : fonts(FontOption.UNSET)) {
      fontUnsetBit |= font.ordinal();
    }
    return "FontMap{" +  fontSetBit + ","
        + fontUnsetBit + "}";
  }


  public static @NotNull FontMap mapOfAll(@NotNull FontOption option) {
    FontMap map = new FontMap();
    map.set(option, TextFont.values());
    return map;
  }

}
