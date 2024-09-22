package net.jterminal.natv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import net.jterminal.system.SystemInfo;
import net.jterminal.util.StreamUtil;
import org.jetbrains.annotations.NotNull;

public final class NativeLibrary {

  private final String name;
  private final int version;

  NativeLibrary(String name, int version) {
    this.name = name;
    this.version = version;
  }

  public @NotNull String name() {
    return name;
  }

  public int version() {
    return version;
  }

  public boolean isSupportedForCurrent() {
    String path = resourceFilePath();
    URL resource = StreamUtil.class.getClassLoader().getResource(path);
    return resource != null;
  }

  public @NotNull String resourceFilePath() {
    return "native/"
        + SystemInfo.current().os().name().toLowerCase()
        + "/"
        + SystemInfo.current().arch().mnemonic().toLowerCase()
        + "/"
        + name.toLowerCase()
        + "."
        + SystemInfo.current().dynamicLibFileEnd();
  }

  public @NotNull String temporaryFileName() {
    String sysId = SystemInfo.current().sysId();
    return name.toLowerCase()
        + sysId
        + "_"
        + version
        + "."
        + SystemInfo.current().dynamicLibFileEnd();
  }

  public @NotNull File temporaryFile() {
    return new File(NativeLoader.temporaryRootDir(), temporaryFileName());
  }

  public boolean isResourceExists() {
    return StreamUtil.getRessourceInputStream(
        resourceFilePath()) != null;
  }

  public void copy() throws IOException {
    InputStream ressourceInputStream = StreamUtil.getRessourceInputStream(
        resourceFilePath());
    if (ressourceInputStream == null) {
      throw new FileNotFoundException("Jar resource required: " + resourceFilePath());
    }
    FileOutputStream outputStream = new FileOutputStream(temporaryFile());
    StreamUtil.transfer(ressourceInputStream, outputStream, 1024, true);
    outputStream.close();
  }

}
