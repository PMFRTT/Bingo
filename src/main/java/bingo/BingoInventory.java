package bingo;

import java.util.HashMap;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BingoInventory {

    private static final HashMap<String, Inventory> playerInventoryHashMap = new HashMap<String, Inventory>();

    private static ItemStack createItemStack() {
        ItemStack itemStack = new ItemStack(Material.TURTLE_HELMET, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(ChatColor.GOLD + "Abgeschlossen!");
        itemMeta.hasEnchants();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static void createInventory(Player player, int size) {
        Inventory inventory = Bukkit.createInventory(null, size, "Bingo-Tafel von " + Utils.getDisplayName(player));
        int i = 0;
        for (Material material : BingoList.getBingoList(player)) {
            inventory.setItem(i, new ItemStack(material));
            i++;
        }
        while (i < inventory.getSize()) {
            inventory.setItem(i, createItemStack());
            i++;
        }
        playerInventoryHashMap.put(player.getDisplayName(), inventory);
    }

    public static Inventory getPlayerInventory(Player player) {
        return playerInventoryHashMap.get(player.getDisplayName());
    }

    public static HashMap<String, Inventory> getAllInventories() {
        return playerInventoryHashMap;
    }

    public static void updateInventory(Player player) {
        Inventory inventory = playerInventoryHashMap.get(player.getDisplayName());
        int i = 0;
        for (Material material : BingoList.getBingoList(player)) {
            inventory.setItem(i, new ItemStack(material));
            i++;
        }
        while (i < inventory.getSize()) {
            inventory.setItem(i, createItemStack());
            i++;
        }
    }
}
