package bingo;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ScoreBoard {

    private static final HashMap<Player, Scoreboard> playerScoreboardHashMap = new HashMap<Player, Scoreboard>();

    public static void test(Player player, List<Material> bingoList) {
        if (!playerScoreboardHashMap.containsKey(player)) {
            Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
            Objective objective = scoreboard.registerNewObjective("Bingo", "dummy", "test");
            int i = 1;
            for (Material material : bingoList) {
                Score score = objective.getScore(Utils.colorize("&c"+bingo.Utils.formatMaterialName(material)));
                score.setScore(i);
            }
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            player.setScoreboard(scoreboard);
            playerScoreboardHashMap.put(player, scoreboard);
        } else {
            playerScoreboardHashMap.remove(player);
        }
    }


}
