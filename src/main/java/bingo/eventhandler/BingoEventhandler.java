package bingo.eventhandler;

import bingo.BingoInventory;
import bingo.BingoPlugin;
import bingo.BingoSettings;
import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
    public void onInventoryClick(InventoryClickEvent e){
        if(BingoInventory.getAllInventories().containsValue(e.getInventory())){
            e.setCancelled(true);
        }
    }




    /*@EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == BingoSettings.Settings) {

            if (e.getSlot() == 3) {
                e.setCancelled(true);
                BingoPlugin.difficulty = "easy";
                BingoSettings.easyMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.mediumMeta.hasEnchants()) {
                    BingoSettings.mediumMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.hardMeta.hasEnchants()) {
                    BingoSettings.hardMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 4) {
                e.setCancelled(true);
                BingoPlugin.difficulty = "medium";
                BingoSettings.mediumMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.easyMeta.hasEnchants()) {
                    BingoSettings.easyMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.hardMeta.hasEnchants()) {
                    BingoSettings.hardMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 5) {
                e.setCancelled(true);
                BingoPlugin.difficulty = "hard";
                BingoSettings.hardMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.easyMeta.hasEnchants()) {
                    BingoSettings.easyMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.mediumMeta.hasEnchants()) {
                    BingoSettings.mediumMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 9) {
                e.setCancelled(true);
                if (BingoPlugin.keepInventory == false) {
                   core.Utils.changeGamerule(GameRule.KEEP_INVENTORY, true);
                    BingoSettings.keepInventory = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.keepInventoryMeta.setDisplayName(ChatColor.GREEN + "Keep Inventory");
                    BingoSettings.update();
                    BingoPlugin.keepInventory = true;
                } else {
                    core.Utils.changeGamerule(GameRule.KEEP_INVENTORY, false);
                    BingoSettings.keepInventory = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.keepInventoryMeta.setDisplayName(ChatColor.WHITE + "Keep Inventory");
                    BingoSettings.update();
                    BingoPlugin.keepInventory = false;
                }
            }
            if (e.getSlot() == 10) {
                e.setCancelled(true);
                if (BingoPlugin.timber == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timber");
                    BingoSettings.enableTimber = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.enableTimberMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.GREEN + "Timber");
                    BingoSettings.update();
                    BingoPlugin.timber = true;
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timber");
                    BingoSettings.enableTimber = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.enableTimberMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Timber");
                    BingoSettings.update();
                    BingoPlugin.timber = false;
                }
            }

            if (e.getSlot() == 11) {
                e.setCancelled(true);
                if (BingoPlugin.enableMobs == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning true");
                    BingoSettings.disableMobs = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.disableMobsMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.GREEN + "Monster");
                    BingoSettings.update();
                    BingoPlugin.enableMobs = true;
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");
                    BingoSettings.disableMobs = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.disableMobsMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Monster");
                    BingoSettings.update();
                    BingoPlugin.enableMobs = false;
                }
            }

            if (e.getSlot() == 26) {
                bingo.startBingo(BingoPlugin.difficulty, p);
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();

            }
        }

        if (BingoPlugin.unlockedInv == false) {
            if (BingoPlugin.invmap.containsValue(e.getInventory())) {
                if (e.getCurrentItem() != null) {
                    if (e.getClickedInventory().equals(e.getInventory())) {
                        e.setCancelled(true);
                    }
                }

            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (BingoPlugin.isPaused == true) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntityType().equals(EntityType.PLAYER)) {
            if (e.getCause().toString().equals("FALL")) {
                if (BingoPlugin.cancelFallDamage == true) {
                    e.setCancelled(true);
                }
            }
        }
    }*/
}
