package bingo.teleporter;

import bingo.BingoPlugin;
import core.Utils;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Teleporter {

    private static boolean enabled;
    private static int time;
    private static int radius;
    private static BingoPlugin bingoPlugin;


    private static HashMap<String, TeleporterTimer> playerCountdown = new HashMap<String, TeleporterTimer>();

    public Teleporter(BingoPlugin bingoPlugin, boolean enabled, int time, int radius) {
        Teleporter.enabled = enabled;
        Teleporter.time = time;
        Teleporter.radius = radius;
        Teleporter.bingoPlugin = bingoPlugin;
    }

    public void init() {
        if (enabled) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                TeleporterTimer teleporterTimer = new TeleporterTimer(bingoPlugin, TimerType.DECREASING, "", "", true, Utils.colorize("Du kannst dich wieder &bteleportieren&f!"), player);
                teleporterTimer.setSeconds(time);
                teleporterTimer.resume();
                playerCountdown.put(player.getDisplayName(), teleporterTimer);
            }
        }
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public static boolean canTP(Player player){
        return playerCountdown.get(player.getDisplayName()).getTicks() == 0;
    }

    public static void teleport(Player player){
        TeleporterTimer teleporterTimer = playerCountdown.get(player.getDisplayName());
        teleporterTimer.setSeconds(time);
        teleporterTimer.resume();
        DebugSender.sendDebug(DebugType.PLUGIN, "player has been teleported", "Bingo");
        playerCountdown.put(player.getDisplayName(), teleporterTimer);
        bingo.Utils.scatterPlayer(player, radius, false);
    }

    public static TeleporterTimer getTimer(Player player){
        return playerCountdown.get(player.getDisplayName());
    }
}
