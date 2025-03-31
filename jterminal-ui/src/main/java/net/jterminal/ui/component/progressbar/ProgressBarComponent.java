package net.jterminal.ui.component.progressbar;

import net.jterminal.ui.component.Component;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.layout.Layout.DimensionValue;
import net.jterminal.ui.layout.Layout.Modifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class ProgressBarComponent extends Component implements Resizeable {

  protected ProgressBarStyle style = new LegacyProgressBarStyle();
  protected float progress;

  public ProgressBarComponent() {
    this(0F);
  }

  public ProgressBarComponent(@Range(from = 0, to = 1) float progress) {
    setHeight(1);
    setWidth(6);
    progress(progress);
  }

  public void progress(@Range(from = 0, to = 1) float value) {
    this.progress = Math.min(1F, Math.max(0F, value));
    repaint();
  }

  public float progress() {
    return progress;
  }

  public @NotNull ProgressBarStyle style() {
    return style;
  }

  public void style(@NotNull ProgressBarStyle style) {
    this.style = style;
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    style.paint(graphics, currentDimension().width(), progress);
  }

  @Deprecated
  @Override
  public void height(int value, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }

  @Deprecated
  @Override
  public void height(DimensionValue positionValue, Modifier... modifiers) {
    throw new IllegalStateException("Height change is not supported!");
  }


}
