package bingo;

import bingo.commandExecutor.BingoCommandExecutor;
import bingo.commandExecutor.ResetCommandExecutor;
import bingo.commandExecutor.TopCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.eventhandler.CheckInventory;
import core.core.CoreMain;
import core.Utils;
import core.core.CoreSendStringPacket;
import core.settings.SettingCycle;
import core.settings.SettingSwitch;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private final BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
    public BingoSettings bingoSettings;

    public static int seconds = 0;

    public int difficulty = 0;
    public static boolean paused = true;
    public static boolean singleplayer = false;
    public boolean announce = true;

    @Override
    public void onEnable() {

        CoreMain.setPlugin(this);
        bingoEventhandler.initialize();
        bingoSettings = new BingoSettings(this);
        BingoCommandExecutor bingoCommandExecutor = new BingoCommandExecutor(this);
        TopCommandExecutor topCommandExecutor = new TopCommandExecutor();
        ResetCommandExecutor resetCommandExecutor = new ResetCommandExecutor(this);

        Objects.requireNonNull(getCommand("Bingo")).setExecutor(bingoCommandExecutor);
        Objects.requireNonNull(getCommand("Top")).setExecutor(topCommandExecutor);
        Objects.requireNonNull(getCommand("reset")).setExecutor(resetCommandExecutor);

        BukkitScheduler scheduler1 = getServer().getScheduler();
        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (singleplayer) {
                    if(!paused){
                        seconds--;
                    }
                    if(seconds <= 0){
                        paused = true;
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String msg;
                        if (!paused) {
                            msg = Utils.colorize("Du hast noch &b" + Utils.formatTimerTime(seconds) + "&f Zeit");
                        } else {
                            msg = Utils.colorize("&cDas Bingo ist pausiert!");
                        }
                        CoreSendStringPacket.sendPacketToHotbar(player, msg);

                    }
                } else {

                    if (!paused) {
                        seconds++;
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        String msg;
                        if (!paused) {
                            msg = Utils.colorize("Das Bingo läuft seit &b" + Utils.formatTimerTime(seconds));
                        } else {
                            msg = Utils.colorize("&cDas Bingo ist pausiert!");
                        }
                        CoreSendStringPacket.sendPacketToHotbar(player, msg);

                    }

                }
            }
        }, 0L, 20);

        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (!paused) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        ScoreBoard.test(player, BingoList.getBingoList(player));
                        if (!BingoList.getBingoList(player).isEmpty()) {
                            CheckInventory.checkInventory(player, announce, singleplayer, getDifficulty());
                            BingoInventory.updateInventory(player);
                        } else {
                            Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + Utils.getDisplayName(player) + " &fhat das Bingo in &a" + Utils.formatTimerTime(seconds) + "&f beendet!"));
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
        if(advancements.getSettingValue()){
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, true);
        }else {
            Utils.changeGamerule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        }
        singleplayer = singlePlayer.getSettingValue();
        if (keepInventory.getSettingValue()) {
            Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
        }
        if(singleplayer){
            seconds = singlePlayerStartTime.getValue();
        }

        BingoList.populatePlayerBingoList(difficulty.getValue(), items.getValue());
        bingo.Utils.preparePlayers();
        paused = false;
    }

    public void onDisable() {
    }

    public BingoSettings getBingoSettings() {
        return bingoSettings;
    }

    public int getDifficulty(){
        return this.difficulty;
    }

}
