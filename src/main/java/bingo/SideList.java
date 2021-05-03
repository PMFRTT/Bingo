package bingo;

import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import bingo.summarizer.SummarizerCore;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.scoreboard.Score;
import core.scoreboard.Scoreboard;
import core.scoreboard.ScoreboardDisplay;
import core.scoreboard.ScoreboardType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class SideList {

    Plugin plugin;
    private static final HashMap<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();
    private static final HashMap<String, ScoreboardDisplay> playerScoreboardsDisplay = new HashMap<String, ScoreboardDisplay>();

    public SideList(Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        createPlayerScoreBoards();
        for (Player player : Bukkit.getOnlinePlayers()) {
            startRender(player);
        }
    }

    public void createPlayerScoreBoards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!playerScoreboards.containsKey(player.getDisplayName())) {
                Scoreboard scoreboard = new Scoreboard(ScoreboardType.STATIC_TITLE, new ArrayList<String>() {{
                    add("Bingo");
                }}, 0);
                ScoreboardDisplay scoreboardDisplay = new ScoreboardDisplay(this.plugin, player);
                playerScoreboards.put(core.Utils.getDisplayName(player), scoreboard);
                playerScoreboardsDisplay.put(core.Utils.getDisplayName(player), scoreboardDisplay);
            } else {
                playerScoreboards.remove(player.getDisplayName());
                createPlayerScoreBoards();
            }
        }
        updateScoreboard();
    }

    public static void updateScoreboard() {
        for (String name : playerScoreboards.keySet()) {
            Player player = Bukkit.getPlayer(name);
            Scoreboard scoreboard = playerScoreboards.get(name);
            List<String> scores = new ArrayList<String>();
            for (Score score : scoreboard.getScores()) {
                scores.add(score.getContent());
            }
            for (Material material : BingoList.getBingoList(Objects.requireNonNull(player))) {
                if (BingoList.playerCollectedList.get(name).contains(material)) {
                    removeScore(player, material);
                    Objects.requireNonNull(SummarizerCore.getSummarization(player)).lockedItem(material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&a" + Utils.formatMaterialName(material)), -1));
                } else if (player.getInventory().contains(material)) {
                    removeScore(player, material);
                    Objects.requireNonNull(SummarizerCore.getSummarization(player)).collectedItem(material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&b" + Utils.formatMaterialName(material)), 0));
                } else {
                    removeScore(player, material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&c" + Utils.formatMaterialName(material)), 1));
                }
                BingoInventory.updateInventory(player);
            }
            int i = 0;
            for (Score score : scoreboard.getScores()) {
                if (scores.contains(score.getContent())) {
                    i++;
                }
            }
            if (i != core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items")) {
                startRender(player);
            }
        }
    }

    private static void removeScore(Player player, Material material) {
        Scoreboard scoreboard = playerScoreboards.get(player.getDisplayName());
        scoreboard.removeScoreByName(core.Utils.colorize("&a" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&b" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&c" + Utils.formatMaterialName(material)));
    }

    private static void startRender(Player player) {
        playerScoreboardsDisplay.get(player.getDisplayName()).renderScoreboard(playerScoreboards.get(player.getDisplayName()));
        DebugSender.sendDebug(DebugType.GUI, "rendered sidelist", "Sidelist");
    }

    public static void removePlayer(Player player) {
        playerScoreboards.remove(player.getDisplayName());
        playerScoreboardsDisplay.remove(player.getDisplayName());
    }

}
