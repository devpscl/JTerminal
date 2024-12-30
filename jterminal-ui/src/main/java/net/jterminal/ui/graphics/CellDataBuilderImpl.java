package net.jterminal.ui.graphics;

import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import net.jterminal.ui.graphics.CellData.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class CellDataBuilderImpl implements Builder {

  private char c = ' ';
  private ForegroundColor foregroundColor;
  private BackgroundColor backgroundColor;
  private TextFont[] fonts = new TextFont[0];

  @Override
  public @NotNull Builder symbol(char c) {
    this.c = c;
    return this;
  }

  @Override
  public @NotNull Builder foregroundColor(@Nullable ForegroundColor foregroundColor) {
    this.foregroundColor = foregroundColor;
    return this;
  }

  @Override
  public @NotNull Builder backgroundColor(@Nullable BackgroundColor backgroundColor) {
    this.backgroundColor = backgroundColor;
    return this;
  }

  @Override
  public @NotNull Builder fonts(@NotNull TextFont... fonts) {
    this.fonts = fonts;
    return this;
  }

  @Override
  public @NotNull CellData build() {
    return CellData.create(c, foregroundColor, backgroundColor, fonts);
  }
}
