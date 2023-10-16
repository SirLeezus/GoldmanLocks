package lee.code.locks.commands.cmds;

import lee.code.locks.Locks;
import lee.code.locks.commands.SubCommand;
import lee.code.locks.lang.Lang;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SignHelpCMD extends SubCommand {
  private final Locks locks;

  public SignHelpCMD(Locks locks) {
    this.locks = locks;
  }
  @Override
  public String getName() {
    return "signhelp";
  }

  @Override
  public String getDescription() {
    return "A guide on how to create a sign lock.";
  }

  @Override
  public String getSyntax() {
    return "/lock signhelp";
  }

  @Override
  public String getPermission() {
    return "locks.command.signhelp";
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
    final List<Component> lines = new ArrayList<>();
    lines.add(Lang.COMMAND_SIGN_HELP_DIVIDER.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_SIGN_HELP_TITLE.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_SIGN_HELP_STEP_1.getComponent(null));
    lines.add(Lang.COMMAND_SIGN_HELP_STEP_2.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_SIGN_HELP_STEP_3_SIGN_1.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_SIGN_HELP_DONE.getComponent(null));
    lines.add(Component.text(""));
    lines.add(Lang.COMMAND_SIGN_HELP_DIVIDER.getComponent(null));
    for (Component line : lines) player.sendMessage(line);
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
    return new ArrayList<>();
  }
}
