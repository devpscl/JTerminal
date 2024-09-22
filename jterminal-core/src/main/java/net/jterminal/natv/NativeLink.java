package net.jterminal.natv;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.jetbrains.annotations.NotNull;

public class NativeLink {

  private final NativeLibrary nativeLibrary;
  private final Class<?> nativeClass;

  NativeLink(NativeLibrary nativeLibrary,
      Class<?> nativeClass) {
    this.nativeLibrary = nativeLibrary;
    this.nativeClass = nativeClass;
  }

  public @NotNull NativeLibrary library() {
    return nativeLibrary;
  }

  public @NotNull Class<?> nativeClass() {
    return nativeClass;
  }

  void callNativeLoader(@NotNull String path) throws NativeException {
    try {
      Method loadLibrary = nativeClass.getDeclaredMethod("loadLibrary", String.class);
      loadLibrary.setAccessible(true);
      loadLibrary.invoke(null, path);
      loadLibrary.setAccessible(false);
    } catch (NoSuchMethodException e) {
      throw new NativeException("Method \"private static void loadLibrary(String)\""
          + " in " + nativeClass.getSimpleName() + " not found");
    } catch (InvocationTargetException | IllegalAccessException e) {
      throw new NativeException("Failed to load library for type "
          + nativeClass.getSimpleName(), e);
    }
  }

}
