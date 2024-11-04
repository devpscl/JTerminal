package net.jterminal.windows;

import net.jterminal.NativeTerminal;
import net.jterminal.annotation.NativeType;
import org.jetbrains.annotations.NotNull;

@NativeType(name = "termengine", version = NativeTerminal.VERSION)
public class WindowsTerminalImpl implements WindowsTerminal {

  @Deprecated
  private static void loadLibrary(@NotNull String path) {
    System.load(path);
  }

  @Override
  public native void setWindowVisible(boolean state);

  @Override
  public native boolean isWindowVisible();

  @Override
  public native void setGraphicUpdate(boolean state);

  @Override
  public native void moveWindow(int x, int y, int width, int height);

  @Override
  public native int[] getWindowPosDim();

  @Override
  public native boolean isWindowOnFocus();

}
