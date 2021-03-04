package bingo;

import bingo.commandExecutor.BingoCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.eventhandler.CheckInventory;
import core.core.CoreMain;
import core.Utils;
import core.core.CoreSendStringPacket;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private final BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
    public final GameSettings settings = new GameSettings("Bingo");


    public static int seconds = 0;

    public static boolean unlockedInv = false;
    public static boolean isPaused = false;
    public static boolean cancelFallDamage = false;
    public static boolean keepInventory = false;
    public static boolean timber = false;
    public static boolean enableMobs = true;
    public static boolean hasEnded = false;
    public static boolean hasStarted = false;

    public static String difficulty = "";

    @Override
    public void onEnable() {

        CoreMain.setPlugin(this);
        bingoEventhandler.initialize();

        BingoCommandExecutor commandExecutor = new BingoCommandExecutor(this);

        Objects.requireNonNull(getCommand("Bingo")).setExecutor(commandExecutor);
        //Objects.requireNonNull(getCommand("Top")).setExecutor(commandExecutor);

        BukkitScheduler scheduler1 = getServer().getScheduler();
        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                for (Player player : Bukkit.getOnlinePlayers()) {
                    String msg;
                    if (hasStarted && !isPaused) {
                        seconds++;
                        msg = Utils.colorize("Das Bingo läuft seit &b" + Utils.formatTimerTime(seconds));
                    } else {
                        msg = Utils.colorize("&cDas Bingo ist pausiert!");
                    }
                    CoreSendStringPacket.sendPacketToHotbar(player, msg);

                }

            }
        }, 0L, 20);

        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (hasStarted) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (!BingoList.getBingoList(player).isEmpty()) {
                            CheckInventory.checkInventory(player, true);
                            BingoInventory.updateInventory(player);
                        }else{
                            Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + Utils.getDisplayName(player) + " &fhat das Bingo in &a" + Utils.formatTimerTime(seconds) + "&f beendet!"));
                            Utils.playSoundForAll(Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                            hasStarted = false;
                        }
                    }
                }
            }
        }, 0L, 1);
    }

    public void startBingo(int difficulty) {
        BingoList.populatePlayerBingoList(difficulty);
        bingo.Utils.preparePlayers();
        hasStarted = true;
    }

    public void onDisable() {
    }

}
