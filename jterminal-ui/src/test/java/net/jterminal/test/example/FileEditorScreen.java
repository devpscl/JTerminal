package net.jterminal.test.example;

import java.io.IOException;
import java.nio.file.Files;
import net.jterminal.Terminal;
import net.jterminal.text.TerminalColor;
import net.jterminal.ui.TermScreen;
import net.jterminal.ui.component.menu.MenuBarComponent;
import net.jterminal.ui.component.menu.MenuItem;
import net.jterminal.ui.component.menu.MenuTab;
import net.jterminal.ui.component.selectable.TextAreaComponent;
import net.jterminal.ui.dialog.FileOpenDialog;
import net.jterminal.ui.dialog.FileSaveDialog;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.Axis;

public class FileEditorScreen extends TermScreen {

  private MenuBarComponent menuBarComponent;
  private TextAreaComponent textAreaComponent;

  private final ExampleApp instance;

  public FileEditorScreen(ExampleApp instance) {
    this.instance = instance;
  }

  public void create() {
    setMouseInputEnabled(true);
    foregroundColor(TerminalColor.WHITE);
    backgroundColor(TerminalColor.BLUE);

    menuBarComponent = new MenuBarComponent();
    MenuTab tab = new MenuTab("File");
    MenuItem newItem = new MenuItem("New");
    MenuItem openItem = new MenuItem("Open");
    MenuItem saveItem = new MenuItem("Save As");
    MenuItem exitItem = new MenuItem("Exit to home");
    newItem.action(this::performNewFile);
    openItem.action(this::performOpenFile);
    saveItem.action(this::performSaveFile);
    exitItem.action(this::performExit);
    tab.add(newItem, openItem, saveItem, exitItem);
    menuBarComponent.add(tab);
    textAreaComponent = new TextAreaComponent();
    textAreaComponent.backgroundColor(TerminalColor.WHITE);
    textAreaComponent.y(2);
    textAreaComponent.width(Layout.fill());
    textAreaComponent.height(Layout.fill());
    textAreaComponent.attachScrollBar(Axis.HORIZONTAL);
    textAreaComponent.attachScrollBar(Axis.VERTICAL);

    add(textAreaComponent);
    add(menuBarComponent);
  }

  private void performNewFile() {
    textAreaComponent.text("");
  }

  private void performOpenFile() {
    FileOpenDialog openDialog = new FileOpenDialog("Open", file -> {
      if(file == null) {
        return;
      }
      try {
        textAreaComponent.text(Files.readString(file.toPath()));
      } catch (IOException e) {
        Terminal.LOGGER.error("Failed to read file", e);
      }
    });
    openDialog.openDialog();
  }

  private void performSaveFile() {
    FileSaveDialog saveDialog = new FileSaveDialog("Save", file -> {
      if(file == null) {
        return;
      }
      try {
        Files.writeString(file.toPath(), textAreaComponent.text().raw());
      } catch (IOException e) {
        Terminal.LOGGER.error("Failed to write file", e);
      }
    });
    saveDialog.openDialog();
  }

  private void performExit() {
    instance.openHomeScreen();
  }

}
