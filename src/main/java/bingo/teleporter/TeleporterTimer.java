package bingo.teleporter;

import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.plugin.Plugin;

public class TeleporterTimer extends Timer {
    public TeleporterTimer(Plugin plugin, TimerType timerType, String runningString, String pausedString, boolean hidden) {
        super(plugin, timerType, runningString, pausedString, hidden);
    }
}
