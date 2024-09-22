package net.jterminal.instance;

import java.io.PrintStream;
import net.jterminal.Terminal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NonNativeTerminalProvider extends AbstractNonNativeTerminal<Terminal> {

  public NonNativeTerminalProvider() {
    super(Terminal.class);
  }

  public NonNativeTerminalProvider(@Nullable PrintStream out,
      @Nullable PrintStream err) {
    super(Terminal.class, out, err);
  }
}
