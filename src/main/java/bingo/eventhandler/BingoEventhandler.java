package bingo.eventhandler;

import bingo.BingoInventory;
import bingo.BingoList;
import bingo.BingoPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;


public class BingoEventhandler implements Listener {

    BingoPlugin bingo;

    public BingoEventhandler(BingoPlugin bingo) {
        this.bingo = bingo;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (BingoInventory.getAllInventories().containsValue(e.getInventory())) {
            if(e.getClick().isShiftClick()){
                if(e.getClickedInventory() == e.getWhoClicked().getInventory()){
                    e.setCancelled(true);
                }
            }
            if (CheckInventory.getLockedSlots((Player) e.getWhoClicked()).contains(e.getSlot())) {
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    if (e.getClick().isShiftClick()) {
                        e.getWhoClicked().getInventory().addItem(new ItemStack(e.getCurrentItem().getType(), 1));
                        BingoList.removeMaterialFromCollected((Player) e.getWhoClicked(), e.getCurrentItem().getType());
                        e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
                        CheckInventory.unlockSlot((Player) e.getWhoClicked(), e.getSlot());
                    }
                    e.setCancelled(true);
                }
            } else if (CheckInventory.getLockedSize(BingoPlugin.items).contains(e.getSlot())) {
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!BingoPlugin.getTimer().isPaused()) {
            BingoPlugin.sideList.createPlayerScoreBoards();
        }
    }
}
