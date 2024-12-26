package net.jterminal.cli.commodore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.commodore.CommodoreFunctional.Executor;
import net.jterminal.cli.exception.CommandBuildException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommodoreManager<T extends CommandArgument> {

  private final List<LiteralNode<T>> list = new ArrayList<>();
  private Executor<T> unknownCommandExecutor;

  public synchronized LiteralNode<T> addCommand(@NotNull String name)
      throws CommandBuildException {
    for (LiteralNode<T> node : list) {
      if(node.literal().equalsIgnoreCase(name)) {
        throw new CommandBuildException(name + " is already in use");
      }
    }
    LiteralNode<T> node = new LiteralNode<>(name);
    list.add(node);
    return node;
  }

  public synchronized void removeCommand(@NotNull final String name) {
    list.removeIf(arg -> arg.literal().equalsIgnoreCase(name));
  }

  public synchronized @NotNull Collection<LiteralNode<T>> commands() {
    return new ArrayList<>(list);
  }

  public void unknownCommandExecute(@NotNull Executor<T> executor) {
    this.unknownCommandExecutor = executor;
  }

  public @Nullable Executor<T> unknownCommandExecutor() {
    return unknownCommandExecutor;
  }

  private List<AbstractNode<T, ?>> findNodes(@NotNull AbstractNode<T, ?> root,
      @NotNull String value) {
    List<AbstractNode<T, ?>> arrayList = new ArrayList<>();
    for (AbstractNode<T, ?> node : root.nodes()) {
      if(node instanceof LiteralNode<?> literalNode) {
        if(literalNode.literal().equals(value)) {
          arrayList.add(node);
          break;
        }
      }
      if(node instanceof ValueNode<?>) {
        arrayList.add(node);
      }
    }
    return arrayList;
  }

  public @NotNull CommandState<T> createStateFrom(CommandArgument[] args,
      final @NotNull AbstractNode<T, ?> lastNode) {

    AbstractNode<T, ?>[] orderFromRoot = lastNode.getOrderFromRoot();
    Map<String, CommandArgument> argumentMap = new HashMap<>();
    for (int idx = 0; idx < orderFromRoot.length && idx < args.length; idx++) {
      AbstractNode<T, ?> node = orderFromRoot[idx];
      if(node instanceof ValueNode<?> valueNode) {
        argumentMap.put(valueNode.id(), args[idx]);
      }
    }
    return new CommandState<>(lastNode, argumentMap, args);
  }

  public synchronized ProduceCommandResult<T> produceCommand(CommandArgument[] args) {
    if(args.length == 0) {
      return new ProduceCommandResult<>(0, new ArrayList<>());
    }
    AbstractNode<T, ?> lastArgument = null;
    String value = args[0].raw();
    for (LiteralNode<T> arg : list) {
      if (arg.literal().equalsIgnoreCase(value)) {
        lastArgument = arg;
        break;
      }
    }
    if(lastArgument == null) {
      return new ProduceCommandResult<>(0, new ArrayList<>());
    }
    List<AbstractNode<T, ?>> prevArgsList = new ArrayList<>();
    List<AbstractNode<T, ?>> argsList = new ArrayList<>();
    argsList.add(lastArgument);

    int depth = 1;
    while (depth < args.length) {
      prevArgsList.clear();
      prevArgsList.addAll(argsList);
      argsList.clear();

      for (AbstractNode<T, ?> node : prevArgsList) {
        argsList.addAll(findNodes(node, args[depth].raw()));
      }
      if(argsList.isEmpty()) {
        argsList = prevArgsList;
        break;
      }
      depth++;
    }
    return new ProduceCommandResult<>(depth, argsList);
  }

  public record ProduceCommandResult<T extends CommandArgument>(int depth, List<
      AbstractNode<T, ?>> nodeList) {}

}
