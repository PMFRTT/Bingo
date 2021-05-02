package bingo;

import bingo.eventhandler.CheckInventory;
import bingo.summarizer.SummarizerCore;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.settings.Setting.Setting;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Random;

public class Utils {

    private static Plugin main;

    public Utils(BingoPlugin main) {
        Utils.main = main;
    }

    public static void scatterPlayer(Player player, int scatterSize, boolean fromSpawn) {
        Random random = new Random();
        World world = Bukkit.getWorld("world");
        Location location = player.getLocation();
        do {
            double x = (random.nextFloat() - 0.5f) * scatterSize;
            double z = (random.nextFloat() - 0.5f) * scatterSize;
            location.setX(x);
            location.setZ(z);
            assert world != null;
            location.setY(world.getHighestBlockYAt((int) x, (int) z));
        } while (location.getBlock().isLiquid());
        location.add(0, 2, 0);
        if (fromSpawn) {
            player.setBedSpawnLocation(location, true);
            player.teleport(location);
        } else {
            player.setBedSpawnLocation(location, true);
            Location location1 = player.getLocation().add(location.getX(), 0, location.getZ());
            location1.setY(world.getHighestBlockYAt(location1));
            player.teleport(location1);
        }
    }

    public static void preparePlayers(int scatterSize) {
        clearPlayers();
        SummarizerCore.init();
        CheckInventory.createLock();
        if (BingoPlugin.banningEnabled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.openInventory(BingoInventory.getPlayerInventory(player));
            }
            Banner.startBanning(scatterSize);
        } else {
            if (BingoPlugin.scatter) {
                for (Player player : Bukkit.getOnlinePlayers())
                    scatterPlayer(player, scatterSize, true);
            }
        }
        DebugSender.sendDebug(DebugType.PLUGIN, "players have been prepared", "Bingo");
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

    public void getSettings(){
        BingoSettings bingoSettings = new BingoSettings(main);
        Setting difficulty = bingoSettings.getSettingbyName("Schwierigkeit");
        Setting items = bingoSettings.getSettingbyName("Items");
        Setting keepInventory = bingoSettings.getSettingbyName("Keep Inventory");
        Setting singlePlayer = bingoSettings.getSettingbyName("Singleplayer");
        Setting scatter = bingoSettings.getSettingbyName("Scatter Players");
        Setting singlePlayerStartTime = bingoSettings.singlePlayerSubSettings.getSettingbyName("Start-Zeit");
        Setting scatterPlayerSize = bingoSettings.scatterPlayerSubSettings.getSettingbyName("Scatter-Größe");
        Setting teleportTime = bingoSettings.teleporterSubSettings.getSettingbyName("Countdown-Zeit");
        Setting teleportRange = bingoSettings.teleporterSubSettings.getSettingbyName("Teleporter-Radius");
        Setting enabletp = bingoSettings.getSettingbyName("Teleporter");
        Setting banning = bingoSettings.getSettingbyName("Items Bannen");
        Setting banningItems = bingoSettings.banningSettings.getSettingbyName("Anzahl der Items");
    }

}
