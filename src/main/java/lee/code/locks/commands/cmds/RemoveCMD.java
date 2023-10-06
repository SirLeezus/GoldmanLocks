package lee.code.locks.commands.cmds;

import lee.code.colors.ColorAPI;
import lee.code.locks.Locks;
import lee.code.locks.commands.SubCommand;
import lee.code.locks.lang.Lang;
import lee.code.locks.utils.CoreUtil;
import lee.code.locks.utils.SignUtil;
import lee.code.playerdata.PlayerDataAPI;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemoveCMD extends SubCommand {
  private final Locks locks;

  public RemoveCMD(Locks locks) {
    this.locks = locks;
  }
  @Override
  public String getName() {
    return "remove";
  }

  @Override
  public String getDescription() {
    return "Remove a trusted player from the sign lock you're looking at.";
  }

  @Override
  public String getSyntax() {
    return "/lock remove &f<player>";
  }

  @Override
  public String getPermission() {
    return "lock.command.remove";
  }

  @Override
  public boolean performAsync() {
    return false;
  }

  @Override
  public boolean performAsyncSynchronized() {
    return false;
  }

  @Override
  public void perform(Player player, String[] args) {
    if (args.length < 2) {
      player.sendMessage(Lang.USAGE.getComponent(new String[]{getSyntax()}));
      return;
    }
    final Block block = player.getTargetBlockExact(5);
    if (block == null) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LOCK_NO_SIGN.getComponent(null)));
      return;
    }
    if (!(block.getState() instanceof Sign sign)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LOCK_NO_SIGN.getComponent(null)));
      return;
    }
    final UUID ownerID = SignUtil.getLockOwner(locks, sign);
    if (ownerID == null) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LOCK_NO_SIGN.getComponent(null)));
      return;
    }
    if (!ownerID.equals(player.getUniqueId())) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LOCK_REMOVE_NOT_OWNER.getComponent(null)));
      return;
    }
    final String targetString = args[1];
    final UUID targetID = PlayerDataAPI.getUniqueId(targetString);
    if (targetID == null) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_PLAYER_DATA.getComponent(new String[]{targetString})));
      return;
    }
    if (!SignUtil.isTrusted(locks, sign, targetID)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_LOCK_REMOVE_NOT_TRUSTED.getComponent(new String[]{ColorAPI.getNameColor(targetID, targetString)})));
      return;
    }
    SignUtil.removeTrusted(locks, sign, targetID);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.COMMAND_LOCK_REMOVE_SUCCESS.getComponent(new String[]{ColorAPI.getNameColor(targetID, targetString)})));
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
