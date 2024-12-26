package net.jterminal.cli.commodore;

import java.util.function.Function;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.commodore.CommodoreFunctional.StyleEditor;
import net.jterminal.cli.exception.CommandExecuteException;
import net.jterminal.text.Combiner;
import net.jterminal.text.TerminalColor;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public interface CommodoreHelper<T extends CommandArgument> {

  default @NotNull LiteralNode<T> literal(@NotNull String name) {
    return new LiteralNode<>(name);
  }

  default @NotNull ValueNode<T> value(@NotNull String id) {
    return new ValueNode<>(id);
  }

  default @NotNull StyleEditor<T> modifiedStyle(Function<CommandState<T>, TextStyle> func) {
    return new StyleEditor<>() {
      @Override
      public @NotNull TermString edit(@NotNull TermString input,
          @NotNull CommandState<T> state) {
        TextStyle textStyle = input.styleAt(0);
        TextStyle newStyle = func.apply(state);
        TextStyle combinedStyle = Combiner.combine(newStyle, textStyle);

        return TermString.builder()
            .appendStyle(combinedStyle)
            .append(input.raw())
            .build();
      }
    };
  }

  default @NotNull StyleEditor<T> passwordStyle() {
    return new StyleEditor<>() {
      @Override
      public @NotNull TermString edit(@NotNull TermString input,
          @NotNull CommandState<T> state) {
        return TermString.value(StringUtil.repeat('*', input.length()));
      }
    };
  }

  default @NotNull StyleEditor<T> numberInvalidStyle() {
    return modifiedStyle(state -> {
      AbstractNode<T, ?> node = state.node();
      if(node == null)  {
        return TextStyle.create();
      }
      CommandArgument arg = state.argumentByNode(node);
      if(arg == null) {
        return TextStyle.create();
      }
      if(!arg.isNumber()) {
        return TextStyle.create(TerminalColor.RED, null);
      }
      return TextStyle.create();
    });
  }

}
