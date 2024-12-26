package net.jterminal.cli.commodore;

import net.jterminal.cli.command.CommandArgument;
import org.jetbrains.annotations.NotNull;

public class LiteralNode<T extends CommandArgument> extends AbstractNode<T,
    LiteralNode<T>> {

  private final String literal;

  public LiteralNode(@NotNull String literal) {
    this.literal = literal;
  }

  @Override
  protected LiteralNode<T> getThis() {
    return this;
  }

  public @NotNull String literal() {
    return literal;
  }

  @Override
  public String toString() {
    String prefix = "";
    if(parent != null) {
      prefix = parent + "_";
    }
    return prefix + "LiteralArg(" + literal() + ")";
  }

}
