package bingo.teleporter;

import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TeleporterTimer extends Timer {
    public TeleporterTimer(Plugin plugin, TimerType timerType, String runningString, String pausedString, boolean hidden, String timerReady, Player player) {
        super(plugin, timerType, runningString, pausedString, hidden, timerReady, player);
    }
}
