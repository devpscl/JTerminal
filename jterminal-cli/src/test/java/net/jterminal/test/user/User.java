package net.jterminal.test.user;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class User {

  private final String name;
  private int securityLevel = 1;
  private String password;
  private final Set<String> permissions = new HashSet<>();

  public User(@NotNull String name, @NotNull  String password) {
    this.name = name;
    this.password = password;
  }

  public int securityLevel() {
    return securityLevel;
  }

  public void securityLevel(int securityLevel) {
    this.securityLevel = securityLevel;
  }

  public @NotNull String name() {
    return name;
  }

  public @NotNull String password() {
    return password;
  }

  public void password(@NotNull String password) {
    this.password = password;
  }

  public void addPermission(@NotNull String permission) {
    permissions.add(permission);
  }

  public void removePermission(@NotNull String permission) {
    permissions.remove(permission);
  }

  public Collection<String> permissions() {
    return Collections.unmodifiableCollection(permissions);
  }
}
