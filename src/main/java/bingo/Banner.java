package bingo;


import bingo.eventhandler.CheckInventory;
import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Random;

import static bingo.Utils.scatterPlayer;

public class Banner {

    private static HashMap<String, Timer> playerTimers = new HashMap<String, Timer>();
    private static HashMap<String, Integer> playerIndexTimers = new HashMap<String, Integer>();

    private static Plugin plugin;

    public Banner(Plugin plugin) {
        Banner.plugin = plugin;
    }

    public static void startBanning(int scatterSize) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerTimers.put(player.getDisplayName(), new Timer(plugin, TimerType.INCREASING, "", "", true, "", player));
            playerTimers.get(player.getDisplayName()).resume();
            playerIndexTimers.put(player.getDisplayName(), 0);
        }
        BukkitTask runnable = new BukkitRunnable() {
            @Override
            public void run() {
                boolean done = true;
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (BingoInventory.bannedItem.get(player.getDisplayName()).size() != core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().banningSettings, "Anzahl der Items")) {
                        done = false;
                    }
                    if ((boolean) BingoPlugin.getBingoSettings().banningSettings.getSettingbyName("Automatisches Bannen").getValue()) {
                        Timer timer = playerTimers.get(player.getDisplayName());
                        Random random = new Random();
                        if (timer.getSeconds() == ((Integer) BingoPlugin.getBingoSettings().banningSettings.getSettingbyName("Erster Timeout").getValue() + playerIndexTimers.get(player.getDisplayName()) * (Integer) BingoPlugin.getBingoSettings().banningSettings.getSettingbyName("Bann Abstand").getValue()) && BingoInventory.bannedItem.get(player.getDisplayName()).size() < core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().banningSettings, "Anzahl der Items")) {
                            while (!banItem(player, BingoList.getBingoList().get(random.nextInt(BingoList.getBingoList().size()))));
                        }
                    }
                }
                if (done) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.closeInventory();
                        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Scatter Players")) {
                            scatterPlayer(player, scatterSize, true);
                        }
                    }
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 1L);
    }

    public static boolean banItem(Player player, Material material) {
        if (BingoInventory.bannedItem.get(player.getDisplayName()).size() < core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().banningSettings, "Anzahl der Items")) {
            if (!BingoList.playerCollectedList.get(player.getDisplayName()).contains(material)) {
                assert BingoList.getBingoList() != null;
                CheckInventory.playerBans.get(player.getDisplayName()).add(BingoList.getBingoList().indexOf(material));
                BingoInventory.bannedItem.get(player.getDisplayName()).add(material);
                BingoList.addMaterialToCollected(player, material);
                SideList.updateScoreboard();
                playerIndexTimers.put(player.getDisplayName(), playerIndexTimers.get(player.getDisplayName()) + 1);
                return true;
            }
        }
        return false;
    }

}
