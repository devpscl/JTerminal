package net.jterminal.instance;

import java.io.PrintStream;
import net.jterminal.NativeTerminal;
import net.jterminal.Terminal;
import net.jterminal.annotation.NativeType;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.natv.NativeException;
import net.jterminal.natv.NativeLoader;
import net.jterminal.system.OperationSystem;
import net.jterminal.system.SystemInfo;
import net.jterminal.system.UnsupportedSystemException;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import net.jterminal.windows.WindowsTerminal;
import net.jterminal.windows.WindowsTerminalImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@NativeType(name = "termengine", version = NativeTerminal.VERSION)
public abstract class AbstractNativeTerminal<T extends Terminal>
    extends AbstractTerminal<T>
    implements NativeTerminal {

  private static TermDim defaultWindowSize = null;

  private static InputEventListener inputEventListener = null;

  private int flags = Terminal.FLAG_LINE_INPUT | Terminal.FLAG_ECHO |
      Terminal.FLAG_SIGNAL_INPUT;
  private int cursorFlags = Terminal.CURSOR_BLINKING | Terminal.CURSOR_VISIBLE;
  private String title = "Terminal";
  private BufferId buffer = BufferId.MAIN;

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
  public void initialize() throws TerminalProviderException {
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
  }

  @Override
  public void enable() throws TerminalProviderException {
    super.enable();

    _setTitle(title.getBytes());
    _setFlags((byte) flags);
    _setCursorFlags((byte) cursorFlags);
    _setBuffer((byte) buffer.ordinal());
    updateInputEventListener();

    if(defaultWindowSize == null) {
      defaultWindowSize = windowSize();
    }
  }

  @Override
  public void disable() {
    updateInputEventListener();
  }

  @Override
  public void title(@NotNull String title) {
    _setTitle(title.getBytes());
    this.title = title;
  }

  @Override
  public @NotNull String title() {
    return title;
  }

  @Override
  public void windowSize(@NotNull TermDim size) {
    if(!isEnabled()) {
      return;
    }
    _setDim(size.width(), size.height());
  }

  @Override
  public @NotNull TermDim windowSize() {
    int dim = _getDim();
    return new TermDim(dim & 0xFF, (dim >> 16) & 0xFF);
  }

  @Override
  public @NotNull TermDim defaultWindowSize() {
    return defaultWindowSize;
  }

  @Override
  public void update() {
    if(!isEnabled()) {
      return;
    }
    _update();
  }

  @Override
  public void beep() {
    _beep();
  }

  @Override
  public boolean isNative() {
    return true;
  }

  @Override
  public void shutdown(int status) {
    _shutdown();
    if(inputEventListener != null) {
      inputEventListener.stop();
    }
    System.exit(status);
  }

  @Override
  public void cursorPosition(@NotNull TermPos pos) {
    if(!isEnabled()) {
      return;
    }
    _setCursor(pos.x(), pos.y());
  }

  @Override
  public @NotNull TermPos cursorPosition() {
    int pos = _requestCursor();
    return new TermPos(pos & 0xFF, (pos >> 16) & 0xFF);
  }

  @Override
  public void clear() {
    if(!isEnabled()) {
      return;
    }
    _clear();
  }

  @Override
  public void flags(int flags) {
    if(!isEnabled()) {
      return;
    }
    _setFlags((byte) flags);
    this.flags = flags;
  }

  @Override
  public void resetFlags() {
    flags(defaultFlags);
  }

  @Override
  public int flags() {
    return flags;
  }

  @Override
  public void cursorFlags(int flags) {
    this.cursorFlags = flags;
    if(!isEnabled()) {
      return;
    }
    _setCursorFlags((byte) flags);
  }

  @Override
  public int cursorFlags() {
    return cursorFlags;
  }

  @Override
  public void reset(boolean clearScreen) {
    flags = Terminal.FLAG_LINE_INPUT | Terminal.FLAG_ECHO;
    cursorFlags = Terminal.CURSOR_BLINKING | Terminal.CURSOR_VISIBLE;
    title = "Terminal";
    if(!isEnabled()) {
      return;
    }
    _reset(clearScreen);
  }

  @Override
  public void setBuffer(@NotNull BufferId buffer) {
    this.buffer = buffer;
    if(!isEnabled()) {
      return;
    }
    _setBuffer((byte) buffer.ordinal());
  }

  @Override
  public @NotNull BufferId getBuffer() {
    return buffer;
  }

  @Override
  public void inputEventListenerEnabled(boolean state) {
    super.inputEventListenerEnabled(state);
    if(!isEnabled()) {
      return;
    }
    updateInputEventListener();
  }

  @Override
  public void waitFutureShutdown() {
    super.waitFutureShutdown();
    _joinFutureClose();
  }

  @Override
  public @NotNull WindowsTerminal windowsTerminal()
      throws UnsupportedSystemException, NativeException {
    if(SystemInfo.current().os() != OperationSystem.WINDOWS) {
      throw new UnsupportedSystemException("Windows is required");
    }
    if(!NativeLoader.isLoaded(WindowsTerminalImpl.class)) {
      NativeLoader.load(WindowsTerminalImpl.class);
    }
    return new WindowsTerminalImpl();
  }

  public void updateInputEventListener() {
    if(inputEventListener == null) {
      if(!inputEventListening) {
        return;
      }
      inputEventListener = new InputEventListener(128);
      inputEventListener.start();
    }
    if(inputEventListening) {
      inputEventListener.resume();
    } else {
      inputEventListener.pause();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // Engine methods
  ///////////////////////////////////////////////////////////////////////////

  protected native void _create(byte mode, int buffer_size);

  protected native void _shutdown();

  protected native void _joinFutureClose();

  protected native boolean _isEnabled();

  protected native void _setFlags(byte flags);

  protected native byte _getFlags();

  protected native void _clear();

  protected native void _update();

  protected native void _reset(boolean clearScreen);

  protected native void _beep();

  protected native void _setBuffer(byte buf);

  protected native byte _getBuffer();

  protected native void _setTitle(byte[] data);

  protected native byte[] _getTitle();

  protected native void _setDim(int x, int y);

  protected native int _getDim();

  protected native void _setCursor(int x, int y);

  protected native int _requestCursor();

  protected native void _setCursorFlags(byte flags);

  protected native byte _getCursorFlags();

}
