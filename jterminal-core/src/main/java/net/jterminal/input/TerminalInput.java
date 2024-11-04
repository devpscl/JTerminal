package net.jterminal.input;

import java.time.Duration;
import net.jterminal.Terminal;
import net.jterminal.Terminal.PropertyManager;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.natv.NativeException;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.util.Ref;
import org.jetbrains.annotations.NotNull;

public interface TerminalInput {

  enum SourceType {
    NATIVE,
    JAVA_IO,
    AUTO
  }

  int capacity();

  int available();

  int read();

  int read(byte @NotNull [] bytes);

  int read(byte @NotNull [] bytes, @NotNull Duration timeout);

  int read(byte @NotNull [] bytes, int off, int len);

  int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration timeout);

  int peek(byte @NotNull [] bytes);

  int peek(byte @NotNull [] bytes, int off, int len);

  int peekEvent(@NotNull Ref<InputEvent> ref);

  InputEvent readEvent();

  InputEvent readEvent(@NotNull Duration timeout);

  void reset();

  void close();

  boolean isClosed();

  boolean isClosable();

  boolean isNative();

  static @NotNull TerminalInput create(int capacity) {
    if(PropertyManager.isNativeEnabled()) {
      NativeTerminalInput terminalInput = new NativeTerminalInput(capacity);
      try {
        terminalInput.init();
        return terminalInput;
      } catch (UnsupportedSystemException | NativeException ex) {
        Terminal.LOGGER.error("Failed to create native input handler", ex);
      }
    }
    NonNativeTerminalInput terminalInput = new NonNativeTerminalInput(capacity);
    terminalInput.init();
    return terminalInput;
  }

}
