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

    private static Plugin plugin;
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
                Scoreboard scoreboard = new Scoreboard(ScoreboardType.MULTI_TITLE, new ArrayList<String>() {{
                    add(core.Utils.colorize(core.Utils.getRainbowString("Bingo") + "&f - by PMFRTT"));
                    add(core.Utils.colorize("Gefundene Items: &6" + BingoList.playerCollectedList.get(player.getDisplayName()).size() + "&7/&a" + core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items")));
                }}, 0);
                scoreboard.addScore(new Score("", 2));
                ScoreboardDisplay scoreboardDisplay = new ScoreboardDisplay(plugin, player);
                scoreboardDisplay.enableTitlesList(scoreboard, 100);
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
                    Score score = new Score(core.Utils.colorize("&a" + Utils.formatMaterialName(material)), -1);
                    score.setSuffix("&7 gefunden!");
                    scoreboard.addScore(score);
                } else if (player.getInventory().contains(material)) {
                    removeScore(player, material);
                    Objects.requireNonNull(SummarizerCore.getSummarization(player)).collectedItem(material);
                    Score score = new Score(core.Utils.colorize("&b" + Utils.formatMaterialName(material)), 0);
                    score.setPrefix("&7Sperre ");
                    score.setSuffix("&7!");
                    scoreboard.addScore(score);
                } else {
                    removeScore(player, material);
                    Score score = new Score(core.Utils.colorize("&c" + Utils.formatMaterialName(material)), 1);
                    score.setPrefix("&7Finde ");
                    score.setSuffix("&7!");
                    scoreboard.addScore(score);
                }

                scoreboard.getTitles().set(1, core.Utils.colorize("Gefundene Items: &6" + BingoList.playerCollectedList.get(player.getDisplayName()).size() + "&7/&a" + core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items")));
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

        Scoreboard scoreboard = getScoreboard(player);

        scoreboard.removeScoreByName(core.Utils.colorize("&a" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&b" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&c" + Utils.formatMaterialName(material)));
    }

    private static void startRender(Player player) {

        Scoreboard scoreboard = getScoreboard(player);
        ScoreboardDisplay display = getScoreboardDisplay(player);

        if (scoreboard.getType().equals(ScoreboardType.STATIC_TITLE) || scoreboard.getType().equals(ScoreboardType.MULTI_TITLE)) {
            display.renderScoreboard(scoreboard, scoreboard.getTitles().get(display.getTitleIndex()));
            DebugSender.sendDebug(DebugType.GUI, "rendered sidelist", "Sidelist");
        }else{
            System.err.println("could not start render of scoreboard because the ScoreBoardType is not STATIC_TITLE (bingo.SideList:126)");
        }
    }

    public static void removePlayer(Player player) {
        playerScoreboards.remove(player.getDisplayName());
        playerScoreboardsDisplay.remove(player.getDisplayName());
    }

    private static Scoreboard getScoreboard(Player player){
        return playerScoreboards.get(player.getDisplayName());
    }

    private static ScoreboardDisplay getScoreboardDisplay(Player player){
        return playerScoreboardsDisplay.get(player.getDisplayName());
    }
}