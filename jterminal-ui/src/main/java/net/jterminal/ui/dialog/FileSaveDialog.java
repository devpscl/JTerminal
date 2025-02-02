package net.jterminal.ui.dialog;

import java.io.File;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class FileSaveDialog extends FileDialog {

  public FileSaveDialog(@NotNull String title, @NotNull Consumer<File> resultEvent) {
    this(new File(""), title, resultEvent);
  }

  public FileSaveDialog(@NotNull File dir,
      @NotNull String title,
      @NotNull Consumer<File> resultEvent) {
    super(dir, title, ChooseType.FILE_CHOOSER, Action.SAVE, resultEvent);
  }

}
