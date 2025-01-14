package net.jterminal.ui.util;

import org.jetbrains.annotations.NotNull;

public class ScrollbarGenerator {

  public static @NotNull ScrollbarRegion generate(
      int level, int maxLevel, double endShrinkLevel,
      int minBarLength,
      int maxBarLength,
      int totalLength) {

      level = Math.max(1, Math.min(maxLevel - 1, level - 1));

      double state = 1D - (maxLevel / endShrinkLevel);

      double lengthD = (maxBarLength-minBarLength) * state;
      int length = Math.max(minBarLength, (int) Math.floor(lengthD) + minBarLength);

      int halfLength = (int) Math.floor(length/2D);

      double startD = length / 2D;
      double endD = totalLength - startD;

      double posD = (endD-startD) * ((double) level / maxLevel);

      int pos = (int) Math.floor(posD + startD);

      int start = pos - halfLength;
      int end = pos + halfLength;
      return new ScrollbarRegion(start, end);
  }

  public record ScrollbarRegion(int start, int end) {}

}
