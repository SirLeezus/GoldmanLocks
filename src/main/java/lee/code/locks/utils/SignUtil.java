package lee.code.locks.utils;

import lee.code.locks.Locks;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Sign;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SignUtil {
  public static UUID getLockOwner(Locks locks, Sign sign) {
    final PersistentDataContainer container = sign.getPersistentDataContainer();
    final NamespacedKey key = new NamespacedKey(locks, "sign-lock-owner");
    final String owner = container.get(key, PersistentDataType.STRING);
    if (owner != null) return UUID.fromString(owner);
    else return null;
  }

  public static String getLockTrusted(Locks locks, Sign sign) {
    final PersistentDataContainer container = sign.getPersistentDataContainer();
    final NamespacedKey key = new NamespacedKey(locks, "sign-lock-trusted");
    return container.get(key, PersistentDataType.STRING);
  }

  public static void addTrusted(Locks locks, Sign sign, UUID trusting) {
    final PersistentDataContainer signContainer = sign.getPersistentDataContainer();
    String trusted = getLockTrusted(locks, sign);
    if (trusted != null) trusted = trusted + "," + trusting;
    else trusted = trusting.toString();
    final NamespacedKey trustedKey = new NamespacedKey(locks, "sign-lock-trusted");
    signContainer.set(trustedKey, PersistentDataType.STRING, trusted);
    sign.update(true, false);
  }

  public static boolean isTrusted(Locks locks, Sign sign, UUID targetID) {
    final String trusted = getLockTrusted(locks, sign);
    if (trusted == null) return false;
    final List<String> trustedList = new ArrayList<>(List.of(trusted.split(",")));
    return trustedList.contains(targetID.toString());
  }
}
