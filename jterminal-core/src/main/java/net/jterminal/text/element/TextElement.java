package net.jterminal.text.element;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.text.style.TextStyle;
import net.jterminal.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TextElement {

  @NotNull TextElement foregroundColor(@Nullable ForegroundColor foregroundColor);

  @Nullable ForegroundColor foregroundColor();

  @NotNull TextElement backgroundColor(@Nullable BackgroundColor backgroundColor);

  @Nullable BackgroundColor backgroundColor();

  @NotNull Set<TextFont> fonts();

  @NotNull TextElement fonts(TextFont...fonts);

  @NotNull TextElement value(@NotNull String value);

  @NotNull String value();

  @NotNull TextElement child(TextElement...elements);

  @NotNull TextElement child(Collection<TextElement> collection);

  @NotNull List<TextElement> child();

  @NotNull TextStyle style();

  @NotNull TextElement style(@NotNull TextStyle style);

  static @NotNull TextElement create(@NotNull String value) {
    return new TextElementImpl(value, TextStyle.create());
  }

  static @NotNull TextElement create(@NotNull String value, @NotNull TextStyle textStyle) {
    return new TextElementImpl(value, textStyle);
  }

  static @NotNull TextElement empty() {
    return create(StringUtil.EMPTY_STRING);
  }

  static @NotNull TextElement empty(@NotNull TextStyle textStyle) {
    return create(StringUtil.EMPTY_STRING, textStyle);
  }

  static @NotNull TextElement create(int value) {
    return new TextElementImpl(String.valueOf(value), TextStyle.create());
  }

  static @NotNull TextElement create(double value) {
    return new TextElementImpl(String.valueOf(value), TextStyle.create());
  }

  static @NotNull TextElement create(boolean value) {
    return new TextElementImpl(String.valueOf(value), TextStyle.create());
  }

  static @NotNull TextElement create(char value) {
    return new TextElementImpl(String.valueOf(value), TextStyle.create());
  }

  static @NotNull TextElement create(Object value) {
    return new TextElementImpl(String.valueOf(value), TextStyle.create());
  }

}
