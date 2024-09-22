package net.jterminal.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreamUtil {

  public static @Nullable InputStream getRessourceInputStream(String path) {
    return StreamUtil.class.getClassLoader().getResourceAsStream(path);
  }

  public static void transfer(@NotNull InputStream inputStream,
      @NotNull OutputStream outputStream, int bufSize, boolean autoFlush) throws IOException {
    byte[] buf = new byte[bufSize];
    int len;
    while ((len = inputStream.read(buf)) != -1) {
      outputStream.write(buf, 0, len);
      outputStream.flush();
    }
  }

}
