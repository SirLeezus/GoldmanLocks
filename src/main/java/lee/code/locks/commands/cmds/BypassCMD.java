package lee.code.locks.commands.cmds;

import lee.code.locks.Locks;
import lee.code.locks.commands.SubCommand;
import lee.code.locks.lang.Lang;
import lee.code.locks.managers.BypassManager;
import lee.code.locks.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BypassCMD extends SubCommand {
  private final Locks locks;

  public BypassCMD(Locks locks) {
    this.locks = locks;
  }
  @Override
  public String getName() {
    return "bypass";
  }

  @Override
  public String getDescription() {
    return "Toggle admin bypass.";
  }

  @Override
  public String getSyntax() {
    return "/lock bypass";
  }

  @Override
  public String getPermission() {
    return "locks.command.bypass";
  }

  @Override
  public boolean performAsync() {
    return true;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return false;
  }

  @Override
  public void perform(Player player, String[] args) {
    final BypassManager bypassManager = locks.getBypassManager();
    final UUID uuid = player.getUniqueId();
    if ( bypassManager.isBypassing(uuid)) {
      bypassManager.removeBypassing(uuid);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_BYPASS_TOGGLE_SUCCESS.getComponent(new String[]{Lang.OFF.getString()})));
    } else {
      bypassManager.addBypassing(uuid);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_BYPASS_TOGGLE_SUCCESS.getComponent(new String[]{Lang.ON.getString()})));
    }
  }

  @Override
  public void performConsole(CommandSender console, String[] args) {
    console.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
  }

  @Override
  public void performSender(CommandSender sender, String[] args) {
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    if (args.length == 2) return StringUtil.copyPartialMatches(args[1], CoreUtil.getOnlinePlayers(), new ArrayList<>());
    return new ArrayList<>();
  }
}
