package lee.code.locks;

import lee.code.locks.listeners.LockSignListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Locks extends JavaPlugin {
  @Getter private Data data;

  @Override
  public void onEnable() {
    this.data = new Data();

    registerListeners();
  }

  @Override
  public void onDisable() {

  }

  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new LockSignListener(this), this);
  }
}
