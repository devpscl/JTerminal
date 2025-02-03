package net.jterminal.ui.graphics;

import java.util.Arrays;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.style.TextFont;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CellDataImpl implements CellData {

  private final ForegroundColor foregroundColor;
  private final BackgroundColor backgroundColor;
  private final TextFont[] fonts;
  private final char symbol;
  private final String sequence;

  public CellDataImpl(char symbol, @Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor,
      TextFont[] fonts,
      @NotNull String sequence) {
    this.symbol = symbol <= 0x1F ? 0x20 : symbol;
    this.foregroundColor = foregroundColor;
    this.backgroundColor = backgroundColor;
    this.fonts = fonts;

    this.sequence = sequence;
  }

  @Override
  public @Nullable ForegroundColor foregroundColor() {
    return foregroundColor;
  }

  @Override
  public @Nullable BackgroundColor backgroundColor() {
    return backgroundColor;
  }

  @Override
  public @NotNull TextFont[] fonts() {
    return fonts;
  }

  @Override
  public char symbol() {
    return symbol;
  }

  @Override
  public @NotNull String ansiSequence() {
    return sequence;
  }

  @Override
  public @NotNull CellData newData(char symbol) {
    return CellData.create(symbol, foregroundColor, backgroundColor,
        Arrays.copyOf(fonts, fonts.length));
  }

  @Override
  public @NotNull CellData newData(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor) {
    return CellData.create(symbol, foregroundColor, backgroundColor,
        Arrays.copyOf(fonts, fonts.length));
  }

  @Override
  public @NotNull CellData newData(@Nullable ForegroundColor foregroundColor,
      @Nullable BackgroundColor backgroundColor, @NotNull TextFont[] fonts) {
    return CellData.create(symbol, foregroundColor, backgroundColor,
        Arrays.copyOf(fonts, fonts.length));
  }
}
