package net.jterminal.ui.dialog;

import java.io.File;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class FileChooseDirectoryDialog extends FileDialog {

  public FileChooseDirectoryDialog(@NotNull String title, @NotNull Consumer<File> resultEvent) {
    this(new File(""), title, resultEvent);
  }

  public FileChooseDirectoryDialog(@NotNull File dir,
      @NotNull String title,
      @NotNull Consumer<File> resultEvent) {
    super(dir, title, ChooseType.DIRECTORY_CHOOSER, Action.CHOOSE, resultEvent);
  }

}
