package net.jterminal.test.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserManager {

  private final List<User> userList = new ArrayList<>();

  public void add(@NotNull User user) {
    userList.add(user);
  }

  public void remove(@NotNull User user) {
    userList.remove(user);
  }

  public @Nullable User getByName(@NotNull String name) {
    for (User user : userList) {
      if(user.name().equals(name)) {
        return user;
      }
    }
    return null;
  }

  public Collection<User> users() {
    return new ArrayList<>(userList);
  }

}
