package bingo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public static void createInventory(Player player, int size) {
        Inventory inventory = Bukkit.createInventory(null, size * 2, "Bingo-Tafel von " + Utils.getDisplayName(player));
        int i = 0;
        for (Material material : BingoList.getBingoList(player)) {
            inventory.setItem(i, new ItemStack(material));
            if(i == 8){
                i = 18;
            }else if(i == 26){
                i = 36;
            } else{
                i++;
            }
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
            if(i == 8){
                i = 18;
            }else if(i == 26){
                i = 36;
            } else{
                i++;
            }
        }
    }

    public static ItemStack convertToLocked(Material material) {
        ItemStack itemStack = new ItemStack(material, 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(Utils.colorize("&a" + bingo.Utils.formatMaterialName(material)));
        itemMeta.hasEnchants();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
        List<String> lore = new ArrayList<String>(){{
            add(Utils.colorize("&fDu hast dieses Item"));
            add(Utils.colorize("&fbereits &agefunden"));
            add(Utils.colorize("&fDas Item bleibt in"));
            add(Utils.colorize("&fdiesem Slot &cgesperrt!"));
            add(Utils.colorize("&7Shiftclick um das Item zu erhalten!"));
        }};
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
