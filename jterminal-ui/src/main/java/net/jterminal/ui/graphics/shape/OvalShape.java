package net.jterminal.ui.graphics.shape;

import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class OvalShape implements TermShape {

  private final TermDim dimension;
  private final char symbol;
  private float densityModifier = 1.0F;

  public OvalShape(@NotNull TermDim dimension, char symbol) {
    this.dimension = dimension;
    this.symbol = symbol;
  }

  public float densityModifier() {
    return densityModifier;
  }

  public void densityModifier(float densityModifier) {
    this.densityModifier = densityModifier;
  }

  @Override
  public @NotNull TermDim size() {
    return dimension;
  }

  @Override
  public void render(@NotNull TermGraphics graphics, @NotNull TermPos pos,
      @NotNull TermDim dim) {

    double radiusX = (dim.width() - 1) / 2D;
    double radiusY = (dim.height() - 1) / 2D;
    double cx = pos.x() + radiusX;
    double cy = pos.y() + radiusY;
    double density = (radiusX + radiusY) * 2 * densityModifier;
    for(double theta = 0; theta <= Math.PI * 2; theta += (Math.PI / density)) {
      double cosx = Math.cos(theta);
      double siny = Math.sin(theta);
      double x = cosx * radiusX + cx;
      double y = siny * radiusY + cy;
      graphics.draw((int)Math.round(x), (int)Math.round(y), symbol);
    }

  }
}
