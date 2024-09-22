package net.jterminal.instance;


import java.io.PrintStream;
import net.jterminal.NativeTerminal;
import org.jetbrains.annotations.Nullable;

public class NativeTerminalProvider extends AbstractNativeTerminal<NativeTerminal>
    implements NativeTerminal {

  public NativeTerminalProvider() {
    super(NativeTerminal.class);
  }

  public NativeTerminalProvider(
      @Nullable PrintStream out,
      @Nullable PrintStream err) {
    super(NativeTerminal.class, out, err);
  }
}
