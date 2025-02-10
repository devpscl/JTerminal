package net.jterminal.cli.commodore;

import java.util.HashMap;
import java.util.Map;
import net.jterminal.cli.command.CommandArgument;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandState<T extends CommandArgument> {

  private final AbstractNode<T, ?> node;
  private final Map<String, CommandArgument> argumentMap;
  private final CommandArgument[] argumentValues;

  public CommandState(AbstractNode<T, ?> node,
      Map<String, CommandArgument> argumentMap,
      CommandArgument[] argumentValues) {
    this.node = node;
    this.argumentMap = argumentMap;
    this.argumentValues = argumentValues;
  }

  public CommandState(CommandArgument[] argumentValues) {
    this.node = null;
    this.argumentMap = new HashMap<>();
    this.argumentValues = argumentValues;
  }

  public @Nullable AbstractNode<T, ?> node() {
    return node;
  }

  public CommandArgument[] arguments() {
    return argumentValues;
  }

  public boolean hasArgumentId(@NotNull String id) {
    return argumentMap.containsKey(id);
  }

  public @Nullable CommandArgument argumentById(@NotNull String name) {
    return argumentMap.get(name);
  }

  public @Nullable CommandArgument argumentByNode(@NotNull AbstractNode<T, ?> node) {
    if(node instanceof ValueNode<?> valueNode) {
      return argumentById(valueNode.id());
    }
    return null;
  }
  
  public @NotNull CommandState<T> newState(@Nullable AbstractNode<T, ?> node) {
    return new CommandState<>(node, argumentMap, argumentValues);
  }

}
