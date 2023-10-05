package lee.code.locks.lang;

import lee.code.locks.utils.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&4&lLocks &6➔ "),
  USAGE("&6&lUsage: &e{0}"),
  COMMAND_LOCK_ADD_SUCCESS("&aYou successfully added the player &6{0} &ato the lock sign in front of you."),
  LOCK_SIGN_TITLE("&4[&c&lLocked&4]"),
  LOCK_SIGN_CREATE_SUCCESS("&aYou successfully created a new lock sign!"),
  LOCK_SIGN_OWNER("{0}"),
  LOCK_SIGN_SIGN_BREAK_SUCCESS("&7You broke a lock sign."),
  LOCK_INFO_TITLE("&a----------- &e[ &2&lLock Info &e] &a-----------"),
  LOCK_INFO_OWNER("&3&lOwner&7: {0}"),
  LOCK_INFO_TRUSTED("&3&lTrusted&7: {0}"),
  LOCK_INFO_FOOTER("&a------------------------------------"),
  ERROR_SIGN_NOT_OWNER_OPEN("&cOnly the lock owner and trusted lock players can open this container."),
  ERROR_SIGN_CREATE_HANGING_SIGN("&cYou cannot create a lock with a hanging sign."),
  ERROR_SIGN_NOT_DIRECTION_SIGN("&cA lock sign needs to be placed on a container to work."),
  ERROR_SIGN_BLOCK_NOT_SUPPORTED("&cThe block &3{0} &cis not a supported block you can lock."),
  ERROR_SIGN_BLOCK_HAS_LOCK("&cThat block already has a lock sign on it."),
  ERROR_SIGN_NOT_OWNER_BREAK("&cOnly the owner of the lock can break this."),
  ERROR_NO_PERMISSION("&cYou do not have permission for this."),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ERROR_NOT_CONSOLE_COMMAND("&cThis command does not work in console."),
  ERROR_NO_PLAYER_DATA("&cCould not find any player data for &6{0}&c."),
  ERROR_LOCK_ADD_NOT_OWNER("&cOnly the owner of the lock sign can add trusted players."),
  ERROR_LOCK_ADD_SELF("&cYou can't trust yourself to your own lock."),
  ERROR_LOCK_ADD_NO_SIGN("&cCould not find lock sign in front of you."),
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
