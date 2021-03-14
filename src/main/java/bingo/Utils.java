package bingo;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Random;

public class Utils {

    private static void scatterPlayers() {
        Random random = new Random();
        World world = Bukkit.getWorld("world");
        for (Player player : Bukkit.getOnlinePlayers()) {
            Location location = player.getLocation();
            float x = (random.nextFloat() - 0.5f) * 20000;
            float z = (random.nextFloat() - 0.5f) * 20000;
            location.setX(x);
            location.setZ(z);
            assert world != null;
            location.setY(world.getHighestBlockYAt((int) x, (int) z));
            player.teleport(location);
        }
    }

    public static void preparePlayers(){
        clearPlayers();
        scatterPlayers();
    }

    private static void clearPlayers(){
        for(Player player : Bukkit.getOnlinePlayers()){
            player.getInventory().clear();
            player.setExp(0f);
            player.setLevel(0);
        }
    }

    public static String formatMaterialName(String materialName){
        String tmp = materialName.replace("_"," ");
        return tmp.toUpperCase();
    }

}
