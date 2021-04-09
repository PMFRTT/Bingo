package bingo.teleporter;

import bingo.BingoPlugin;
import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class Respawner implements Listener {

    private final BingoPlugin plugin;

    private static HashMap<String, Location> deathMap = new HashMap<String, Location>();

    public Respawner(BingoPlugin plugin) {
        this.plugin = plugin;
        initialize();
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        deathMap.put(e.getEntity().getDisplayName(), e.getEntity().getLocation());
    }

    public static void respawn(Player player) {
        if (deathMap.containsKey(player.getDisplayName())) {
            World world = player.getWorld();
            if (deathMap.get(player.getDisplayName()).getBlock().isLiquid()) {
                world.getBlockAt(deathMap.get(player.getDisplayName()).add(0, 3, 0)).setType(Material.GLASS);
                player.teleport(deathMap.get(player.getDisplayName()).add(0, 5, 0));
                player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du wurdest an die Stelle deines letzten Todes teleportiert!"));
            }else{
                world.getBlockAt(deathMap.get(player.getDisplayName()).add(0, 0, 0)).setType(Material.GLASS);
                player.teleport(deathMap.get(player.getDisplayName()).add(0, 2, 0));
                player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du wurdest an die Stelle deines letzten Todes teleportiert!"));
            }
        }
    }
}
