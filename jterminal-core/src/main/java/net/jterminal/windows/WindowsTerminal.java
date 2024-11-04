package net.jterminal.windows;

public interface WindowsTerminal {

  void setWindowVisible(boolean state);

  boolean isWindowVisible();

  void setGraphicUpdate(boolean state);

  void moveWindow(int x, int y, int width, int height);

  int[] getWindowPosDim();

  boolean isWindowOnFocus();

}
