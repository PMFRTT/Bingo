package bingo.hotbar;

import bingo.BingoPlugin;
import core.hotbar.HotbarManager;
import core.hotbar.HotbarScheduler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class BingoHotbarManager extends HotbarManager {

    private final Plugin plugin;

    public BingoHotbarManager(Plugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public void init(){
        for(Player player : Bukkit.getOnlinePlayers()){
            this.createHotbarScheduler(player, new HotbarScheduler(plugin, BingoPlugin.getTimer(), player));
            getHotbarScheduler(player).startScheduler(true);
        }
    }

}
