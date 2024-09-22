package net.jterminal.ansi;

import java.util.Map.Entry;
import java.util.Objects;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.FontMap;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnsiCodeSerializer {

  public static final AnsiCodeSerializer DEFAULT = new AnsiCodeSerializer();

  public @NotNull String serialize(@Nullable ForegroundColor foregroundColor) {
    return Objects.requireNonNullElse(foregroundColor, TerminalColor.DEFAULT)
        .getForegroundAnsiCode();
  }

  public @NotNull String serialize(@Nullable BackgroundColor backgroundColor) {
    return Objects.requireNonNullElse(backgroundColor, TerminalColor.DEFAULT)
        .getBackgroundAnsiCode();
  }

  public String serialize(@NotNull FontMap fontMap) {
    StringBuilder buffer = new StringBuilder();
    for (Entry<TextFont, Boolean> entry : fontMap.entrySet()) {
      TextFont font = entry.getKey();
      boolean state = entry.getValue();
      if(state) {
        buffer.append(font.getAnsiCode());
      } else {
        buffer.append(font.getResetAnsiCode());
      }
    }
    return buffer.toString();
  }

  public String serialize(@Nullable TextStyle textStyle) {
    if(textStyle == null) {
      return "\u001b[0m";
    }
    return "\u001b[0m" + serialize(textStyle.foregroundColor())
        + serialize(textStyle.backgroundColor())
        + serialize(textStyle.fontMap());
  }

}
