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
import net.jterminal.text.termstring.IndexedStyleData;
import net.jterminal.text.termstring.IndexedStyleData.IndexEntry;
import net.jterminal.text.termstring.TermString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnsiCodeSerializer {

  public static final AnsiCodeSerializer DEFAULT = new AnsiCodeSerializer(
      null, null);

  private final ForegroundColor defaultForegroundColor;
  private final BackgroundColor defaultBackgroundColor;

  public AnsiCodeSerializer(@Nullable ForegroundColor defaultForegroundColor,
      @Nullable BackgroundColor defaultBackgroundColor) {
    this.defaultForegroundColor = defaultForegroundColor;
    this.defaultBackgroundColor = defaultBackgroundColor;
  }

  public @NotNull ForegroundColor getDefaultForegroundColor() {
    return defaultForegroundColor == null ? TerminalColor.DEFAULT : defaultForegroundColor;
  }

  public @NotNull BackgroundColor getDefaultBackgroundColor() {
    return defaultBackgroundColor == null ? TerminalColor.DEFAULT : defaultBackgroundColor;
  }

  public @NotNull String serialize(@Nullable ForegroundColor foregroundColor) {
    if(foregroundColor == TerminalColor.DEFAULT) {
      foregroundColor = null;
    }
    return Objects.requireNonNullElse(foregroundColor, getDefaultForegroundColor())
        .getForegroundAnsiCode();
  }

  public @NotNull String serialize(@Nullable BackgroundColor backgroundColor) {
    if(backgroundColor == TerminalColor.DEFAULT) {
      backgroundColor = null;
    }
    return Objects.requireNonNullElse(backgroundColor, getDefaultBackgroundColor())
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

  public @NotNull String serialize(@NotNull TermString termString) {
    String value = termString.raw();
    IndexedStyleData data = termString.data();
    int offset = 0;
    TextStyle prevStyle = TextStyle.create();
    StringBuilder stringBuilder = new StringBuilder(value);
    for (IndexEntry entry : data.indexes()) {
      prevStyle = Combiner.combine(entry.textStyle(), prevStyle);
      String ansi = serialize(prevStyle);
      int index = entry.index() + offset;
      if(index < stringBuilder.length()) {
        stringBuilder.insert(index, ansi);
      } else {
        stringBuilder.append(ansi);
      }
      offset += ansi.length();
    }
    return stringBuilder.toString();
  }

}
