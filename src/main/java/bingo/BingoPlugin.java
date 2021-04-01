package bingo;

import bingo.commandExecutor.BingoCommandExecutor;
import bingo.commandExecutor.ResetCommandExecutor;
import bingo.commandExecutor.TopCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.eventhandler.CheckInventory;
import core.core.CoreMain;
import core.Utils;
import core.timer.Timer;
import core.settings.Setting.SettingCycle;
import core.settings.Setting.SettingSwitch;
import core.timer.TimerType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private final BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
    private static BingoSettings bingoSettings;
    private int difficulty = 0;
    public static int items = 0;
    public static SideList sideList;
    private static Timer timer;
    public static boolean scatter;

    @Override
    public void onEnable() {

        CoreMain.setPlugin(this);
        timer = new Timer(this, TimerType.INCREASING, "Das Bingo läuft seit: &b", "&cDas Bingo ist pausiert", false);
        bingoEventhandler.initialize();
        bingoSettings = new BingoSettings(this);
        BingoCommandExecutor bingoCommandExecutor = new BingoCommandExecutor(this);
        TopCommandExecutor topCommandExecutor = new TopCommandExecutor();
        ResetCommandExecutor resetCommandExecutor = new ResetCommandExecutor(this);
        sideList = new SideList(this);

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
                        }
                    }
                }
            }
        }, 0L, 1);
    }

    public void startBingo() {
        SettingCycle difficulty = (SettingCycle) bingoSettings.getSettingbyName("Schwierigkeit");
        SettingCycle items = (SettingCycle) bingoSettings.getSettingbyName("Items");
        SettingSwitch keepInventory = (SettingSwitch) bingoSettings.getSettingbyName("Keep Inventory");
        SettingSwitch singlePlayer = (SettingSwitch) bingoSettings.getSettingbyName("Singleplayer");
        SettingSwitch advancements = (SettingSwitch) bingoSettings.getSettingbyName("Announce Advancements");
        SettingSwitch scatter = (SettingSwitch) bingoSettings.getSettingbyName("Scatter Players");
        SettingCycle singlePlayerStartTime = (SettingCycle) bingoSettings.singlePlayerSubSettings.getSettingbyName("Start-Zeit");
        SettingCycle scatterPlayerSize = (SettingCycle) bingoSettings.scatterPlayerSubSettings.getSettingbyName("Scatter-Größe");
        this.difficulty = difficulty.getValue();
        this.scatter = scatter.getSettingValue();
        if (advancements.getSettingValue()) {
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
        } else {
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }
        boolean singleplayer = singlePlayer.getSettingValue();
        if (keepInventory.getSettingValue()) {
            Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
        }
        if (singleplayer) {
            timer.setTimerType(TimerType.DECREASING);
            timer.setSeconds(singlePlayerStartTime.getValue());
            timer.setSingle(true);
        }
        this.items = items.getValue();
        BingoList.populatePlayerBingoList(difficulty.getValue(), items.getValue());
        bingo.Utils.preparePlayers(scatterPlayerSize.getValue());
        sideList.init();
        timer.resume();
    }

    public void onDisable() {
    }

    public BingoSettings getBingoSettings() {
        return bingoSettings;
    }

    public int getDifficulty() {
        return this.difficulty;
    }

    public static Timer getTimer() {
        return timer;
    }

}
