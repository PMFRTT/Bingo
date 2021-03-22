package bingo;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Random;

public class Utils {

    private static void scatterPlayers() {
        Random random = new Random();
        World world = Bukkit.getWorld("world");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            do {
                float x = (random.nextFloat() - 0.5f) * 20000;
                float z = (random.nextFloat() - 0.5f) * 20000;
                location.setX(x);
                location.setZ(z);
                assert world != null;
                location.setY(world.getHighestBlockYAt((int) x, (int) z));
            } while (location.getBlock().isLiquid());
            location.add(0, 2, 0);
            player.setBedSpawnLocation(location, true);
            player.teleport(location);
        }
    }

    public static void preparePlayers() {
        clearPlayers();
        if (BingoPlugin.scatter) {
            scatterPlayers();
        }
    }

    private static void clearPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            player.setExp(0f);
            player.setLevel(0);
            core.Utils.heal(player);
        }
    }

    public static String formatMaterialName(Material material) {
        String materialName = material.toString();
        String tmp = materialName.replace("_", " ");
        String tmp2 = tmp.toLowerCase();
        return WordUtils.capitalize(tmp2);
    }


}
