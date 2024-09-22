package net.jterminal.text.style;

import java.util.EnumMap;
import java.util.Set;
import net.jterminal.text.style.TextStyle.FontOption;
import org.jetbrains.annotations.NotNull;

public class FontMap extends EnumMap<TextFont, Boolean> {

  public FontMap() {
    super(TextFont.class);
  }

  public FontMap(EnumMap<TextFont, ? extends Boolean> m) {
    super(m);
  }

  public @NotNull FontOption optionOf(@NotNull TextFont font) {
    if(containsKey(font)) {
      return get(font) ? FontOption.SET : FontOption.UNSET;
    }
    return FontOption.IGNORED;
  }

  public void set(@NotNull FontOption fontOption, TextFont...textFonts) {
    switch (fontOption) {
      case SET -> set(textFonts);
      case UNSET -> unset(textFonts);
      case IGNORED -> ignore(textFonts);
    }
  }

  public void set(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      put(textFont, true);
    }
  }

  public void unset(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      put(textFont, false);
    }
  }

  public void ignore(TextFont...textFonts) {
    for (TextFont textFont : textFonts) {
      remove(textFont);
    }
  }

  public @NotNull Set<TextFont> fonts() {
    return keySet();
  }

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



}
