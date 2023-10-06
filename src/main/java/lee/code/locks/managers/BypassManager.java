package lee.code.locks.managers;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BypassManager {
  private final Set<UUID> bypassPlayers = ConcurrentHashMap.newKeySet();

  public void addBypassing(UUID uuid) {
    bypassPlayers.add(uuid);
  }

  public void removeBypassing(UUID uuid) {
    bypassPlayers.remove(uuid);
  }

  public boolean isBypassing(UUID uuid) {
    return bypassPlayers.contains(uuid);
  }
}
