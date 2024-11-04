package net.jterminal.cli.line;

import net.jterminal.cli.CLITerminal;
import net.jterminal.input.KeyboardInputEvent;
import net.jterminal.util.SetLock;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
public interface InternalLineReader {

  @NotNull SetLock<CLITerminal> getSetLock();

  void handleKeyboardEvent(@NotNull KeyboardInputEvent inputEvent);

}
