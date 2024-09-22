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
import net.jterminal.system.UnsupportedSystemException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NativeLoader {

  private static final File dir;
  private static final Set<NativeLink> linkList = new HashSet<>();

  static {
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    dir = new File(tempDir, "javan");
  }

  public static @NotNull File temporaryRootDir() {
    return dir;
  }

  public @NotNull Collection<NativeLink> links() {
    return new ArrayList<>(linkList);
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
      throw new UnsupportedSystemException();
    }
    NativeLink link = new NativeLink(library, nativeTypeClass);
    File file = library.temporaryFile();
    if(!dir.exists()) {
      dir.mkdir();
    }
    if(!file.exists() || Terminal.PropertyManager.isNoLibraryCache()) {
      try {
        library.copy();
      } catch (IOException e) {
        throw new NativeException("Failed to copy library into temp directory", e);
      }
    }
    try {
      link.callNativeLoader(file.getAbsolutePath());
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
