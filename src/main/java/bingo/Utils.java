package bingo;

import bingo.eventhandler.CheckInventory;
import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import bingo.summarizer.SummarizerCore;
import bingo.teleporter.Teleporter;
import core.core.CoreMain;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.hotbar.HotbarScheduler;
import core.timer.Timer;
import core.timer.TimerType;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.graalvm.compiler.core.common.util.Util;

import java.util.Random;

public class Utils {

    private static Plugin main;

    public Utils(BingoPlugin main) {
        Utils.main = main;
    }

    public void init() {
        core.Utils.changeGamerule(GameRule.DO_DAYLIGHT_CYCLE, false);
        Banner banner = new Banner(main);
        BingoPlugin.setTimer(new Timer(main, TimerType.INCREASING, "Das Bingo läuft seit: &b", "&cDas Bingo ist pausiert", false));
        for (Player player : Bukkit.getOnlinePlayers()) {

            HotbarScheduler hotbarScheduler = CoreMain.hotbarManager.getHotbarScheduler(player);

            hotbarScheduler.setTimer(BingoPlugin.getTimer());
            hotbarScheduler.startScheduler(false);
        }
    }

    private static void startCompletedChecker() {
        BukkitScheduler scheduler = main.getServer().getScheduler();

        scheduler.scheduleSyncRepeatingTask(main, new Runnable() {

            @Override
            public void run() {
                if (!BingoPlugin.getTimer().isPaused()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!BingoList.completed(player)) {
                            BingoInventory.updateInventory(player);
                            CheckInventory.checkInventory(player, core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items"));
                        } else {
                            BingoPlugin.getTimer().pause();
                            core.Utils.sendMessageToEveryone(core.Utils.getPrefix("Bingo") + core.Utils.colorize("&e" + core.Utils.getDisplayName(player) + " &fhat das Bingo in &a" + core.Utils.formatTimerTimeTicksThreeDecimal(BingoPlugin.getTimer().getTicks()) + "&f beendet!"));
                            core.Utils.playSoundForAll(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            for (Player player1 : Bukkit.getOnlinePlayers()) {
                                player1.openInventory(BingoInventory.getPlayerInventory(player));
                            }
                        }
                    }
                }
            }
        }, 0L, 1);
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
        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Items Bannen")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.openInventory(BingoInventory.getPlayerInventory(player));
            }
            Banner.startBanning(scatterSize);
        } else {
            if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Scatter Players")) {
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

    public static void startBingo() {

        core.Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false);

        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Keep Inventory")) {
            core.Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
        }

        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Singleplayer")) {
            BingoPlugin.getTimer().setTimerType(TimerType.DECREASING);
            BingoPlugin.getTimer().setSeconds(core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Start-Zeit"));
            BingoPlugin.getTimer().setSingle(true);
        }

        BingoList.populatePlayerBingoList(core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Schwierigkeit"), core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items"));
        bingo.Utils.preparePlayers(core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Scatter-Größe"));
        BingoPlugin.sideList.init();
        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Teleporter")) {
            Teleporter teleporter = new Teleporter((BingoPlugin) main, true, core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().teleporterSubSettings, "Countdown-Zeit"), core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().teleporterSubSettings, "Teleporter-Radius"));
            teleporter.init();
        }
        startCompletedChecker();
        for (Player player : Bukkit.getOnlinePlayers()) {

            HotbarScheduler hotbarScheduler = CoreMain.hotbarManager.getHotbarScheduler(player);
            hotbarScheduler.scheduleRepeatingMessage(core.Utils.colorize("Verwende &c/bingo respawn&f um an dem Ort deines Todes zu spawnen!"), 24000, 250, 4800);
            hotbarScheduler.scheduleRepeatingMessage(core.Utils.colorize("Mit &6/top&f kannst du aus einer Höhle an die Oberfläche kommen!"), 24000, 250, 9600);
            hotbarScheduler.scheduleRepeatingMessage(core.Utils.colorize("Du brauchst eine Pause? Verwende &a/bingo pause&f um das Bingo zu pausieren!"), 24000, 250, 14400);
            hotbarScheduler.scheduleRepeatingMessage(core.Utils.colorize("Du möchtest verreisen? Mit &5/rtp&f kannst du dich alle " + (core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().teleporterSubSettings, "Countdown-Zeit")) / 60) + " Minuten teleportieren!", 24000, 250, 19200);
            hotbarScheduler.scheduleRepeatingMessage(core.Utils.colorize("Verlierst du den Überblick? Verwende &e/bingo&f um eine Übersicht über dein Bingo zu erhalten!"), 24000, 250, 24000);

        }
        BingoPlugin.getTimer().

                resume();
    }

}
