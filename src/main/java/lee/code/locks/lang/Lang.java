package lee.code.locks.lang;

import lee.code.locks.utils.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&c&lLocks &6➔ "),
  USAGE("&6&lUsage: &e{0}"),
  LOCK_SIGN_TITLE("&4[&c&lLocked&4]"),
  LOCK_SIGN_CREATE_SUCCESS("&aYou successfully created a new lock sign!"),
  LOCK_SIGN_OWNER("{0}"),
  LOCK_SIGN_SIGN_BREAK_SUCCESS("&7You broke a player lock sign."),
  ERROR_SIGN_NOT_OWNER_OPEN("&cOnly the owner and trusted lock players can open this container."),
  ERROR_SIGN_CREATE_HANGING_SIGN("&cYou cannot create a lock with a hanging sign."),
  ERROR_SIGN_NOT_DIRECTION_SIGN("&cA lock sign needs to be placed on a container to work."),
  ERROR_SIGN_BLOCK_NOT_SUPPORTED("&cThe block &3{0} &cis not a supported block you can lock."),
  ERROR_SIGN_BLOCK_HAS_LOCK("&cThat block already has a lock sign on it."),
  ERROR_SIGN_NOT_OWNER_BREAK("&cOnly the owner of the shop can break this."),
  ;
  @Getter
  private final String string;

  public String getString(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return value;
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return value;
  }

  public Component getComponent(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return CoreUtil.parseColorComponent(value);
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return CoreUtil.parseColorComponent(value);
  }
}
