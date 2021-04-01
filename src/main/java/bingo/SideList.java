package bingo;

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
import java.util.Objects;


public class SideList {

    Plugin plugin;
    private static HashMap<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();
    private static HashMap<String, ScoreboardDisplay> playerScoreboardsDisplay = new HashMap<String, ScoreboardDisplay>();

    public SideList(Plugin plugin) {
        this.plugin = plugin;
    }

    public void init() {
        createPlayerScoreBoards();
        startRender();
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
            }
        }
        updateScoreboard();
    }

    public static void updateScoreboard() {
        for (String name : playerScoreboards.keySet()) {
            System.out.println("updating");
            Player player = Bukkit.getPlayer(name);
            Scoreboard scoreboard = playerScoreboards.get(name);
            for (Material material : BingoList.getBingoList(Objects.requireNonNull(player))) {
                if (player.getInventory().contains(material)) {
                    removeScore(player, material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&b" + Utils.formatMaterialName(material)), 0));
                } else if (BingoList.playerCollectedList.get(name).contains(material)) {
                    removeScore(player, material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&a" + Utils.formatMaterialName(material)), 0));
                } else {
                    removeScore(player, material);
                    scoreboard.addScore(new Score(core.Utils.colorize("&c" + Utils.formatMaterialName(material)), 0));
                }
            }
        }
        startRender();
    }

    private static void removeScore(Player player, Material material) {
        Scoreboard scoreboard = playerScoreboards.get(player.getDisplayName());
        scoreboard.removeScoreByName(core.Utils.colorize("&a" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&b" + Utils.formatMaterialName(material)));
        scoreboard.removeScoreByName(core.Utils.colorize("&c" + Utils.formatMaterialName(material)));
    }

    private static void startRender() {
        for (String name : playerScoreboards.keySet()) {
            playerScoreboardsDisplay.get(name).renderScoreboard(playerScoreboards.get(name));
        }
    }

}