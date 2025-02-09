package net.jterminal.test.example.scenario;

import net.jterminal.text.termstring.TermString;
import net.jterminal.ui.component.FrameContainer;
import net.jterminal.ui.component.LabelComponent;
import net.jterminal.ui.component.selectable.ButtonComponent;
import net.jterminal.ui.dialog.ResultDialog;
import net.jterminal.ui.dialog.ResultDialog.OptionType;
import net.jterminal.ui.dialog.ResultDialogBuilder;
import net.jterminal.ui.layout.Anchor;
import net.jterminal.ui.layout.Layout;
import org.jetbrains.annotations.NotNull;

public class ResultDialogScenarioContainer extends FrameContainer {

  public void create() {
    LabelComponent label1 = new LabelComponent(getLabel1Content());
    label1.x(3);
    label1.y(3);

    ButtonComponent button1 = new ButtonComponent("Open");
    button1.x(Layout.relative(label1, Anchor.LEFT));
    button1.y(Layout.dock(label1, Anchor.BOTTOM));
    button1.action(() -> {
      ResultDialog dialog = new ResultDialogBuilder()
          .title("Example dialog")
          .message(TermString.value("Some styled text..."))
          .optionType(OptionType.OK_TYPE)
          .build();
      dialog.openDialog();
    });

    LabelComponent label2 = new LabelComponent(getLabel2Content());
    label2.x(Layout.relative(label1, Anchor.LEFT));
    label2.y(Layout.dock(button1, Anchor.BOTTOM), Layout.offset(3));

    ButtonComponent button2 = new ButtonComponent("Open");
    button2.x(Layout.relative(label1, Anchor.LEFT));
    button2.y(Layout.dock(label2, Anchor.BOTTOM));
    button2.action(() -> {
      ResultDialog dialog = new ResultDialogBuilder()
          .title("Example dialog")
          .message(TermString.value("Some styled text..."))
          .optionType(OptionType.YES_NO_TYPE)
          .build();
      dialog.openDialog();
    });

    LabelComponent label3 = new LabelComponent(getLabel3Content());
    label3.x(Layout.relative(label1, Anchor.LEFT));
    label3.y(Layout.dock(button2, Anchor.BOTTOM), Layout.offset(3));

    ButtonComponent button3 = new ButtonComponent("Open");
    button3.x(Layout.relative(label1, Anchor.LEFT));
    button3.y(Layout.dock(label3, Anchor.BOTTOM));
    button3.action(() -> {
      ResultDialog dialog = new ResultDialogBuilder()
          .title("Example dialog")
          .message(TermString.value("Some styled text..."))
          .optionType(OptionType.YES_NO_CANCEL_TYPE)
          .build();
      dialog.openDialog();
    });

    add(label1);
    add(button1);
    add(label2);
    add(button2);
    add(label3);
    add(button3);
  }

  private static @NotNull TermString getLabel1Content() {
    return TermString.value(
        "Create a result dialog with ok button"
    );
  }

  private static @NotNull TermString getLabel2Content() {
    return TermString.value(
        "Create a result dialog with yes and no option"
    );
  }

  private static @NotNull TermString getLabel3Content() {
    return TermString.value(
        "Create a result dialog with yes, no and cancel option"
    );
  }

}
