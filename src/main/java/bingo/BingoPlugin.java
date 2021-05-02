package bingo;

import bingo.commandExecutor.BingoCommandExecutor;
import bingo.commandExecutor.ResetCommandExecutor;
import bingo.commandExecutor.TopCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.eventhandler.CheckInventory;
import bingo.teleporter.Respawner;
import bingo.teleporter.Teleporter;
import core.core.CoreMain;
import core.Utils;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.settings.Setting.Setting;
import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private static BingoSettings bingoSettings;

    private static Timer timer;

    public static int items = 9;
    public static int bannableItems = 0;
    public static SideList sideList;
    public static boolean scatter;
    public static boolean tpEnabled = false;
    public static boolean banningEnabled = false;

    public static Respawner respawner;

    @Override
    public void onEnable() {

        Banner banner = new Banner(this);

        bingo.Utils utils = new bingo.Utils(this);
        CoreMain.setPlugin(this);
        timer = new Timer(this, TimerType.INCREASING, "Das Bingo läuft seit: &b", "&cDas Bingo ist pausiert", false);
        BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
        bingoEventhandler.initialize();
        bingoSettings = new BingoSettings(this);
        BingoCommandExecutor bingoCommandExecutor = new BingoCommandExecutor(this);
        TopCommandExecutor topCommandExecutor = new TopCommandExecutor();
        ResetCommandExecutor resetCommandExecutor = new ResetCommandExecutor(this);
        sideList = new SideList(this);
        respawner = new Respawner(this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            CoreMain.hotbarManager.getHotbarScheduler(player).setTimer(getTimer());
            CoreMain.hotbarManager.getHotbarScheduler(player).startScheduler(false);
        }

        Objects.requireNonNull(getCommand("Bingo")).setExecutor(bingoCommandExecutor);
        Objects.requireNonNull(getCommand("Top")).setExecutor(topCommandExecutor);
        Objects.requireNonNull(getCommand("reset")).setExecutor(resetCommandExecutor);
        Objects.requireNonNull(getCommand("rtp")).setExecutor(bingoCommandExecutor);

        BukkitScheduler scheduler1 = getServer().getScheduler();

        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (!timer.isPaused()) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!BingoList.completed(player)) {
                            BingoInventory.updateInventory(player);
                            CheckInventory.checkInventory(player, items);
                        } else {
                            timer.pause();
                            Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + Utils.getDisplayName(player) + " &fhat das Bingo in &a" + Utils.formatTimerTimeTicksThreeDecimal(timer.getTicks()) + "&f beendet!"));
                            Utils.playSoundForAll(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            for (Player player1 : Bukkit.getOnlinePlayers()) {
                                player1.openInventory(BingoInventory.getPlayerInventory(player));
                            }
                        }
                    }
                }
            }
        }, 0L, 1);

        DebugSender.sendDebug(DebugType.PLUGIN, "bingo loaded", "Bingo");

    }

    public void startBingo() {

        Setting<Integer> difficulty = bingoSettings.getSettingbyName("Schwierigkeit");
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

        banningEnabled = (boolean) banning.getValue();
        tpEnabled = (boolean) enabletp.getValue();
        BingoPlugin.scatter = (boolean) scatter.getValue();
        boolean singleplayer = (boolean) singlePlayer.getValue();
        if ((boolean) banning.getValue()) {
            bannableItems = (int) banningItems.getValue();
        }
        if ((boolean) keepInventory.getValue()) {
            Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
        }
        Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        if (singleplayer) {
            timer.setTimerType(TimerType.DECREASING);
            timer.setSeconds((Integer) singlePlayerStartTime.getValue());
            timer.setSingle(true);
        }
        int teleporterRadius = (int) teleportRange.getValue();
        int teleporterTime = (int) teleportTime.getValue();
        BingoList.populatePlayerBingoList(difficulty.getValue(), (int) items.getValue());
        bingo.Utils.preparePlayers((int) scatterPlayerSize.getValue());
        sideList.init();
        if ((boolean) enabletp.getValue()) {
            Teleporter teleporter = new Teleporter(this, true, teleporterTime, teleporterRadius);
            teleporter.init();
        }
        timer.resume();
    }

    public void onDisable() {
    }

    public static BingoSettings getBingoSettings() {
        return bingoSettings;
    }

    public static Timer getTimer() {
        return timer;
    }

}
