# Dialog

A dialog can be opened in a screen that has the focus while this dialog is still open.

## ResultDialog with builder
<tldr>
Example code:
<code-block lang="java">
ResultDialog dialog = new ResultDialogBuilder()
    .title("Warning")
    .message("Close without saving?")
    .optionType(OptionType.OK_CANCEL_TYPE)
    .event(ResultDialog.BUTTON_OK, rd -> {
      Terminal.get().shutdown(1);
    })
    .build();
screen.openDialog(dialog);
</code-block>
Screen output:
<img src="resultdialog.png" alt="output"/>
</tldr>

## Filedialog

<tldr>
Example code:
<code-block lang="java">
FileOpenDialog fileOpenDialog = new FileOpenDialog("Open file", file -> {
  //select file event
});
fileOpenDialog.fileFilter("java", "txt");
screen.openDialog(fileOpenDialog);
</code-block>
Screen output:
<img src="filedialog.png" alt="output"/>
</tldr>