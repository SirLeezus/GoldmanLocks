package lee.code.locks;

import com.mojang.brigadier.tree.LiteralCommandNode;
import lee.code.locks.commands.CommandManager;
import lee.code.locks.commands.TabCompletion;
import lee.code.locks.listeners.LockSignListener;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Locks extends JavaPlugin {
  @Getter private Data data;
  @Getter private CommandManager commandManager;

  @Override
  public void onEnable() {
    this.data = new Data();
    this.commandManager = new CommandManager(this);

    registerCommands();
    registerListeners();
  }

  @Override
  public void onDisable() {

  }

  private void registerCommands() {
    getCommand("lock").setExecutor(commandManager);
    getCommand("lock").setTabCompleter(new TabCompletion(commandManager));
    loadCommodoreData();
  }


  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new LockSignListener(this), this);
  }

  private void loadCommodoreData() {
    try {
      final LiteralCommandNode<?> towns = CommodoreFileReader.INSTANCE.parse(getResource("lock.commodore"));
      CommodoreProvider.getCommodore(this).register(getCommand("lock"), towns);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
