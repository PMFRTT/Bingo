package bingo.eventhandler;

import bingo.BingoInventory;
import bingo.BingoList;
import bingo.BingoPlugin;
import bingo.SideList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

public class BingoEventhandler implements Listener {

    BingoPlugin bingo;

    public BingoEventhandler(BingoPlugin bingo) {
        this.bingo = bingo;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, bingo);
    }

    @EventHandler
    public void onPlayerPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(bingo, SideList::updateScoreboard, 1L);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(bingo, SideList::updateScoreboard, 1L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        SideList.updateScoreboard();
        if (BingoInventory.getAllInventories().containsValue(e.getInventory())) {
            if (e.getClick().isShiftClick()) {
                if (e.getClickedInventory() == e.getWhoClicked().getInventory()) {
                    SideList.updateScoreboard();
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
                    SideList.updateScoreboard();
                    e.setCancelled(true);
                }
            } else if (CheckInventory.getLockedSize(BingoPlugin.items).contains(e.getSlot())) {
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    SideList.updateScoreboard();
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!BingoPlugin.getTimer().isPaused()) {
            BingoPlugin.sideList.createPlayerScoreBoards();
            SideList.updateScoreboard();
        }
    }
}
