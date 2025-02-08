package net.jterminal.ui.component;

import net.jterminal.ui.component.tool.ContainerViewArea;
import net.jterminal.ui.graphics.TermGraphics;
import net.jterminal.ui.graphics.shape.BoxShape;
import net.jterminal.ui.util.BoxCharacter;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.jetbrains.annotations.NotNull;

public class FrameContainer extends PaneContainer {

  private TextAlignment titleAlignment = TextAlignment.LEFT;
  private String title = "";
  private int titleOffset = 0;
  private BoxCharacter.Type boxLineType = Type.NORMAL;

  public FrameContainer() {

  }

  public FrameContainer(@NotNull String title) {
    this.title = title;
  }

  public FrameContainer(@NotNull String title, @NotNull TextAlignment titleAlignment) {
    this.title = title;
    this.titleAlignment = titleAlignment;
  }

  public FrameContainer(@NotNull String title, @NotNull TextAlignment titleAlignment, int offset) {
    this.title = title;
    this.titleAlignment = titleAlignment;
    this.titleOffset = offset;
  }

  public @NotNull Type boxLineType() {
    return boxLineType;
  }

  public void boxLineType(@NotNull Type boxLineType) {
    this.boxLineType = boxLineType;
  }

  public int titleOffset() {
    return titleOffset;
  }

  public void titleOffset(int titleOffset) {
    this.titleOffset = titleOffset;
  }

  public @NotNull String title() {
    return title;
  }

  public @NotNull TextAlignment titleAlignment() {
    return titleAlignment;
  }

  public void title(@NotNull String title) {
    this.title = title;
    repaint();
  }

  public void title(@NotNull String title, int offset) {
    this.title = title;
    this.titleOffset = offset;
    repaint();
  }

  public void titleAlignment(@NotNull TextAlignment titleAlignment) {
    this.titleAlignment = titleAlignment;
    repaint();
  }

  @Override
  public void paint(@NotNull TermGraphics graphics) {
    super.paint(graphics);
    TermDim size = graphics.size();
    BoxShape boxShape = new BoxShape(size);
    boxShape.addBox(1, 1, size.width(), size.height(), boxLineType);
    graphics.drawShape(1, 1, boxShape);
    if(!title.isEmpty()) {
      int x;
      if(titleAlignment == TextAlignment.CENTER) {
        x = (size.width() / 2) - (title.length() / 2) + 1 + titleOffset;
      } else if(titleAlignment == TextAlignment.RIGHT) {
        x = size.width() - titleOffset - title.length();
      } else {
        x = titleOffset + 1;
      }
      graphics.drawString(x, 1, title);
    }
  }

  @Override
  protected ContainerViewArea containerViewArea() {
    return new ContainerViewArea() {
      @Override
      public @NotNull TermPos origin(@NotNull TermDim containerSize) {
        return new TermPos(2, 2);
      }

      @Override
      public @NotNull TermDim dimension(@NotNull TermDim containerSize) {
        return containerSize
            .width(containerSize.width() - 2)
            .height(containerSize.height() - 2);
      }
    };
  }

}
