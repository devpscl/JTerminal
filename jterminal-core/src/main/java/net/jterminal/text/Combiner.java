package net.jterminal.text;

import net.jterminal.text.style.FontMap;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Combiner {

  static <T> @Nullable T combine(@Nullable T mostPrioritized,
      @Nullable T leastPrioritized) {
    if(mostPrioritized != null) {
      return mostPrioritized;
    }
    return leastPrioritized;
  }

  static @NotNull TextStyle combine(@NotNull TextStyle mostPrioritizedStyle,
      @NotNull TextStyle leastPrioritizedStyle) {
    FontMap fontMap = combine(mostPrioritizedStyle.fontMap(), leastPrioritizedStyle.fontMap());
    ForegroundColor fgc = combine(mostPrioritizedStyle.foregroundColor(),
        leastPrioritizedStyle.foregroundColor());
    BackgroundColor bgc = combine(mostPrioritizedStyle.backgroundColor(),
        leastPrioritizedStyle.backgroundColor());
    return TextStyle.create(fgc, bgc, fontMap);
  }

  static @NotNull FontMap combine(@NotNull FontMap mostPrioritizedFontMap,
      @NotNull FontMap leastPrioritizedFontMap) {
    FontMap fontMap = new FontMap();
    for (TextFont font : TextFont.values()) {
      Boolean state = mostPrioritizedFontMap.get(font);
      if(state != null) {
        fontMap.put(font, state);
        continue;
      }
      state = leastPrioritizedFontMap.get(font);
      if(state != null) {
        fontMap.put(font, state);
      }
    }
    return fontMap;
  }

}
