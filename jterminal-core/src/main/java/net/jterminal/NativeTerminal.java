package net.jterminal;


import net.jterminal.natv.NativeException;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.windows.WindowsTerminal;
import org.jetbrains.annotations.NotNull;

public interface NativeTerminal extends Terminal {

  int VERSION = 1;

  enum BufferId {
    MAIN,
    SECONDARY
  }

  void setBuffer(@NotNull BufferId buffer);

  @NotNull BufferId getBuffer();

  @NotNull WindowsTerminal windowsTerminal() throws UnsupportedSystemException, NativeException;

}
