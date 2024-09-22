package net.jterminal;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import net.jterminal.exception.TerminalInitializeException;
import net.jterminal.exception.TerminalProviderException;
import net.jterminal.exception.TerminalRuntimeException;
import net.jterminal.instance.AbstractTerminal;
import net.jterminal.instance.NativeTerminalProvider;
import net.jterminal.instance.NonNativeTerminalProvider;
import net.jterminal.io.TerminalInputStream;
import net.jterminal.util.TerminalDimension;
import net.jterminal.util.TerminalPosition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Terminal {

  Logger LOGGER = LogManager.getLogger();

  InputStream FD_IN = new FileInputStream(FileDescriptor.in);
  OutputStream FD_OUT = new FileOutputStream(FileDescriptor.out);
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

  void write(@NotNull Object obj);

  void write(byte[] bytes, int off, int len);

  void write(byte x);

  void write(short x);

  void write(int x);

  void write(long x);

  void write(char x);

  void write(boolean x);

  void write(float x);

  void write(double x);

  void write(@NotNull String x);

  void write(@NotNull TerminalBuffer terminalBuffer);

  void writeLine(@NotNull Object obj);

  void writeLine(byte[] bytes, int off, int len);

  void writeLine(byte x);

  void writeLine(short x);

  void writeLine(int x);

  void writeLine(long x);

  void writeLine(char x);

  void writeLine(boolean x);

  void writeLine(float x);

  void writeLine(double x);

  void writeLine(@NotNull String x);

  void writeLine(@NotNull TerminalBuffer terminalBuffer);

  TerminalInputStream newInputStream(int capacity);

  void cursorPosition(@NotNull TerminalPosition pos);

  @NotNull
  TerminalPosition cursorPosition();

  void clear();

  void flags(int flags);

  int flags();

  void cursorFlags(int flags);

  int cursorFlags();

  void reset(boolean clearScreen);

  void title(@NotNull String title);

  @NotNull
  String title();

  void windowSize(@NotNull TerminalDimension size);

  @NotNull
  TerminalDimension windowSize();

  void update();

  void beep();

  boolean isNative();

  void shutdown(int status);

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

  static <T extends Terminal> void set(@NotNull T terminal)
      throws TerminalProviderException {
    synchronized (InternalFields.instanceLock) {
      if (InternalFields.instance != null) {
        Terminal instance = InternalFields.instance;
        AbstractTerminal<?> abstractTerminal = (AbstractTerminal<?>) instance;
        abstractTerminal.disable();
        InternalFields.instance = null;
      }
      AbstractTerminal<?> abstractTerminal = (AbstractTerminal<?>) terminal;
      abstractTerminal.enable();
    }
  }

  static @NotNull Terminal get() {
    synchronized (InternalFields.instanceLock) {
      if(InternalFields.instance == null) {
        throw new TerminalRuntimeException("No instance provided");
      }
      return InternalFields.instance;
    }
  }

  enum ExecutionMode {
    PERFORMANCE,
    EFFICIENCY
  }

  class PropertyManager {

    public static final String NATIVE_ENABLED = "termnative";
    public static final String START_PROVIDER = "termprovider";
    public static final String FORCE_PROVIDER = "forceprovider";
    public static final String IN_BUFFER_SIZE = "inbuffer_size";
    public static final String EXECUTION_MODE = "execmode";
    public static final String NO_LIB_CACHE   = "nolibcache";

    public static boolean isNativeEnabled() {
      String value = System.getProperty(NATIVE_ENABLED, "1");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

    public static void setNativeEnabled(boolean state) {
      System.setProperty(NATIVE_ENABLED, String.valueOf(state));
    }

    public static @Nullable String getProviderClassName() {
      Properties properties = System.getProperties();
      return properties.getProperty(START_PROVIDER);
    }

    public static void setProviderClassName(@Nullable String name) {
      Properties properties = System.getProperties();
      if(name == null) {
        properties.remove(START_PROVIDER);
        return;
      }
      properties.setProperty(START_PROVIDER, name);
    }

    public static boolean isForcingProvider() {
      String value = System.getProperty(FORCE_PROVIDER, "0");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

    public static void setForceProvider(boolean state) {
      System.setProperty(FORCE_PROVIDER, String.valueOf(state));
    }

    public static int getInputBufferSize() {
      String property = System.getProperty(IN_BUFFER_SIZE, "2048");
      try {
        return Integer.parseInt(property);
      } catch (NumberFormatException ex) {
        LOGGER.warn("Property \"" + IN_BUFFER_SIZE + "\" is an invalid number");
        return 2048;
      }
    }

    public static void setInputBufferSize(int size) {
      System.setProperty(IN_BUFFER_SIZE, String.valueOf(size));
    }

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

    public static void setExecMode(@NotNull ExecutionMode execMode) {
      System.setProperty(EXECUTION_MODE, execMode.name().toLowerCase());
    }

    public static boolean isNoLibraryCache() {
      String value = System.getProperty(NO_LIB_CACHE, "0");
      return value.equals("1") || value.equalsIgnoreCase("true");
    }

  }

}
