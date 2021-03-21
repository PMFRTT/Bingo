package bingo;

import bingo.commandExecutor.BingoCommandExecutor;
import bingo.commandExecutor.ResetCommandExecutor;
import bingo.commandExecutor.TopCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.eventhandler.CheckInventory;
import core.core.CoreMain;
import core.Utils;
import core.timer.Timer;
import core.core.CoreSendStringPacket;
import core.settings.SettingCycle;
import core.settings.SettingSwitch;
import core.timer.TimerType;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private final BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
    public BingoSettings bingoSettings;

    public int difficulty = 0;
    public static boolean paused = true;
    public static boolean singleplayer = false;
    public boolean announce = true;
    public static SideList sideList;
    public static Timer timer;

    @Override
    public void onEnable() {

        CoreMain.setPlugin(this);
        timer = new Timer(this, TimerType.Increasing);
        bingoEventhandler.initialize();
        bingoSettings = new BingoSettings(this);
        BingoCommandExecutor bingoCommandExecutor = new BingoCommandExecutor(this);
        TopCommandExecutor topCommandExecutor = new TopCommandExecutor();
        ResetCommandExecutor resetCommandExecutor = new ResetCommandExecutor(this);
        sideList = new SideList(this);

        Objects.requireNonNull(getCommand("Bingo")).setExecutor(bingoCommandExecutor);
        Objects.requireNonNull(getCommand("Top")).setExecutor(topCommandExecutor);
        Objects.requireNonNull(getCommand("reset")).setExecutor(resetCommandExecutor);

        BukkitScheduler scheduler1 = getServer().getScheduler();

        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (!paused) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!BingoList.getBingoList(player).isEmpty()) {
                            CheckInventory.checkInventory(player, announce, singleplayer, getDifficulty());
                            BingoInventory.updateInventory(player);
                        } else {
                            timer.pause();
                            Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + Utils.getDisplayName(player) + " &fhat das Bingo in &a" + Utils.formatTimerTimeTicksThreeDecimal(timer.getTicks()) + "&f beendet!"));
                            Utils.playSoundForAll(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            paused = true;
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
        SettingSwitch announce = (SettingSwitch) bingoSettings.getSettingbyName("Announce Bingo");
        SettingSwitch advancements = (SettingSwitch) bingoSettings.getSettingbyName("Announce Advancements");
        SettingCycle singlePlayerStartTime = (SettingCycle) bingoSettings.singlePlayerSubSettings.getSettingbyName("Start-Zeit");
        this.difficulty = difficulty.getValue();
        this.announce = announce.getSettingValue();
        if (advancements.getSettingValue()) {
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
            CoreMain.showAdvancements = true;
        } else {
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            CoreMain.showAdvancements = false;
        }
        singleplayer = singlePlayer.getSettingValue();
        if (keepInventory.getSettingValue()) {
            Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
        }
        if (singleplayer) {
            timer.setTimerType(TimerType.Decreasing);
            timer.setSeconds(singlePlayerStartTime.getValue());
            timer.setSingle(true);
        }

        BingoList.populatePlayerBingoList(difficulty.getValue(), items.getValue());
        bingo.Utils.preparePlayers();
        sideList.init();
        paused = false;
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

}
