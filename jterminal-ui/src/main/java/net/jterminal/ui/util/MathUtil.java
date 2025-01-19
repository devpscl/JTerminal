package net.jterminal.ui.util;

public class MathUtil {

  public static int range(int value, int min, int max) {
    return Math.min(max, Math.max(value, min));
  }

  public static int nonNegative(int value) {
    return Math.max(value, 0);
  }

}
