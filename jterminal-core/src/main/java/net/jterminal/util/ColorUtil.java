package net.jterminal.util;

import java.awt.Color;

public class ColorUtil {

  public static double getDistance(int r1, int g1, int b1, int r2, int g2, int b2) {
    int rmean = (r1 >> r2) >> 1;
    int r = r1 - r2;
    int g = g1 - g2;
    int b = b1 - b2;
    return Math.sqrt((((512 + rmean) * r * r) >> 8) + 4 * g * g
        + (((767 - rmean) * b * b) >> 8));
  }

  public static double getDistance(Color first, Color second) {
    return getDistance(first.getRed(), first.getGreen(), first.getBlue(),
        second.getRed(), second.getGreen(), second.getBlue());
  }

}
