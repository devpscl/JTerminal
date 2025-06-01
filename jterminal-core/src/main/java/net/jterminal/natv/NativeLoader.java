package net.jterminal.natv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jterminal.Terminal;
import net.jterminal.annotation.NativeType;
import net.jterminal.system.SystemInfo;
import net.jterminal.system.UnsupportedSystemException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NativeLoader {

  private static LibraryDestination libraryDestination
      = new TempDirLibraryDestination();
  private static final Set<NativeLink> linkList = new HashSet<>();

  public @NotNull Collection<NativeLink> links() {
    return new ArrayList<>(linkList);
  }

  public static @NotNull LibraryDestination libraryDestination() {
    return libraryDestination;
  }

  public static void libraryDestination(@NotNull LibraryDestination destination) {
    libraryDestination = destination;
  }

  public @Nullable Collection<NativeLink> get(@NotNull String libraryName) {
    List<NativeLink> links = new ArrayList<>();
    for (NativeLink link : new ArrayList<>(linkList)) {
      if(link.library().name().equalsIgnoreCase(libraryName)) {
        links.add(link);
      }
    }
    return links;
  }

  public @Nullable NativeLink get(Class<?> nativeTypeClass) {
    if (!nativeTypeClass.isAnnotationPresent(NativeType.class)) {
      return null;
    }
    final NativeType nativeType = nativeTypeClass.getAnnotation(NativeType.class);
    for (NativeLink link : new ArrayList<>(linkList)) {
      if(link.library().name().equalsIgnoreCase(nativeType.name())) {
        return link;
      }
    }
    return null;
  }

  public static boolean isLoaded(Class<?> nativeTypeClass) {
    return linkList.stream().anyMatch(link -> link.nativeClass() == nativeTypeClass);
  }

  public static @NotNull NativeLink load(@NotNull String name, int version,
      @NotNull Class<?> nativeTypeClass) throws NativeException, UnsupportedSystemException {
    if(isLoaded(nativeTypeClass)) {
      throw new NativeException("Native class is already loaded: "
          + nativeTypeClass.getSimpleName());
    }
    NativeLibrary library = new NativeLibrary(name, version);
    if(!library.isSupportedForCurrent()) {
      throw new UnsupportedSystemException(SystemInfo.current().toString());
    }
    File file = library.destinationFile();
    if(Terminal.PropertyManager.isNoLibraryCache()) {
      if(!file.delete()) {
        Terminal.LOGGER.error("Failed to delete old library file: {}", file.getName());
      }
    }
    if(!file.exists()) {
      try {
        library.copy();
      } catch (IOException e) {
        throw new NativeException("Failed to copy library into temp directory", e);
      }
    }
    return link(library, nativeTypeClass, file);
  }

  private static @NotNull NativeLink link(@NotNull NativeLibrary library,
      @NotNull Class<?> nativeTypeClass, @NotNull File libraryFile) throws NativeException {
    NativeLink link = new NativeLink(library, nativeTypeClass);
    try {
      link.callNativeLoader(libraryFile.getAbsolutePath());
    } catch (UnsatisfiedLinkError e) {
      throw new NativeException("Failed to link", e);
    }
    linkList.add(link);
    return link;
  }

  public static @NotNull NativeLink load(
      Class<?> nativeTypeClass) throws NativeException, UnsupportedSystemException {
    if (!nativeTypeClass.isAnnotationPresent(NativeType.class)) {
      throw new NativeException(
          "Native type " + nativeTypeClass.getSimpleName() + " is not annotated with @NativeType");
    }
    NativeType nativeType = nativeTypeClass.getAnnotation(NativeType.class);
    return load(nativeType.name(), nativeType.version(), nativeTypeClass);
  }


}
