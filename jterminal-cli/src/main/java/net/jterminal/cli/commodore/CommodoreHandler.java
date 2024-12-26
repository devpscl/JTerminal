package net.jterminal.cli.commodore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.jterminal.Terminal;
import net.jterminal.cli.CLITerminal;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.command.CommandHandler;
import net.jterminal.cli.command.CommandParser;
import net.jterminal.cli.commodore.CommodoreFunctional.Executor;
import net.jterminal.cli.commodore.CommodoreFunctional.StyleEditor;
import net.jterminal.cli.commodore.CommodoreFunctional.SuggestionResolver;
import net.jterminal.cli.commodore.CommodoreManager.ProduceCommandResult;
import net.jterminal.cli.exception.CommandBuildException;
import net.jterminal.cli.exception.CommandExecuteException;
import net.jterminal.text.style.TextStyle;
import net.jterminal.text.termstring.TermString;
import net.jterminal.text.termstring.TermStringBuilder;
import org.jetbrains.annotations.NotNull;

public abstract class CommodoreHandler<T extends CommandArgument>
    implements CommandHandler<T>, CommodoreHelper<T> {

  private static final @NotNull String loggingInvalidCommand = "Invalid command: {}";

  private final CommandParser<T> parser;
  private final CommodoreManager<T> commodoreManager = new CommodoreManager<>();

  public CommodoreHandler(CommandParser<T> parser) {
    this.parser = parser;
  }

  public void build() {
    try {
      build(commodoreManager);
    } catch (CommandBuildException e) {
      CLITerminal.LOGGER.error("Failed to build command structure", e);
    }
  }

  protected abstract void build(@NotNull CommodoreManager<T> manager) throws CommandBuildException;

  @Override
  public @NotNull CommandParser<T> parser() {
    return parser;
  }

  @Override
  public void command(T[] args, @NotNull String line) {
    ProduceCommandResult<T> result = commodoreManager.produceCommand(args);
    if(result.nodeList().isEmpty()) {
      Executor<T> unknownedCommandExecutor = commodoreManager.unknownCommandExecutor();
      if(unknownedCommandExecutor == null) {
        CLITerminal.LOGGER.error(loggingInvalidCommand, line);
        return;
      }
      try {
        unknownedCommandExecutor.exec(new CommandState<>(args));
      } catch (CommandExecuteException e) {
        CLITerminal.LOGGER.error("Failed to execute unknown event", e);
      }
      return;
    }
    AbstractNode<T, ?> node = result.nodeList().get(0);
    CommandState<T> state = commodoreManager.createStateFrom(args, node);
    final Executor<T> executor = node.executor();
    final String id = getCommandId(node);
    if(result.depth() != args.length || executor == null) {
      Executor<T> unknownedCommandExecutor = node.unknownCommandExecutor();
      if(unknownedCommandExecutor == null) {
        unknownedCommandExecutor = commodoreManager.unknownCommandExecutor();
      }
      if(unknownedCommandExecutor == null) {
        CLITerminal.LOGGER.error(loggingInvalidCommand, line);
        return;
      }
      try {
        unknownedCommandExecutor.exec(state);
      } catch (CommandExecuteException e) {
        CLITerminal.LOGGER.error("Failed to execute unknown event at " + id, e);
      }
      return;
    }
    try {
      executor.exec(state);
    } catch (CommandExecuteException e) {
      CLITerminal.LOGGER.error("Failed to execute command: " + id, e);
    }
  }

  private @NotNull String getCommandId(@NotNull AbstractNode<T, ?> node) {
    AbstractNode<T, ?> rootNode = node.rootNode();
    if(rootNode instanceof LiteralNode<?> literalNode) {
      return literalNode.literal();
    }
    return "unknown";
  }

  @Override
  public @NotNull TermString view(@NotNull TermString view, T[] args) {
    ProduceCommandResult<T> result = commodoreManager.produceCommand(args);
    List<AbstractNode<T, ?>> nodeList = result.nodeList();
    if(nodeList.isEmpty() || args.length == 0) {
      return view;
    }

    TermStringBuilder builder = TermString.builder(view);

    AbstractNode<T, ?> lastNode = result.nodeList().get(0);
    CommandState<T> state = commodoreManager.createStateFrom(args, lastNode);
    AbstractNode<T, ?>[] array = lastNode.getOrderFromRoot();
    for (int idx = 0; idx < array.length && idx < args.length; idx++) {
      AbstractNode<T, ?> node = array[idx];
      StyleEditor<T> styleEditor = node.styleEditor();
      if(styleEditor == null) {
        continue;
      }
      T commandArg = args[idx];
      TermString argString = view.substring(commandArg.positionStart(), commandArg.positionEnd());
      TextStyle textStyle = builder.getStyle(commandArg.positionStart());
      argString = styleEditor.edit(argString, state.newState(node));
      builder.replace(commandArg.positionStart(), commandArg.positionEnd(), argString);
      builder.insertStyle(commandArg.positionEnd(), textStyle, true);
    }
    return builder.build();
  }

  @Override
  public @NotNull List<String> getTabCompletions(T[] args, int cursor) {
    if(args.length == 0) {
      List<String> list = new ArrayList<>();
      for (LiteralNode<T> node : commodoreManager.commands()) {
        list.add(node.literal());
      }
      return list;
    }
    CommandArgument lastArgument = args[args.length - 1];
    ProduceCommandResult<T> result = commodoreManager.produceCommand(args);

    List<AbstractNode<T, ?>> nodeList = result.nodeList();
    if(nodeList.isEmpty()) {
      List<String> list = new ArrayList<>();
      for (LiteralNode<T> node : commodoreManager.commands()) {
        String literal = node.literal();
        if(literal.startsWith(args[0].raw())) {
          list.add(literal);
        }
      }
      return list;
    }
    AbstractNode<T, ?> lastNode = result.nodeList().get(0);
    CommandState<T> state = commodoreManager.createStateFrom(args, lastNode);

    Set<String> suggestionSet = new HashSet<>();

    if(result.depth() == args.length && cursor > lastArgument.positionEnd()
        || result.depth() + 1 == args.length) {

      List<AbstractNode<T, ?>> possibleNodes = new ArrayList<>();
      for (AbstractNode<T, ?> node : nodeList) {
        possibleNodes.addAll(node.nodes());
      }
      String input = cursor > lastArgument.positionEnd() ? "" : lastArgument.raw();
      fillSuggestions(suggestionSet, possibleNodes, input, state);
      return new ArrayList<>(suggestionSet);
    }

    if(result.depth() == args.length) {
      fillSuggestions(suggestionSet, nodeList, lastArgument.raw(), state);
      return new ArrayList<>(suggestionSet);
    }
    return Collections.emptyList();
  }

  @SuppressWarnings("unchecked")
  private void fillSuggestions(Set<String> suggestionSet,
      Collection<AbstractNode<T, ?>> nodeList,
      @NotNull String argumentInput, CommandState<T> state) {
    Set<String> unfilteredSuggestions = new HashSet<>();
    for (AbstractNode<T, ?> node : nodeList) {
      if(node instanceof LiteralNode<?> literalNode) {
        String literal = literalNode.literal();
        unfilteredSuggestions.add(literal);
      }
      if(node instanceof ValueNode<?>) {
        SuggestionResolver<T> suggestionResolver = ((ValueNode<T>) node).suggestionResolver();
        if(suggestionResolver == null) {
          continue;
        }
        HashSet<String> privateSet = new HashSet<>();
        suggestionResolver.resolve(privateSet, state.newState(node));
        unfilteredSuggestions.addAll(privateSet);
      }
    }
    for (String suggestion : unfilteredSuggestions) {
      if(suggestion.startsWith(argumentInput)) {
        suggestionSet.add(suggestion);
      }
    }
  }
}
