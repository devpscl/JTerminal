package net.jterminal;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import net.jterminal.event.TerminalDisableEvent;
import net.jterminal.event.TerminalEnableEvent;
import net.jterminal.event.bus.EventBus;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.exception.TerminalRuntimeException;
import net.jterminal.instance.AbstractTerminal;
import net.jterminal.instance.NativeTerminalProvider;
import net.jterminal.instance.NonNativeTerminalProvider;
import net.jterminal.text.BackgroundColor;
import net.jterminal.text.ForegroundColor;
import net.jterminal.text.element.TextElement;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.TermDim;
import net.jterminal.util.TermPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Terminal is the interface class that represents a terminal instance.
 * A terminal instance is unique and can be created in any number. This allows you to manage
 * the command line, read input, write output and more. Only one instance can be used at a time.
 * All inactive instances cannot produce any changes in the terminal. A terminal instance
 * can originate from different providers, which influence the internal functionality differently.
 * <br>
 * Depending on the system, an instance can be created automatically by {@link Terminal#auto()}.
 * An instance can be explicitly created by the provider via {@link Terminal#create(Class)} and all
 * instances already created can be activated via {@link Terminal#set(Terminal)}, whereby only one can
 * be represented as an active instance at a time
 *
 */
public interface Terminal {

  Logger LOGGER = LogManager.getLogger();

  /**
   * Default input from file descriptor 0
   */
  InputStream FD_IN = new FileInputStream(FileDescriptor.in);
  /**
   * Default output from file descriptor 1
   */
  OutputStream FD_OUT = new FileOutputStream(FileDescriptor.out);
  /**
   * Default error output from file descriptor 2
   */
  OutputStream FD_ERR = new FileOutputStream(FileDescriptor.err);


  int FLAG_LINE_INPUT = 0x1;
  int FLAG_ECHO = 0x2;
  int FLAG_MOUSE_INPUT = 0x4;
  int FLAG_MOUSE_EXTENDED_INPUT = 0x8;
  int FLAG_EXTENDED_INPUT = 0x10;
  int FLAG_SIGNAL_INPUT = 0x20;
  int FLAG_WINDOW_INPUT = 0x40;


  int CURSOR_VISIBLE = 0x1;
  int CURSOR_BLINKING = 0x2;

  /**
   * Writes an object to the output. The object is converted into <br>
   * a string by {@link String#valueOf(Object)}.
   *
   * @param obj the object
   */
  void write(@NotNull Object obj);

  /**
   * Writes {@code len} bytes from byte array that begins at offset {@code off}.
   *
   * @param  bytes A byte array
   * @param  off   Offset from which to start taking bytes
   * @param  len   Number of bytes to write
   */
  void write(byte[] bytes, int off, int len);

  /**
   * Write single byte to output.
   *
   * @param x the byte
   */
  void write(byte x);

  /**
   * Write single short to output.
   *
   * @param x the short.
   */
  void write(short x);

  /**
   * Write single int to output.
   *
   * @param x the int
   */
  void write(int x);

  /**
   * Write single long to output.
   *
   * @param x the long
   */
  void write(long x);

  /**
   * Write single character to output.
   *
   * @param x the char
   */
  void write(char x);

  /**
   * Write single bool to output.
   *
   * @param x the boolean
   */
  void write(boolean x);

  /**
   * Write single float to output.
   *
   * @param x the float
   */
  void write(float x);

  /**
   * Write single double to output.
   *
   * @param x the double
   */
  void write(double x);

  /**
   * Write string to output.
   *
   * @param x the string
   */
  void write(@NotNull String x);

  /**
   * Write specific terminal buffer to output.
   *
   * @param terminalBuffer the terminal buffer
   */
  void write(@NotNull TerminalBuffer terminalBuffer);

  /**
   * Write specific terminal string to output. Before, the termstring {@code termString} will be
   * converted to escaped sequence string.
   *
   * @param termString the term string
   */
  void write(@NotNull TermString termString);

  /**
   * Write specific text element. Before, the text element {@code textElement} will be
   * converted to escaped sequence string.
   *
   * @param textElement the text element
   */
  void write(@NotNull TextElement textElement);

  /**
   * Writes an object to the output with {@code \n} at the end. The object is converted into <br>
   * a string by {@link String#valueOf(Object)}.
   *
   * @param obj the object
   */
  void writeLine(@NotNull Object obj);

  /**
   * Writes {@code len} bytes from byte array that begins at offset {@code off}.
   * In addition, an {@code \n} is written at the end.
   * @param  bytes A byte array
   * @param  off   Offset from which to start taking bytes
   * @param  len   Number of bytes to write
   */
  void writeLine(byte[] bytes, int off, int len);

  /**
   * Write byte to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the byte
   */
  void writeLine(byte x);

  /**
   * Write short to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the short
   */
  void writeLine(short x);

  /**
   * Write int to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the int
   */
  void writeLine(int x);

  /**
   * Write long to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the long
   */
  void writeLine(long x);

  /**
   * Write character to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the char
   */
  void writeLine(char x);

  /**
   * Write bool to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the boolean
   */
  void writeLine(boolean x);

  /**
   * Write float to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the float
   */
  void writeLine(float x);

  /**
   * Write double to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the double
   */
  void writeLine(double x);

  /**
   * Write string to output.
   * In addition, an {@code \n} is written at the end.
   * @param x the string
   */
  void writeLine(@NotNull String x);

  /**
   * Write specific terminal buffer to output.
   * In addition, an {@code \n} is written at the end.
   * @param terminalBuffer the terminal buffer
   */
  void writeLine(@NotNull TerminalBuffer terminalBuffer);

  /**
   * Write specific terminal string to output. Before, the termstring {@code termString} will be
   * converted to escaped sequence string. In addition, an {@code \n} is written at the end.
   *
   * @param termString the term string
   */
  void writeLine(@NotNull TermString termString);

  /**
   * Write specific text element. Before, the text element {@code textElement} will be
   * converted to escaped sequence string. In addition, an {@code \n} is written at the end.
   *
   * @param textElement the text element
   */
  void writeLine(@NotNull TextElement textElement);

  /**
   * Write {@code \n} to output.
   */
  void newLine();

  /**
   * Foreground color.
   *
   * @param foregroundColor the foreground color
   */
  void foregroundColor(@Nullable ForegroundColor foregroundColor);

  /**
   * Background color.
   *
   * @param backgroundColor the background color
   */
  void backgroundColor(@Nullable BackgroundColor backgroundColor);

  /**
   * Style.
   *
   * @param textStyle the text style
   */
  void style(@Nullable TextStyle textStyle);

  /**
   * Reset style.
   */
  void resetStyle();

  /**
   * Cursor position.
   *
   * @param pos the pos
   */
  void cursorPosition(@NotNull TermPos pos);

  /**
   * Cursor position terminal position.
   *
   * @return the terminal position
   */
  @NotNull
  TermPos cursorPosition();

  /**
   * Clear.
   */
  void clear();

  /**
   * Flags.
   *
   * @param flags the flags
   */
  void flags(int flags);

  /**
   * Flags int.
   *
   * @return the int
   */
  int flags();

  /**
   * Cursor flags.
   *
   * @param flags the flags
   */
  void cursorFlags(int flags);

  /**
   * Cursor flags int.
   *
   * @return the int
   */
  int cursorFlags();

  /**
   * Reset.
   *
   * @param clearScreen the clear screen
   */
  void reset(boolean clearScreen);

  /**
   * Title.
   *
   * @param title the title
   */
  void title(@NotNull String title);

  /**
   * Title string.
   *
   * @return the string
   */
  @NotNull
  String title();

  /**
   * Window size.
   *
   * @param size the size
   */
  void windowSize(@NotNull TermDim size);

  /**
   * Window size terminal dimension.
   *
   * @return the terminal dimension
   */
  @NotNull
  TermDim windowSize();

  @NotNull TermDim defaultWindowSize();

  /**
   * Update.
   */
  void update();

  /**
   * Beep.
   */
  void beep();

  /**
   * Is native boolean.
   *
   * @return the boolean
   */
  boolean isNative();

  /**
   * Shutdown.
   *
   * @param status the status
   */
  void shutdown(int status);

  void inputEventListenerEnabled(boolean state);

  boolean inputEventListenerEnabled();

  @NotNull EventBus eventBus();

  void waitFutureShutdown();

  default boolean isEnabled() {
    return InternalFields.instance == this;
  }

  /**
   * Create @ not null t.
   *
   * @param <T>           the type parameter
   * @param instanceClass the instance class
   * @return the @ not null t
   * @throws TerminalInitializeException the terminal initialize exception
   */
  @SuppressWarnings("unchecked")
  static <T extends Terminal> @NotNull
      T create(Class<? extends AbstractTerminal<T>> instanceClass) throws TerminalInitializeException {
    try {
      Constructor<? extends AbstractTerminal<T>> constructor = instanceClass.getConstructor();
      AbstractTerminal<?> terminal = constructor.newInstance();
      return (T) terminal;
    } catch (NoSuchMethodException e) {
      throw new TerminalInitializeException("Failed to invoke public empty constructor", e);
    } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
      throw new TerminalInitializeException("Failed to create new instance of "
          + instanceClass.getSimpleName(), e);
    }
  }

  /**
   * Auto terminal.
   *
   * @return the terminal
   * @throws TerminalInitializeException the terminal initialize exception
   * @throws TerminalProviderException   the terminal provider exception
   */
  @SuppressWarnings("unchecked")
  static @NotNull Terminal auto() throws TerminalInitializeException, TerminalProviderException {
    synchronized (InternalFields.instanceLock) {
      if(InternalFields.instance != null) {
        return InternalFields.instance;
      }
    }
    String providerClassName = PropertyManager.getProviderClassName();
    if(providerClassName != null) {
      try {
        Class<?> clazz = Class.forName(providerClassName);
        try {
          if(AbstractTerminal.class.isAssignableFrom(clazz)) {
            Class<? extends AbstractTerminal<Terminal>> terminalType
                = (Class<? extends AbstractTerminal<Terminal>>) clazz;
            Terminal terminal = create(terminalType);
            set(terminal);
            return terminal;
          }
          LOGGER.error("Provider is not assignable to \"AbstractTerminal\": " + providerClassName);
        } catch (TerminalInitializeException ex) {
          LOGGER.error("Failed to initialize provider: " + providerClassName, ex);
        } catch (TerminalProviderException ex) {
          LOGGER.error("Failed to enable provider: " + providerClassName, ex);
        }
      } catch (ClassNotFoundException e) {
        LOGGER.error("Property provider class not found: " + providerClassName);
      }
      if(PropertyManager.isForcingProvider()) {
        throw new TerminalInitializeException("Could not load terminal provider: see logs");
      }
      LOGGER.warn("Terminal will be initialized by auto provider now");
    }
    if(PropertyManager.isNativeEnabled()) {
      try {
        NativeTerminal nativeTerminal = create(NativeTerminalProvider.class);
        set(nativeTerminal);
        return nativeTerminal;
      } catch (TerminalInitializeException ex) {
        LOGGER.error("Failed to initialize native provider", ex);
      } catch (TerminalProviderException ex) {
        LOGGER.error("Failed to enable native provider ", ex);
      }
      if(PropertyManager.isForcingProvider()) {
        throw new TerminalInitializeException("Could not load terminal provider: see logs");
      }
      LOGGER.warn("Terminal will be initialized by non native provider now");
    }
    Terminal terminal = create(NonNativeTerminalProvider.class);
    set(terminal);
    return terminal;
  }

  /**
   * Set.
   *
   * @param <T>      the type parameter
   * @param terminal the terminal
   * @throws TerminalProviderException the terminal provider exception
   */
  static <T extends Terminal> void set(@NotNull T terminal)
      throws TerminalProviderException {
    synchronized (InternalFields.instanceLock) {
      if (InternalFields.instance != null) {
        Terminal instance = InternalFields.instance;
        AbstractTerminal<?> abstractTerminal = (AbstractTerminal<?>) instance;
        abstractTerminal.disable();
        abstractTerminal.eventBus().post(new TerminalDisableEvent(terminal));
      }
      AbstractTerminal<?> abstractTerminal = (AbstractTerminal<?>) terminal;
      InternalFields.instance = abstractTerminal;
      abstractTerminal.enable();
      abstractTerminal.eventBus().post(new TerminalEnableEvent(terminal));

    }
  }

  /**
   * Get terminal.
   *
   * @return the terminal
   */
  static @NotNull Terminal get() {
    synchronized (InternalFields.instanceLock) {
      if(InternalFields.instance == null) {
        throw new TerminalRuntimeException("No instance provided");
      }
      return InternalFields.instance;
    }
  }

  static <T extends Terminal> @NotNull T getAs(@NotNull Class<T> type) {
    Terminal terminal = get();
    return type.cast(terminal);
  }

  /**
   * The enum Execution mode.
   */
  enum ExecutionMode {
    /**
     * Performance execution mode.
     */
    PERFORMANCE,
    /**
     * Efficiency execution mode.
     */
    EFFICIENCY
  }

  /**
   * The type Property manager.
   */
  class PropertyManager {

    /**
     * The constant NATIVE_ENABLED.
     */
    public static final String NATIVE_ENABLED = "termnative";
    /**
     * The constant START_PROVIDER.
     */
    public static final String START_PROVIDER = "termprovider";
    /**
     * The constant FORCE_PROVIDER.
     */
    public static final String FORCE_PROVIDER = "forceprovider";
    /**
     * The constant IN_BUFFER_SIZE.
     */
    public static final String IN_BUFFER_SIZE = "inbuffer_size";
    /**
     * The constant EXECUTION_MODE.
     */
    public static final String EXECUTION_MODE = "execmode";
    /**
     * The constant NO_LIB_CACHE.
     */
    public static final String NO_LIB_CACHE   = "nolibcache";

    /**
     * Is native enabled boolean.
     *
     * @return the boolean
     */
    public static boolean isNativeEnabled() {
      String value = System.getProperty(NATIVE_ENABLED, "1");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

    /**
     * Sets native enabled.
     *
     * @param state the state
     */
    public static void setNativeEnabled(boolean state) {
      System.setProperty(NATIVE_ENABLED, String.valueOf(state));
    }

    /**
     * Gets provider class name.
     *
     * @return the provider class name
     */
    public static @Nullable String getProviderClassName() {
      Properties properties = System.getProperties();
      return properties.getProperty(START_PROVIDER);
    }

    /**
     * Sets provider class name.
     *
     * @param name the name
     */
    public static void setProviderClassName(@Nullable String name) {
      Properties properties = System.getProperties();
      if(name == null) {
        properties.remove(START_PROVIDER);
        return;
      }
      properties.setProperty(START_PROVIDER, name);
    }

    /**
     * Is forcing provider boolean.
     *
     * @return the boolean
     */
    public static boolean isForcingProvider() {
      String value = System.getProperty(FORCE_PROVIDER, "0");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

    /**
     * Sets force provider.
     *
     * @param state the state
     */
    public static void setForceProvider(boolean state) {
      System.setProperty(FORCE_PROVIDER, String.valueOf(state));
    }

    /**
     * Gets input buffer size.
     *
     * @return the input buffer size
     */
    public static int getInputBufferSize() {
      String property = System.getProperty(IN_BUFFER_SIZE, "2048");
      try {
        return Integer.parseInt(property);
      } catch (NumberFormatException ex) {
        LOGGER.warn("Property \"" + IN_BUFFER_SIZE + "\" is an invalid number");
        return 2048;
      }
    }

    /**
     * Sets input buffer size.
     *
     * @param size the size
     */
    public static void setInputBufferSize(int size) {
      System.setProperty(IN_BUFFER_SIZE, String.valueOf(size));
    }

    /**
     * Gets exec mode.
     *
     * @return the exec mode
     */
    public static @NotNull ExecutionMode getExecMode() {
      String property = System.getProperty(EXECUTION_MODE, ExecutionMode.EFFICIENCY.name());
      switch (property.toLowerCase()) {
        case "effi", "efficiency", "slow" -> {
          return ExecutionMode.EFFICIENCY;
        }
        case "perf", "performance", "fast" -> {
          return ExecutionMode.PERFORMANCE;
        }
        default -> {
          LOGGER.warn("Property \"" + EXECUTION_MODE + "\" is invalid");
          return ExecutionMode.EFFICIENCY;
        }
      }
    }

    /**
     * Sets exec mode.
     *
     * @param execMode the exec mode
     */
    public static void setExecMode(@NotNull ExecutionMode execMode) {
      System.setProperty(EXECUTION_MODE, execMode.name().toLowerCase());
    }

    /**
     * Is no library cache boolean.
     *
     * @return the boolean
     */
    public static boolean isNoLibraryCache() {
      String value = System.getProperty(NO_LIB_CACHE, "0");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

  }

}
