package bingo.summarizer;

import bingo.main.BingoPlugin;
import core.timer.Timer;
import core.timer.TimerType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class SummarizerCore {

    private static Plugin plugin;

    private static HashMap<String, Summarization> summarizationOffline = new HashMap<String, Summarization>();
    private static Timer timer;

    public SummarizerCore(Plugin plugin){
        SummarizerCore.plugin = plugin;
        timer = new Timer(plugin, TimerType.INCREASING, "", "", true);
    }

    public static void init() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            summarizationOffline.put(player.getDisplayName(), new Summarization(player));
        }
    }

    public static Summarization getSummarization(Player player) {
        return summarizationOffline.get(player.getDisplayName());
    }

    public static Timer getTimer(){
        return SummarizerCore.timer;
    }

}
