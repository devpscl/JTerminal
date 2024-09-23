package net.jterminal.ansi;

import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.Combiner;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.element.TextElement;
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

  public @NotNull String serialize(@NotNull FontMap fontMap) {
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

  public @NotNull String serialize(@Nullable TextStyle textStyle) {
    StringBuilder buffer = new StringBuilder("\u001b[0m");
    if(textStyle == null) {
      return buffer.toString();
    }
    buffer.append(serialize(textStyle.foregroundColor()))
        .append(serialize(textStyle.backgroundColor()));
    for (Entry<TextFont, Boolean> entry : textStyle.fontMap().entrySet()) {
      if(entry.getValue()) {
        buffer.append(entry.getKey().getAnsiCode());
      }
    }
    return buffer.toString();
  }

  public @NotNull String serialize(@NotNull TextElement textElement) {
    return serialize(textElement, TextStyle.create());
  }

  public @NotNull String serialize(@NotNull TextElement textElement,
      @NotNull TextStyle textStyle) {
    TextStyle currentStyle = Combiner.combine(textElement.style(), textStyle);
    StringBuilder buffer = new StringBuilder();
    buffer.append(serialize(currentStyle))
        .append(textElement.value());
    List<TextElement> child = textElement.child();
    for (TextElement element : child) {
      buffer.append(serialize(element, currentStyle));
    }
    return buffer.toString();
  }

}
