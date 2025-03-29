package net.jterminal.ui.component;

import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.util.TextAlignment;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class LabelComponent extends Component implements Resizeable {

  private TermString text;
  private TextAlignment textAlignment;
  private boolean autoWrapLines;

  public LabelComponent(@NotNull TermString text) {
    this(text, TextAlignment.LEFT);
  }

  public LabelComponent(@NotNull TermString text, @NotNull TextAlignment alignment) {
    this.text = text;
    this.textAlignment = alignment;
    updateSize();
  }

  public LabelComponent() {
    this(TermString.empty());
  }

  public boolean autoWrapLines() {
    return autoWrapLines;
  }

  public void autoWrapLines(boolean autoWrapLines) {
    this.autoWrapLines = autoWrapLines;
    repaint();
  }

  public @NotNull TextAlignment textAlignment() {
    return textAlignment;
  }

  public void textAlignment(@NotNull TextAlignment textAlignment) {
    this.textAlignment = textAlignment;
    repaint();
  }

  public void updateSize() {
    int width = 0;
    TermString[] lines = text.split('\n');
    int height = lines.length;
    for (TermString line : lines) {
      if(line.length() > width) {
        width = line.length();
      }
    }
    setWidth(width);
    setHeight(height);
  }

  public @NotNull TermString text() {
    return text;
  }

  public void text(@NotNull TermString text) {
    this.text = text;
    repaint();
  }

  public void text(@NotNull TermString text, boolean updateSize) {
    this.text = text;
    if(updateSize) {
      updateSize();
    }
    repaint();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    int width = graphics.size().width();
    TermString[] lines = autoWrapLines ? text.wrapLines(width) : text.split('\n');
    TermDim size = currentDimension();
    int midX = (size.width() / 2);
    int y = 1;
    int x;
    for (TermString line : lines) {
      int length = line.length();
      if(textAlignment == TextAlignment.RIGHT) {
        x = size.width() - length;
      } else if(textAlignment == TextAlignment.CENTER) {
        x = midX - (length / 2) + 1;
      } else {
        x = 1;
      }
      graphics.drawString(x, y, line);
      y++;
    }
  }
}
