package net.jterminal.input;

import java.time.Duration;
import net.jterminal.NativeTerminal;
import net.jterminal.annotation.NativeType;
import net.jterminal.natv.NativeException;
import net.jterminal.natv.NativeLoader;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.util.PointerRef;
import net.jterminal.util.Ref;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;

@Internal
@NativeType(name = "termengine", version = NativeTerminal.VERSION)
class NativeTerminalInput implements TerminalInput {

  private final PointerRef ptrRef = new PointerRef();
  private final int capacity;

  public NativeTerminalInput(int capacity) {
    this.capacity = capacity;
  }

  public void init() throws UnsupportedSystemException, NativeException {
    if(!NativeLoader.isLoaded(NativeTerminalInput.class)) {
      NativeLoader.load(NativeTerminalInput.class);
    }
    ptrRef.set(_createInputStream(capacity));
  }

  private static void loadLibrary(@NotNull String path) {
    System.load(path);
  }

  @Override
  public int capacity() {
    return capacity;
  }

  @Override
  public int available() {
    return _avail(ptrRef.get());
  }

  @Override
  public int read() {
    return _read(ptrRef.get());
  }

  @Override
  public int read(byte @NotNull [] bytes) {
    return _read(ptrRef.get(), bytes, 0, bytes.length, -1);
  }

  @Override
  public int read(byte @NotNull [] bytes, @NotNull Duration timeout) {
    return read(bytes, 0, bytes.length, timeout);
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len) {
    return _read(ptrRef.get(), bytes, off, len, -1);
  }

  @Override
  public int read(byte @NotNull [] bytes, int off, int len, @NotNull Duration timeout) {
    return _read(ptrRef.get(), bytes, off, len, timeout.toMillis());
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
  public int peekEvent(@NotNull Ref<InputEvent> ref) {
    return _peekInput(ptrRef.get(), ref);
  }

  @Override
  public InputEvent readEvent() {
    return _readInput(ptrRef.get(), -1);
  }

  @Override
  public InputEvent readEvent(@NotNull Duration timeout) {
    return _readInput(ptrRef.get(), timeout.toMillis());
  }

  @Override
  public void reset() {
    _reset(ptrRef.get());
  }

  @Override
  public void close() {
    if(!isClosed()) {
      _disposeInputStream(ptrRef.get());
      ptrRef.setNull();
    }
  }

  @Override
  public boolean isClosed() {
    return ptrRef.isNull();
  }

  @Override
  public boolean isClosable() {
    return true;
  }

  @Override
  public boolean isNative() {
    return true;
  }

  protected native long _createInputStream(int capacity);

  protected native void _disposeInputStream(long inputStreamPtr);

  protected native int _read(long ptr);

  protected native int _avail(long ptr);

  protected native int _peek(long ptr, byte[] bytes, int off, int len);

  protected native int _read(long ptr, byte[] bytes, int off, int len, long timeout);

  protected native int _peekInput(long ptr, Ref<InputEvent> ref);

  protected native InputEvent _readInput(long ptr, long timeout);

  protected native void _reset(long ptr);

}
