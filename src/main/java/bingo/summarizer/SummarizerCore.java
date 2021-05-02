package bingo.summarizer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SummarizerCore {

    private static List<Summarization> summarizations = new ArrayList<Summarization>();

    public static void init() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            summarizations.add(new Summarization(player));
        }
    }

    public static Summarization getSummarization(Player player) {
        for (Summarization summarization : summarizations) {
            if (summarization.getOwner() == player) {
                return summarization;
            }
        }
        return null;
    }

}
