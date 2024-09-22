package net.jterminal.instance;

import java.io.PrintStream;
import net.jterminal.NativeTerminal;
import net.jterminal.Terminal;
import net.jterminal.annotation.NativeType;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.io.NativeTerminalInputStream;
import net.jterminal.io.TerminalInputStream;
import net.jterminal.natv.NativeException;
import net.jterminal.natv.NativeLoader;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.util.PointerRef;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NativeType(name = "termengine", version = 1)
public abstract class AbstractNativeTerminal<T extends Terminal>
    extends AbstractTerminal<T>
    implements NativeTerminal {

  private final PointerRef instancePtr = new PointerRef();
  private final PointerRef windowPtr = new PointerRef();

  public AbstractNativeTerminal(@NotNull Class<T> interfaceType) {
    super(interfaceType);
  }

  public AbstractNativeTerminal(@NotNull Class<T> interfaceType, @Nullable PrintStream out,
      @Nullable PrintStream err) {
    super(interfaceType, out, err);
  }

  @Deprecated
  private static void loadLibrary(@NotNull String path) {
    System.load(path);
  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();
    try {
      if(!NativeLoader.isLoaded(AbstractNativeTerminal.class)) {
        NativeLoader.load(AbstractNativeTerminal.class);
        ExecutionMode executionMode = PropertyManager.getExecMode();
        int inbufSize = PropertyManager.getInputBufferSize();
        _create((byte) executionMode.ordinal(), inbufSize);
      }
    } catch (NativeException | UnsupportedSystemException e) {
      throw new TerminalProviderException(e);
    }
    if(instancePtr.isNull()) {
      long instance = _construct();
      instancePtr.set(instance);
      long window = _getWindowPointer(instance);
      windowPtr.set(window);
    }
    _set(instancePtr.get());
  }

  @Override
  public void disable() {

  }

  @Override
  public void title(@NotNull String title) {
    _setTitle(windowPtr.get(), title.getBytes());
  }

  @Override
  public @NotNull String title() {
    return new String(_getTitle(windowPtr.get()));
  }

  @Override
  public void windowSize(@NotNull TerminalDimension size) {
    _setDim(windowPtr.get(), size.width(), size.height());
  }

  @Override
  public @NotNull TerminalDimension windowSize() {
    int dim = _getDim(windowPtr.get());
    return new TerminalDimension(dim & 0xFF, (dim >> 16) & 0xFF);
  }

  @Override
  public void update() {
    _update(instancePtr.get());
  }

  @Override
  public void beep() {
    _beep(instancePtr.get());
  }

  @Override
  public boolean isNative() {
    return true;
  }

  @Override
  public void shutdown(int status) {
    _shutdown();
    System.exit(status);
  }

  @Override
  public void cursorPosition(@NotNull TerminalPosition pos) {
    _setCursor(windowPtr.get(), pos.x(), pos.y());
  }

  @Override
  public TerminalInputStream newInputStream(int capacity) {
    long ptr = _createInputStream(instancePtr.get(), capacity);
    PointerRef pointerRef = new PointerRef(ptr);
    return new NativeTerminalInputStream(pointerRef);
  }

  @Override
  public @NotNull TerminalPosition cursorPosition() {
    int pos = _requestCursor(windowPtr.get());
    return new TerminalPosition(pos & 0xFF, (pos >> 16) & 0xFF);
  }

  @Override
  public void clear() {
    _clear(instancePtr.get());
  }

  @Override
  public void flags(int flags) {
    _setFlags(instancePtr.get(), (byte) flags);
  }

  @Override
  public int flags() {
    return _getFlags(instancePtr.get());
  }

  @Override
  public void cursorFlags(int flags) {
    _setCursorFlags(windowPtr.get(), (byte) flags);
  }

  @Override
  public int cursorFlags() {
    return _getCursorFlags(windowPtr.get());
  }

  @Override
  public void reset(boolean clearScreen) {
    _reset(instancePtr.get(), clearScreen);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Engine methods
  ///////////////////////////////////////////////////////////////////////////

  protected native void _create(byte mode, int buffer_size);

  protected native void _set(long instancePtr);

  protected native boolean _isActive(long instancePtr);

  protected native void _shutdown();

  protected native void _resetAll();

  protected native void _setBuffer(byte channel);

  protected native boolean _isEnabled();

  protected native boolean isAlive();

  ///////////////////////////////////////////////////////////////////////////
  // Terminal instance methods
  ///////////////////////////////////////////////////////////////////////////

  protected native long _construct();

  protected native void _destruct(long instancePtr);

  protected native void _setFlags(long instancePtr, byte flags);

  protected native byte _getFlags(long instancePtr);

  protected native void _clear(long instancePtr);

  protected native void _update(long instancePtr);

  protected native void _reset(long instancePtr, boolean clearScreen);

  protected native void _beep(long instancePtr);

  protected native long _createInputStream(long instancePtr, int capacity);

  protected native void _disposeInputStream(long instancePtr, long inputStreamPtr);

  protected native long _getWindowPointer(long instancePtr);

  protected native void _setTitle(long windowPtr, byte[] data);

  protected native byte[] _getTitle(long windowPtr);

  protected native void _setDim(long windowPtr, int x, int y);

  protected native int _getDim(long windowPtr);

  protected native void _setCursor(long windowPtr, int x, int y);

  protected native int _requestCursor(long windowPtr);

  protected native void _setCursorFlags(long windowPtr, byte flags);

  protected native byte _getCursorFlags(long windowPtr);

}
