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
    private HashMap<String, Scoreboard> playerScoreboards = new HashMap<String, Scoreboard>();
    private HashMap<String, ScoreboardDisplay> playerScoreboardsDisplay = new HashMap<String, ScoreboardDisplay>();

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

    public void updateScoreboard() {
        for (String name : playerScoreboards.keySet()) {
            Scoreboard scoreboard = playerScoreboards.get(name);
            for (Material material : BingoList.getBingoList(Objects.requireNonNull(Bukkit.getPlayer(name)))) {
                scoreboard.addScore(new Score(core.Utils.colorize("&e" + Utils.formatMaterialName(material)), 0));
            }
        }
        startRender();
    }

    public void removeScore(Player player, Material material) {
        Scoreboard scoreboard = playerScoreboards.get(player.getDisplayName());
        Score score = scoreboard.getScoreByName(core.Utils.colorize("&e" + Utils.formatMaterialName(material)));
        score.setContent(core.Utils.colorize("&a" + Utils.formatMaterialName(material)));
        score.setValue(-1);
        startRender();
    }

    private void startRender() {
        for (String name : playerScoreboards.keySet()) {
            playerScoreboardsDisplay.get(name).renderScoreboard(playerScoreboards.get(name));
        }
    }

}
