package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CellData {

  char EMPTY_SYMBOL = ' ';

  @Nullable ForegroundColor foregroundColor();

  @Nullable BackgroundColor backgroundColor();

  @NotNull TextFont[] fonts();

  @NotNull String ansiSequence();

  char symbol();

  @NotNull CellData newData(char symbol);

  @NotNull CellData newData(@
      Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor);

  @NotNull CellData newData(
      @Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      @NotNull TextFont[] fonts);

  static @NotNull CellData create() {
    return create(EMPTY_SYMBOL);
  }

  static @NotNull CellData create(char symbol) {
    return create(symbol, null, null, new TextFont[0]);
  }

  static @NotNull CellData create(char symbol,
      @Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      @NotNull TextFont[] fonts) {

    StringBuilder builder = new StringBuilder();
    if(foregroundColor != null) {
      builder.append(foregroundColor.getForegroundAnsiCode());
    }
    if(backgroundColor != null) {
      builder.append(backgroundColor.getBackgroundAnsiCode());
    }
    for (TextFont font : fonts) {
      builder.append(font.getResetAnsiCode());
    }

    return new CellDataImpl(symbol, foregroundColor, backgroundColor,
        fonts, builder.toString());
  }

  static @NotNull CellData empty(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor, TextFont... fonts) {
    return create(EMPTY_SYMBOL, foregroundColor, backgroundColor, fonts);
  }

  static @NotNull Builder builder() {
    return new CellDataBuilderImpl();
  }

  interface Builder {

    @NotNull Builder symbol(char c);

    @NotNull Builder foregroundColor(@Nullable ForegroundColor foregroundColor);

    @NotNull Builder backgroundColor(@Nullable BackgroundColor backgroundColor);

    @NotNull Builder fonts(@NotNull TextFont...fonts);

    @NotNull CellData build();

  }

}
