package net.jterminal.cli.commodore;

import net.jterminal.cli.command.CommandArgument;
import net.jterminal.cli.commodore.CommodoreFunctional.SuggestionResolver;
import org.jetbrains.annotations.NotNull;

public class ValueNode<T extends CommandArgument> extends AbstractNode<T, ValueNode<T>> {

  private SuggestionResolver<T> suggestionResolver;

  private final String id;

  public ValueNode(@NotNull String id) {
    this.id = id;
  }

  public @NotNull String id() {
    return id;
  }

  public ValueNode<T> suggestionResolver(
      SuggestionResolver<T> suggestionResolver) {
    this.suggestionResolver = suggestionResolver;
    return getThis();
  }

  public SuggestionResolver<T> suggestionResolver() {
    return suggestionResolver;
  }

  @Override
  protected ValueNode<T> getThis() {
    return this;
  }

  @Override
  public String toString() {
    String prefix = "";
    if(parent != null) {
      prefix = parent + "_";
    }
    return prefix + "ValueArg(?)";
  }

}
