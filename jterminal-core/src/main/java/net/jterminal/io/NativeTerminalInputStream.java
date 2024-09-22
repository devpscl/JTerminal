package net.jterminal.io;

import java.io.IOException;
import java.time.Duration;
import net.jterminal.annotation.NativeType;
import net.jterminal.input.InputEvent;
import net.jterminal.instance.AbstractNativeTerminal;
import net.jterminal.natv.NativeException;
import net.jterminal.natv.NativeLoader;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.util.PointerRef;
import net.jterminal.util.Ref;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
@NativeType(name = "termengine", version = 1)
public class NativeTerminalInputStream extends TerminalInputStream {

  private final PointerRef ptrRef;

  public NativeTerminalInputStream(@NotNull PointerRef pointerRef) {
    this.ptrRef = pointerRef;
  }

  private static void loadLibrary(@NotNull String path) {
    System.load(path);
  }

  @Internal
  public static void load() throws UnsupportedSystemException, NativeException {
    if(!NativeLoader.isLoaded(AbstractNativeTerminal.class)) {
      NativeLoader.load(AbstractNativeTerminal.class);
    }
  }

  @Override
  public int available() {
    return _avail(ptrRef.get());
  }

  @Override
  public int read() throws IOException {
    return _read(ptrRef.get());
  }

  @Override
  public int read(byte @NotNull [] bytes) {
    return _read(ptrRef.get(), bytes, 0, bytes.length, -1);
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len) {
    return _read(ptrRef.get(), bytes, off, len, -1);
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration duration) {
    return _read(ptrRef.get(), bytes, off, len, duration.toMillis());
  }

  @Override
  public int peek(byte @NotNull [] bytes) {
    return _peek(ptrRef.get(), bytes, 0, bytes.length);
  }

  @Override
  public int peek(byte @NotNull [] bytes, int off, int len) {
    return _peek(ptrRef.get(), bytes, off, len);
  }

  @Override
  public int peekInputEvent(@NotNull Ref<InputEvent> ref) {
    return _peekInput(ptrRef.get(), ref);
  }

  @Override
  public InputEvent readInputEvent() {
    return _readInput(ptrRef.get(), -1);
  }

  @Override
  public InputEvent readInputEvent(@NotNull Duration duration) {
    return _readInput(ptrRef.get(), duration.toMillis());
  }

  @Override
  public void reset() {
    _reset(ptrRef.get());
  }

  @Override
  public void close() {
    if(ptrRef.isNull()) {
      return;
    }
    _close(ptrRef.get());
    ptrRef.setNull();
  }

  @Override
  public boolean isClosed() {
    return ptrRef.isNull();
  }

  protected native int _read(long ptr);

  protected native int _avail(long ptr);

  protected native int _peek(long ptr, byte[] bytes, int off, int len);

  protected native int _read(long ptr, byte[] bytes, int off, int len, long timeout);

  protected native int _peekInput(long ptr, Ref<InputEvent> ref);

  protected native InputEvent _readInput(long ptr, long timeout);

  protected native void _reset(long ptr);

  protected native void _close(long ptr);

}
