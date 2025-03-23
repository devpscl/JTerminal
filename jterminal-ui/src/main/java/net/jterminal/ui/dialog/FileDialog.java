package net.jterminal.ui.dialog;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import net.jterminal.input.Keyboard;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.component.selectable.ListViewComponent;
import net.jterminal.ui.component.selectable.TextFieldComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.event.special.ListItemInteractEvent;
import net.jterminal.ui.event.special.ListItemShowEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.BoxCharacter;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileDialog extends TermDialog {

  public enum ChooseType {
    FILE_CHOOSER,
    DIRECTORY_CHOOSER
  }

  public enum Action {
    OPEN,
    SAVE,
    CHOOSE
  }

  private File dir;
  private List<File> viewFileList = new ArrayList<>();

  private FrameContainer frameContainer;
  private ButtonComponent actionButton;
  private ButtonComponent cancelButton;
  private LabelComponent fullPathFieldLabel;
  private TextFieldComponent fullPathInputComponent;
  private FrameContainer listViewFrameContainer;
  private ListViewComponent listViewComponent;
  private LabelComponent nameFieldLabel;
  private TextFieldComponent nameInputComponent;

  private BoxCharacter.Type boxType = BoxCharacter.Type.NORMAL;
  private FileFilter fileFilter;
  private final ChooseType chooseType;
  private final Action action;
  private Consumer<File> resultEvent;

  public FileDialog(@NotNull File dir, @NotNull String title,
      @NotNull ChooseType chooseType, Action action, Consumer<File> resultEvent) {
    this.dir = new File(dir.getAbsolutePath());
    this.chooseType = chooseType;
    this.action = action;
    this.resultEvent = resultEvent;
    init(title);
    updateList();
    updateFullPath();
    width(Layout.fill(), Layout.scale(0.8F));
    height(Layout.fill(), Layout.scale(0.8F));
  }

  private void init(@NotNull String title) {
    frameContainer = new FrameContainer(title, TextAlignment.LEFT, 2);
    frameContainer.width(Layout.fill());
    frameContainer.height(Layout.fill());

    fullPathFieldLabel = new LabelComponent(TermString.value("Full Path:"));
    fullPathFieldLabel.x(2);
    fullPathFieldLabel.y(2);

    fullPathInputComponent = new TextFieldComponent();
    fullPathInputComponent.x(Layout.dock(fullPathFieldLabel, Anchor.RIGHT), Layout.offset(1));
    fullPathInputComponent.y(Layout.relative(fullPathFieldLabel, Anchor.TOP));
    fullPathInputComponent.width(Layout.fill(), Layout.offset(-2));
    fullPathInputComponent.backgroundColor(TerminalColor.WHITE);
    fullPathInputComponent.priority(-1);
    fullPathInputComponent.eventBus().subscribe(ComponentKeyEvent.class,
        this::handleFullPathInputEvent);

    listViewFrameContainer = new FrameContainer();
    listViewFrameContainer.x(Layout.relative(fullPathFieldLabel, Anchor.LEFT));
    listViewFrameContainer.y(Layout.relative(fullPathFieldLabel, Anchor.BOTTOM),
        Layout.offset(2));

    listViewFrameContainer.width(Layout.fill(), Layout.offset(-2));
    listViewFrameContainer.height(Layout.fill(), Layout.offset(-3));

    listViewComponent = new ListViewComponent();
    listViewComponent.width(Layout.fill());
    listViewComponent.height(Layout.fill());
    listViewComponent.attachScrollBar();
    listViewComponent.eventBus().subscribe(ComponentKeyEvent.class, this::handleListInputEvent);
    listViewComponent.eventBus().subscribe(ListItemInteractEvent.class, this::handleInteractEvent);
    listViewComponent.eventBus().subscribe(ListItemShowEvent.class, this::handleShowEvent);
    listViewFrameContainer.add(listViewComponent);

    nameFieldLabel = new LabelComponent(TermString.value("File Name: "));
    nameFieldLabel.x(Layout.relative(listViewFrameContainer, Anchor.LEFT));
    nameFieldLabel.y(Layout.relative(Anchor.BOTTOM), Layout.offset(-1));

    cancelButton = new ButtonComponent("Cancel");
    cancelButton.action(this::handleCancelEvent);
    cancelButton.y(Layout.relative(nameFieldLabel, Anchor.TOP));
    cancelButton.x(Layout.relative(Anchor.RIGHT), Layout.offset(-cancelButton.preferredWidth()),
        Layout.offset(-1));

    actionButton = new ButtonComponent(action.name());
    actionButton.action(this::handleActionEvent);
    actionButton.y(Layout.relative(nameFieldLabel, Anchor.TOP));
    actionButton.x(Layout.relative(cancelButton, Anchor.LEFT),
        Layout.offset(-actionButton.preferredWidth()),
        Layout.offset(-1));

    nameInputComponent = new TextFieldComponent();
    nameInputComponent.backgroundColor(TerminalColor.WHITE);
    nameInputComponent.x(Layout.dock(nameFieldLabel, Anchor.RIGHT));
    nameInputComponent.y(Layout.relative(nameFieldLabel, Anchor.TOP));
    nameInputComponent.width(Layout.dockTo(actionButton, Anchor.LEFT), Layout.offset(-1));

    frameContainer.add(fullPathFieldLabel);
    frameContainer.add(fullPathInputComponent);
    frameContainer.add(listViewFrameContainer);
    frameContainer.add(nameFieldLabel);
    frameContainer.add(nameInputComponent);
    frameContainer.add(actionButton);
    frameContainer.add(cancelButton);

    add(frameContainer);
  }

  public void fileFilter(@Nullable FileFilter fileFilter) {
    this.fileFilter = fileFilter;
    updateList();
  }

  public void fileFilter(@NotNull String...fileTypes) {
    fileFilter(file -> {
      String name = file.getName();
      int idx = name.lastIndexOf('.') + 1;
      if(idx >= name.length()) {
        return false;
      }
      String typeStr = name.substring(idx);
      boolean typeOk = Arrays.stream(fileTypes)
          .anyMatch(str -> str.equalsIgnoreCase(typeStr));
      return file.isDirectory() || typeOk;
    });
  }

  public @NotNull Type boxType() {
    return boxType;
  }

  public @NotNull Action action() {
    return action;
  }

  public void boxType(@NotNull Type boxType) {
    this.boxType = boxType;
    repaint();
  }

  public @NotNull ChooseType chooseType() {
    return chooseType;
  }

  public @NotNull File directory() {
    return dir;
  }

  public void directory(@NotNull File dir) {
    synchronized (lock) {
      this.dir = dir;
      updateList();
      updateFullPath();
    }
  }

  public void targetFile(@NotNull File file) {
    File parentFile = file.getParentFile();
    if(parentFile != null) {
      directory(parentFile);
    }
    nameInputComponent.value(file.getName());
  }

  public @NotNull File file() {
    return new File(dir, nameInputComponent.value());
  }

  private void updateList() {
    int cursor = listViewComponent.cursor();
    viewFileList.clear();
    List<String> itemList = createItemList(dir, viewFileList);
    listViewComponent.elements(itemList);
    listViewComponent.cursor(cursor);
  }

  private void updateFullPath() {
    fullPathInputComponent.value(dir.getAbsolutePath());
  }

  private @NotNull List<String> createItemList(@Nullable File directory, List<File> viewFiles) {
    List<String> l = new ArrayList<>();
    l.add("...");
    if(directory == null || !directory.exists()) {
      return l;
    }
    File[] files = directory.listFiles(fileFilter);
    if(files == null) {
      return l;
    }
    List<File> dirList = new ArrayList<>();
    List<File> fileList = new ArrayList<>();
    for (File file : files) {
      if(file.isDirectory()) {
        dirList.add(file);
        continue;
      }
      if(chooseType == ChooseType.DIRECTORY_CHOOSER) {
        continue;
      }
      fileList.add(file);
    }
    for (File file : dirList) {
      l.add("[DIR]  " + file.getName());
      viewFiles.add(file);
    }
    for (File file : fileList) {
      l.add("[FILE] " + file.getName());
      viewFiles.add(file);
    }
    return l;
  }

  private void handleFullPathInputEvent(@NotNull ComponentKeyEvent event) {
    TextFieldComponent component = (TextFieldComponent) event.component();
    if(!component.isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_ENTER) {
      synchronized (lock) {
        String value = fullPathInputComponent.value();
        dir = new File(value);
        updateFullPath();
        updateList();
      }
    }
  }

  private void handleListInputEvent(@NotNull ComponentKeyEvent event) {
    ListViewComponent component = (ListViewComponent) event.component();
    if(!component.isSelected()) {
      return;
    }
    if(event.key() == Keyboard.KEY_BACKSPACE) {
      performLeaveDirectory();
    }
  }

  private void performLeaveDirectory() {
    synchronized (lock) {
      File parentFile = dir.getParentFile();
      if(parentFile != null) {
        dir = parentFile;
      }
      updateFullPath();
      updateList();
    }
  }

  private void performInteractDirectory(@NotNull File file) {
    synchronized (lock) {
      dir = file;
      updateFullPath();
      updateList();
    }
  }

  private void performSelectFile(@NotNull File file) {
    if(chooseType == ChooseType.FILE_CHOOSER && file.isFile()) {
      nameInputComponent.value(file.getName());
      return;
    }
    if(chooseType == ChooseType.DIRECTORY_CHOOSER && file.isDirectory()) {
      nameInputComponent.value(file.getName());
    }
  }

  private void handleInteractEvent(@NotNull ListItemInteractEvent event) {
    int index = event.index();
    if(index == 0) {
      performLeaveDirectory();
      return;
    }
    index -= 1;
    if(index >= viewFileList.size()) {
      return;
    }
    File file = viewFileList.get(index);
    if(file == null) {
      return;
    }
    if(file.isDirectory()) {
      performInteractDirectory(file);
    }
  }

  private void handleShowEvent(@NotNull ListItemShowEvent event) {
    int index = event.index() - 1;
    if(index >= viewFileList.size() || index < 0) {
      return;
    }
    File file = viewFileList.get(index);
    if(file == null || !file.exists()) {
      return;
    }
    performSelectFile(file);
  }

  private void handleCancelEvent() {
    closeDialog();
    if(resultEvent != null) {
      resultEvent.accept(null);
    }
  }

  private void handleActionEvent() {
    closeDialog();
    if(resultEvent != null) {
      resultEvent.accept(file());
    }
  }

}
