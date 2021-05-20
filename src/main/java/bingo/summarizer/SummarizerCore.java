package bingo.summarizer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class SummarizerCore {

    private static HashMap<String, Summarization> summarizationOffline = new HashMap<String, Summarization>();

    public static void init() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            summarizationOffline.put(player.getDisplayName(), new Summarization(player));
        }
    }

    public static Summarization getSummarization(Player player) {
        return summarizationOffline.get(player.getDisplayName());
    }

}
