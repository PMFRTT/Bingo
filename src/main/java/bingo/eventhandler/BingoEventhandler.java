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
import org.bukkit.event.entity.EntityDamageEvent;
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
    public void onBlockBroken(BlockBreakEvent e1) {
        if (BingoPlugin.getTimer().isPaused()) {
            if (!e1.getPlayer().isOp()) {
                CoreMain.hotbarManager.getHotbarScheduler(e1.getPlayer()).scheduleMessage(Utils.colorize("&cDu kannst die Welt nicht verändern solange das Bingo pausiert ist!"), 40);
                e1.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e2) {
        if (BingoPlugin.getTimer().isPaused()) {
            if (!e2.getDamager().isOp()) {
                if (e2.getDamager() instanceof Player) {
                    CoreMain.hotbarManager.getHotbarScheduler((Player) e2.getDamager()).scheduleMessage(Utils.colorize("&cDu kannst die Welt nicht verändern solange das Bingo pausiert ist!"), 40);
                }
                e2.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void handleBingoLock(InventoryClickEvent e) {
        if (BingoInventory.getPlayerInventory((Player) e.getWhoClicked()) != null) {
            if (!Banner.getRunning()) {
                if (BingoInventory.getInventories().contains(e.getClickedInventory())) {
                    if (Objects.requireNonNull(CheckInventory.createLockedList(BingoList.getSize())).contains(e.getSlot())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (BingoPlugin.getTimer().isPaused()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void handleBingoUnlock(InventoryClickEvent e) {
        if (BingoInventory.getPlayerInventory((Player) e.getWhoClicked()) != null) {
            if (CheckInventory.getLockedSlots((Player) e.getWhoClicked()).contains(e.getSlot())) {
                if (e.getClickedInventory() != e.getWhoClicked().getInventory()) {
                    if (e.getClick().isShiftClick()) {
                        if (!core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Singleplayer")) {
                            e.getWhoClicked().getInventory().addItem(new ItemStack(Objects.requireNonNull(e.getCurrentItem()).getType(), 1));
                            BingoList.removeMaterialFromCollected((Player) e.getWhoClicked(), e.getCurrentItem().getType());
                            e.getInventory().setItem(e.getRawSlot(), new ItemStack(Material.AIR));
                            CheckInventory.unlockSlot((Player) e.getWhoClicked(), e.getSlot());
                        }
                    }
                    Bukkit.getScheduler().scheduleSyncDelayedTask(main, SideList::updateScoreboard, 2L);
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void handleBingoBans(InventoryClickEvent e) {
        if (BingoInventory.getPlayerInventory((Player) e.getWhoClicked()) != null) {
            if (Banner.getRunning()) {
                Player player = (Player) e.getWhoClicked();
                if (CheckInventory.createLockedList(BingoList.bingoList.size()).contains(e.getSlot()) || !CheckInventory.playerBans.get(player.getDisplayName()).contains(e.getSlot())) {
                    if (BingoInventory.getInventories().contains(e.getClickedInventory())) {
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
    public void onPlayerJoin(PlayerJoinEvent e) {
        CoreMain.hotbarManager.getHotbarScheduler(e.getPlayer()).setTimer(BingoPlugin.getTimer());
        CoreMain.hotbarManager.getHotbarScheduler(e.getPlayer()).startScheduler(true);
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
