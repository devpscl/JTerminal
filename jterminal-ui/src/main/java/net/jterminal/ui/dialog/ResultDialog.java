package net.jterminal.ui.dialog;

import java.util.function.Consumer;
import net.jterminal.input.Keyboard;
import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.event.component.ComponentKeyEvent;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import net.jterminal.ui.util.BoxCharacter.Type;
import net.jterminal.ui.util.TextAlignment;
import org.jetbrains.annotations.NotNull;

public class ResultDialog extends TermDialog {

  public enum OptionType {
    YES_NO_TYPE,
    YES_NO_CANCEL_TYPE,
    OK_TYPE,
    OK_CANCEL_TYPE
  }

  public static final int BUTTON_OK = 1;
  public static final int BUTTON_YES = 2;
  public static final int BUTTON_NO = 3;
  public static final int BUTTON_CANCEL = 4;

  protected final int buttonSpace;
  protected final OptionType type;
  protected Consumer<Integer> action = null;
  private final FrameContainer frameContainer = new FrameContainer();
  private final LabelComponent labelComponent = new LabelComponent();

  public ResultDialog(@NotNull String title, OptionType type, int buttonSpace) {
    this.type = type;
    this.buttonSpace = buttonSpace;
    frameContainer.title(title);
    init();
  }

  public int spaceBetweenButtons() {
    return buttonSpace;
  }

  public @NotNull String title() {
    return frameContainer.title();
  }

  public void title(@NotNull String title) {
    frameContainer.title(title);
  }

  public @NotNull TermString message() {
    return labelComponent.text();
  }

  public void message(@NotNull String message) {
    message(TermString.value(message));
  }

  public void message(@NotNull TermString message) {
    labelComponent.text(message, true);
    labelComponent.width(Layout.fill());
  }

  public void action(@NotNull Consumer<Integer> action) {
    this.action = action;
  }

  public @NotNull OptionType optionType() {
    return type;
  }

  private void init() {
    frameContainer.boxLineType(Type.DOUBLE);
    frameContainer.titleAlignment(TextAlignment.CENTER);
    frameContainer.width(Layout.fill());
    frameContainer.height(Layout.fill());

    labelComponent.textAlignment(TextAlignment.CENTER);
    labelComponent.y(Layout.relative(Anchor.TOP), Layout.offset(1));
    labelComponent.x(1);
    labelComponent.width(Layout.fill());
    frameContainer.add(labelComponent);

    ButtonComponent okButton = new ButtonComponent("OK");
    okButton.action(this::handleOkButton);

    ButtonComponent yesButton = new ButtonComponent("YES");
    yesButton.action(this::handleYesButton);

    ButtonComponent noButton = new ButtonComponent("NO");
    noButton.action(this::handleNoButton);

    ButtonComponent cancelButton = new ButtonComponent("CANCEL");
    cancelButton.action(this::handleCancelButton);

    switch (type) {
      case OK_TYPE -> addButtons(okButton);
      case OK_CANCEL_TYPE -> addButtons(okButton, cancelButton);
      case YES_NO_TYPE -> addButtons(yesButton, noButton);
      case YES_NO_CANCEL_TYPE -> addButtons(yesButton, noButton, cancelButton);
    }
    add(frameContainer);
  }

  private void addButtons(ButtonComponent...buttonComponents) {
    int length = 0;
    for (ButtonComponent buttonComponent : buttonComponents) {
      length += buttonComponent.preferredWidth() + buttonSpace;
    }
    if(length < 1) {
      return;
    }
    length -= buttonSpace;
    int halfLength = length / 2;
    ButtonComponent lastComponent = null;
    for (ButtonComponent buttonComponent : buttonComponents) {
      buttonComponent.y(Layout.relative(Anchor.BOTTOM));
      if(lastComponent == null) {
        buttonComponent.x(Layout.center(),
            Layout.offset(-halfLength + 1));
        frameContainer.add(buttonComponent);
        lastComponent = buttonComponent;
        continue;
      }
      buttonComponent.x(Layout.dock(lastComponent, Anchor.RIGHT), Layout.offset(buttonSpace));
      lastComponent = buttonComponent;
      frameContainer.add(buttonComponent);
    }
  }

  private void handleOkButton() {
    if(action != null) {
      action.accept(BUTTON_OK);
    }
    closeDialog();
  }

  private void handleYesButton() {
    if(action != null) {
      action.accept(BUTTON_YES);
    }
    closeDialog();
  }

  private void handleNoButton() {
    if(action != null) {
      action.accept(BUTTON_NO);
    }
    closeDialog();
  }

  private void handleCancelButton() {
    if(action != null) {
      action.accept(BUTTON_CANCEL);
    }
    closeDialog();
  }

  @Override
  public void processKeyEvent(@NotNull ComponentKeyEvent event) {
    super.processKeyEvent(event);
    if(event.key() == Keyboard.KEY_ESCAPE) {
      if(type == OptionType.YES_NO_CANCEL_TYPE) {
        handleCancelButton();
        return;
      }
      if(type == OptionType.OK_CANCEL_TYPE) {
        handleCancelButton();
      }
    }
  }
}
