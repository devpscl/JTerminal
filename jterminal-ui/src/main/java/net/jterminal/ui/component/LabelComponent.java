package net.jterminal.ui.component;

import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import org.jetbrains.annotations.NotNull;

public class LabelComponent extends Component {

  private TermString text;

  public LabelComponent(@NotNull TermString text) {
    this.text = text;
    updateSize();
  }

  public LabelComponent() {
    this(TermString.empty());
  }

  private void updateSize() {
    int width = 0;
    TermString[] lines = text.split('\n');
    int height = lines.length;
    for (TermString line : lines) {
      if(line.length() > width) {
        width = line.length();
      }
    }
    size = new TermDim(width, height);
  }

  public @NotNull TermString text() {
    return text;
  }

  public void text(@NotNull TermString text) {
    this.text = text;
    updateSize();
    repaint();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    graphics.drawString(1, 1, text);
  }
}
