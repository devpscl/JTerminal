package net.jterminal.cli.commodore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.commodore.CommodoreFunctional.*;
import net.jterminal.cli.exception.CommandBuildException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractNode<T extends CommandArgument, I> {

  private final List<AbstractNode<T, ?>> childNodes = new ArrayList<>();
  private Executor<T> executor;
  private Executor<T> unknownCommandExecutor;
  private StyleEditor<T> styleEditor;
  AbstractNode<T, ?> parent;

  protected abstract I getThis();

  public I add(@NotNull AbstractNode<T, ?> arg) throws CommandBuildException {
    if(arg.parent != null) {
      throw new CommandBuildException("Node already added in another");
    }
    arg.parent = this;
    childNodes.add(arg);
    childNodes.sort(new NodeComparator());
    return getThis();
  }

  public @NotNull Collection<AbstractNode<T, ?>> nodes() {
    return childNodes;
  }

  public I execute(@NotNull Executor<T> executor) {
    this.executor = executor;
    return getThis();
  }

  public @Nullable Executor<T> executor() {
    return executor;
  }

  public I unknownCommandExecute(@NotNull Executor<T> executor) {
    this.unknownCommandExecutor = executor;
    return getThis();
  }

  public @Nullable Executor<T> unknownCommandExecutor() {
    if(unknownCommandExecutor != null) {
      return unknownCommandExecutor;
    }
    if(parent != null) {
      return parent.unknownCommandExecutor();
    }
    return null;
  }

  public I styleEditor(@NotNull StyleEditor<T> styleEditor) {
    this.styleEditor = styleEditor;
    return getThis();
  }

  public @Nullable StyleEditor<T> styleEditor() {
    return styleEditor;
  }

  public @NotNull AbstractNode<T, ?> rootNode() {
    if(parent == null) {
      return this;
    }
    return parent.rootNode();
  }

  public int depth() {
    if(parent == null) {
      return 1;
    }
    return parent.depth() + 1;
  }

  @SuppressWarnings("unchecked")
  public AbstractNode<T, ?>[] getOrderFromRoot() {
    int depth = depth();
    AbstractNode<T, ?>[] arr = new AbstractNode[depth];
    AbstractNode<T, ?> lastNode = this;
    for(int idx = depth; idx > 0; idx--) {
      arr[idx - 1] = lastNode;
      lastNode = lastNode.parent;
    }
    return arr;
  }

  private static class NodeComparator implements Comparator<AbstractNode<?, ?>> {

    @Override
    public int compare(AbstractNode<?, ?> o1, AbstractNode<?, ?> o2) {
      if(o1 instanceof LiteralNode<?>) {
        return 1;
      }
      if(o2 instanceof LiteralNode<?>) {
        return -1;
      }
      return 0;
    }
  }

}
