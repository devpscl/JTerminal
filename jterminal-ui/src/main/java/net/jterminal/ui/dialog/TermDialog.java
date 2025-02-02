package net.jterminal.ui.dialog;

import net.jterminal.Terminal;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.UITerminal;
import net.jterminal.ui.component.Container;
import net.jterminal.ui.component.Resizeable;
import net.jterminal.ui.component.RootContainer;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class TermDialog extends RootContainer implements Resizeable {

  private boolean shadow = true;

  public TermDialog() {
    foregroundColor(TerminalColor.BLACK);
    backgroundColor(TerminalColor.GRAY);
  }

  public boolean shadowStyle() {
    return shadow;
  }

  public void shadowStyle(boolean shadow) {
    this.shadow = shadow;
    repaint();
  }

  @Override
  public void recalculateProperties() {
    synchronized (lock) {
      super.recalculateProperties();
      final Container parent = parent();
      if(parent == null) {
        return;
      }
      TermDim size = currentDimension();
      TermDim dim = parent.currentDimension();
      cachedPosition.x((dim.width() / 2) - (size.width() / 2))
          .y((dim.height() / 2) - (size.height() / 2));
    }
  }

  public void paintGlobal(@NotNull TermGraphics graphics) {
    TermPos origin = currentPosition();
    TermDim size = currentDimension();
    TermPos endCorner = origin.copy()
        .addX(size.width())
        .addY(size.height());
    if(shadow) {
      graphics.backgroundColor(TerminalColor.BLACK);
      graphics.fillRect(endCorner.x(), origin.y() + 1, 1, size.height(), ' ');
      graphics.fillRect(origin.x() + 1, endCorner.y(), size.width(), 1, ' ');
    }
  }

  public void openDialog() {
    if(parent() != null) {
      return;
    }
    Terminal terminal = Terminal.get();
    if(terminal instanceof UITerminal uiTerminal) {
      TermScreen screen = uiTerminal.openedScreen();
      if(screen == null) {
        return;
      }
      screen.openDialog(this);
    }
  }

  public void closeDialog() {
    TermScreen screen = screen();
    if(screen != null) {
      if(screen.openedDialog() != this) {
        return;
      }
      screen.closeDialog();
    }
  }

}
