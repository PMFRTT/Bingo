package bingo.eventhandler;

import bingo.*;
import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import core.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import core.Utils;

import java.util.Objects;

public class BingoEventhandler implements Listener {

    BingoPlugin main;

    public BingoEventhandler(BingoPlugin main) {
        this.main = main;
    }

    public void initialize() {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onPlayerPickUp(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 5L);
        }
    }

    @EventHandler
    public void onBlockBroken(BlockBreakEvent e) {
        if (BingoPlugin.getTimer().isPaused()) {
            CoreMain.hotbarManager.getHotbarScheduler(e.getPlayer()).scheduleMessage(Utils.colorize("&cDu kannst die Welt nicht verändern solange das Bingo pausiert ist!"), 40);
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (BingoPlugin.getTimer().isPaused()) {
            if (e.getDamager() instanceof Player) {
                CoreMain.hotbarManager.getHotbarScheduler((Player) e.getDamager()).scheduleMessage(Utils.colorize("&cDu kannst die Welt nicht verändern solange das Bingo pausiert ist!"), 40);
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 5L);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 1L);
        Player player = (Player) e.getWhoClicked();

        if (BingoInventory.getAllInventories().containsValue(e.getInventory())) {
            if (e.getClick().isShiftClick()) {
                if (e.getClickedInventory() == player.getInventory()) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 1L);
                    e.setCancelled(true);
                }
            }
            if (CheckInventory.getLockedSlots((Player) e.getWhoClicked()).contains(e.getSlot())) {
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    if (e.getClick().isShiftClick()) {
                        e.getWhoClicked().getInventory().addItem(new ItemStack(Objects.requireNonNull(e.getCurrentItem()).getType(), 1));
                        BingoList.removeMaterialFromCollected((Player) e.getWhoClicked(), e.getCurrentItem().getType());
                        e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
                        CheckInventory.unlockSlot((Player) e.getWhoClicked(), e.getSlot());
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 1L);
                    e.setCancelled(true);
                }
            } else {
                if (Objects.requireNonNull(CheckInventory.getLockedSize(Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items"))).contains(e.getSlot()) || !CheckInventory.playerBans.get(player.getDisplayName()).contains(e.getSlot())) {
                    if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                        if (Objects.requireNonNull(CheckInventory.getLockedSize(Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Items"))).contains(e.getSlot())) {
                            e.setCancelled(true);
                        }
                        if (BingoInventory.bannedItem.get(player.getDisplayName()).size() < Utils.getSettingValueInt(BingoPlugin.getBingoSettings(), "Anzahl der Items")) {
                            if (e.getCurrentItem() != null) {
                                Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 1L);
                                if (!e.getCurrentItem().getType().equals(Material.BARRIER) && !e.getCurrentItem().getType().equals(Material.AIR)) {
                                    player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du hast &c" + bingo.Utils.formatMaterialName(e.getCurrentItem().getType()) + " &ferfolgreich &cgebannt&f!"));
                                    if (e.getCurrentItem().getType() != Material.BARRIER) {
                                        Banner.banItem((Player) e.getWhoClicked(), e.getCurrentItem().getType());
                                    }
                                } else {
                                    player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Dieses Item hast du bereits &cgebannt&f!"));
                                }
                                e.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onInvClick(InventoryClickEvent e) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
            @Override
            public void run() {
                SideList.updateScoreboard();
            }
        }, 5L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!BingoPlugin.getTimer().isPaused()) {
            BingoPlugin.sideList.createPlayerScoreBoards();
            SideList.updateScoreboard();
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        SideList.removePlayer(player);
    }
}
