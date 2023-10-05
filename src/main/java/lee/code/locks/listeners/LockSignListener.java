package lee.code.locks.listeners;

import lee.code.colors.ColorAPI;
import lee.code.locks.Locks;
import lee.code.locks.lang.Lang;
import lee.code.locks.utils.CoreUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LockSignListener implements Listener {
  private final Locks locks;

  public LockSignListener(Locks locks) {
    this.locks = locks;
  }

  @EventHandler
  public void onLockCreate(SignChangeEvent e) {
    final List<Component> lines = new ArrayList<>(e.lines());
    if (lines.isEmpty()) return;
    final PlainTextComponentSerializer plainTextComponentSerializer = PlainTextComponentSerializer.plainText();
    final String lockString = plainTextComponentSerializer.serialize(lines.get(0));
    if (!lockString.equalsIgnoreCase("[lock]")) return;
    final Player player = e.getPlayer();
    final UUID playerID = player.getUniqueId();
    final Block block = e.getBlock();
    if (!(block.getBlockData() instanceof Directional directional)) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_NOT_DIRECTION_SIGN.getComponent(null)));
      return;
    }
    if (block.getType().name().endsWith("HANGING_SIGN")) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_CREATE_HANGING_SIGN.getComponent(null)));
      return;
    }
    final Block blockBehind = block.getRelative(directional.getFacing().getOppositeFace());
    if (!locks.getData().getSupportedSignBlocks().contains(blockBehind.getType())) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_BLOCK_NOT_SUPPORTED.getComponent(new String[]{CoreUtil.capitalize(blockBehind.getType().name())})));
      return;
    }
    if (getLockSign(blockBehind) != null) {
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_BLOCK_HAS_LOCK.getComponent(null)));
      return;
    }

    final List<Component> newLines = new ArrayList<>();
    newLines.add(Lang.LOCK_SIGN_TITLE.getComponent(null));
    newLines.add(Lang.LOCK_SIGN_OWNER.getComponent(new String[]{ColorAPI.getNameColor(playerID, player.getName())}));
    for (int i = 0; i < newLines.size(); i++) e.line(i, newLines.get(i));

    final TileState state = (TileState) block.getState();
    final PersistentDataContainer signContainer = state.getPersistentDataContainer();
    final NamespacedKey lockOwner = new NamespacedKey(locks, "sign-lock-owner");
    final NamespacedKey trusted = new NamespacedKey(locks, "sign-lock-owner");

    signContainer.set(lockOwner, PersistentDataType.STRING, playerID.toString());
    signContainer.set(trusted, PersistentDataType.STRING, "");
    state.update(true, false);
    player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.LOCK_SIGN_CREATE_SUCCESS.getComponent(null)));
  }

  @EventHandler
  public void onLockBlockBreak(BlockBreakEvent e) {
    final Player player = e.getPlayer();
    final Block block = e.getBlock();
    final UUID playerID = player.getUniqueId();
    if (locks.getData().getSupportedSignBlocks().contains(e.getBlock().getType())) {
      final Sign sign = getLockSign(block);
      if (sign == null) return;
      final UUID ownerID = getLockOwner(sign);
      if (ownerID == null) return;
      if (!ownerID.equals(playerID)) {
        e.setCancelled(true);
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_NOT_OWNER_BREAK.getComponent(null)));
        return;
      }
      block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.LOCK_SIGN_SIGN_BREAK_SUCCESS.getComponent(null)));
    } else if (block.getState().getBlockData() instanceof WallSign) {
      final Sign sign = (Sign) block.getState();
      final UUID ownerID = getLockOwner(sign);
      if (ownerID == null) return;
      if (!ownerID.equals(playerID)) {
        e.setCancelled(true);
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_NOT_OWNER_BREAK.getComponent(null)));
        return;
      }
      block.getWorld().playSound(block.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
      player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.LOCK_SIGN_SIGN_BREAK_SUCCESS.getComponent(null)));
    }
  }

  @EventHandler
  public void onLockInteract(PlayerInteractEvent e) {
    if (!e.getAction().isRightClick()) return;
    final Player player = e.getPlayer();
    final UUID playerID = player.getUniqueId();
    final Block block = e.getClickedBlock();
    if (block == null) return;
    if (block.getState().getBlockData() instanceof WallSign) {
      final Sign sign = (Sign) block.getState();
      final UUID ownerID = getLockOwner(sign);
      if (ownerID == null) return;
      e.setCancelled(true);
      //TODO send lock info message
    } else if (locks.getData().getSupportedSignBlocks().contains(block.getType())) {
      final Sign sign = getLockSign(block);
      if (sign == null) return;
      final UUID ownerID = getLockOwner(sign);
      if (ownerID == null) return;
      if (!ownerID.equals(playerID)) {
        e.setCancelled(true);
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_SIGN_NOT_OWNER_OPEN.getComponent(null)));
      }
    }
  }

  @EventHandler
  public void onLockExplode(EntityExplodeEvent e) {
    for (Block block : new ArrayList<>(e.blockList())) {
      if (locks.getData().getSupportedSignBlocks().contains(block.getType())) {
        final Sign shopSign = getLockSign(block);
        if (shopSign != null) e.blockList().remove(block);
      } else if (block.getState().getBlockData() instanceof WallSign) {
        final Sign sign = (Sign) block.getState();
        final UUID ownerID = getLockOwner(sign);
        if (ownerID != null) e.blockList().remove(block);
      }
    }
  }

  private UUID getLockOwner(Sign sign) {
    final PersistentDataContainer container = sign.getPersistentDataContainer();
    final NamespacedKey key = new NamespacedKey(locks, "sign-lock-owner");
    final String owner = container.get(key, PersistentDataType.STRING);
    if (owner != null) return UUID.fromString(owner);
    else return null;
  }

  private Sign getLockSign(Block block) {
    final BlockState blockState = block.getState();
    final BlockFace[] faces = new BlockFace[]{BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    for (BlockFace face : faces) {
      final Block relativeBlock = block.getRelative(face);
      final BlockState relativeBlockState = relativeBlock.getState();
      if (blockState instanceof Chest chest && relativeBlockState instanceof Chest relativeChest) {
        final InventoryHolder inventoryHolder = chest.getInventory().getHolder();
        final InventoryHolder relativeInventoryHolder = relativeChest.getInventory().getHolder();
        if (inventoryHolder instanceof DoubleChest && relativeInventoryHolder instanceof DoubleChest) {
          final DoubleChestInventory inventory = (DoubleChestInventory) inventoryHolder.getInventory();
          final DoubleChestInventory relativeInventory = (DoubleChestInventory) relativeInventoryHolder.getInventory();
          final Location location = inventory.getLocation();
          final Location relativeLocation = relativeInventory.getLocation();
          if (location != null && location.equals(relativeLocation)) {
            for (BlockFace relativeFace : faces) {
              final Block relative = relativeBlock.getRelative(relativeFace);
              final BlockState relativeState = relative.getState();
              final Sign sign = getSign(relativeBlock, relative, relativeState);
              if (sign != null) return sign;
            }
          }
        }
      } else {
        final Sign sign = getSign(block, relativeBlock, relativeBlockState);
        if (sign != null) return sign;
      }
    }
    return null;
  }

  private Sign getSign(Block block, Block relativeBlock, BlockState relativeBlockState) {
    if (relativeBlockState.getBlockData() instanceof WallSign) {
      final Sign sign = (Sign) relativeBlockState;
      final Directional signDirectional = (Directional) sign.getBlockData();
      final Block relativeBlockBehind = relativeBlock.getRelative(signDirectional.getFacing().getOppositeFace());
      if (relativeBlockBehind.equals(block)) {
        final PersistentDataContainer container = sign.getPersistentDataContainer();
        final NamespacedKey owner = new NamespacedKey(locks, "sign-lock-owner");
        if (container.has(owner, PersistentDataType.STRING)) return sign;
      }
    }
    return null;
  }
}
