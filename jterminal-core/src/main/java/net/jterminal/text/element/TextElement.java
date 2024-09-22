package net.jterminal.text.element;

import java.util.Set;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
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

  @Nullable String value();

  @NotNull TextElement child(TextElement...elements);

  @NotNull Set<TextElement> child();

  static @NotNull TextElement create(@NotNull String value) {
    return new TextElementImpl(value, null, null, null);
  }



}
