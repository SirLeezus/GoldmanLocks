package lee.code.locks.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CoreUtil {
  private final static DecimalFormat amountFormatter = new DecimalFormat("#,###.##");
  private final static Pattern numberDoublePattern = Pattern.compile("^(?=.*[1-9])(\\d*\\.?\\d*)$");

  public static String parseValue(int value) {
    if (value == 0) return "0";
    return amountFormatter.format(value);
  }

  public static String parseValue(double value) {
    if (value == 0) return "0";
    return amountFormatter.format(value);
  }

  public static Component parseColorComponent(String text) {
    final LegacyComponentSerializer serializer = LegacyComponentSerializer.legacyAmpersand();
    return (Component.empty().decoration(TextDecoration.ITALIC, false)).append(serializer.deserialize(text));
  }

  @SuppressWarnings("deprecation")
  public static String capitalize(String message) {
    final String format = message.toLowerCase().replaceAll("_", " ");
    return WordUtils.capitalize(format);
  }

  public static boolean isPositiveDoubleNumber(String numbers) {
    return numberDoublePattern.matcher(numbers).matches();
  }

  public static String serializeLocation(Location location) {
    if (location == null) return null;
    else if (location.getWorld() == null) return null;
    return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getYaw() + "," + location.getPitch();
  }

  public static Location parseLocation(String location) {
    if (location == null) return null;
    final String[] split = location.split(",", 6);
    return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]), (float) Double.parseDouble(split[4]), (float) Double.parseDouble(split[5]));
  }

  public static List<String> getOnlinePlayers() {
    return Bukkit.getOnlinePlayers().stream()
      .filter(player -> !player.getGameMode().equals(GameMode.SPECTATOR))
      .map(Player::getName)
      .collect(Collectors.toList());
  }

  public static String getTextBeforeCharacter(String input, char character) {
    final int index = input.indexOf(character);
    if (index == -1) return input;
    else return input.substring(0, index);
  }

  public static <K, V extends Comparable<? super V>> HashMap<K, V> sortByValue(Map<K, V> hm, Comparator<V> comparator) {
    final HashMap<K, V> temp = new LinkedHashMap<>();
    hm.entrySet().stream()
      .sorted(Map.Entry.comparingByValue(comparator))
      .forEachOrdered(entry -> temp.put(entry.getKey(), entry.getValue()));
    return temp;
  }
}
