package bingo.eventhandler;

import bingo.BingoInventory;
import bingo.BingoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class BingoEventhandler implements Listener {

    BingoPlugin bingo;

    public BingoEventhandler(BingoPlugin bingo) {
        this.bingo = bingo;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){
        if(BingoInventory.getAllInventories().containsValue(e.getInventory())){
            e.setCancelled(true);
        }
    }
}
