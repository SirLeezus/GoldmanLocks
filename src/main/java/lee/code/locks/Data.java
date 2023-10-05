package lee.code.locks;

import lee.code.locks.enums.SignBlock;
import lombok.Getter;
import org.bukkit.Material;

import java.util.HashSet;
import java.util.Set;

public class Data {
  @Getter private final Set<Material> supportedSignBlocks = new HashSet<>();

  public Data() {
    loadData();
  }

  private void loadData() {
    for (SignBlock signBlock : SignBlock.values()) supportedSignBlocks.add(signBlock.getMaterial());
  }
}
