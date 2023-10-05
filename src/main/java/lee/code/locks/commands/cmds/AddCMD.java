package lee.code.locks.commands.cmds;

import lee.code.locks.Locks;
import lee.code.locks.commands.SubCommand;
import lee.code.locks.lang.Lang;
import lee.code.locks.utils.CoreUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AddCMD extends SubCommand {
  private final Locks locks;

  public AddCMD(Locks locks) {
    this.locks = locks;
  }
  @Override
  public String getName() {
    return "add";
  }

  @Override
  public String getDescription() {
    return "Add a player to the sign lock you're looking at.";
  }

  @Override
  public String getSyntax() {
    return "/lock add &f<player>";
  }

  @Override
  public String getPermission() {
    return "lock.command.add";
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
