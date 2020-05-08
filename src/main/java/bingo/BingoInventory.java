package bingo;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BingoInventory {

	public static Collection<? extends Player> players = Bukkit.getOnlinePlayers();

	static ItemStack done = new ItemStack(Material.TURTLE_HELMET, 1);
	static ItemMeta doneMeta = done.getItemMeta();

	public static HashMap<Player, Inventory> createInv(Player p) {

		System.out.print("Inv for " + p.getDisplayName() + " created");
		InventoryHolder owner = p;
		Inventory inv = Bukkit.createInventory(owner, InventoryType.DISPENSER, "Bingo von " + p.getDisplayName());
		int size = BingoPlugin.map.get(p.getDisplayName()).size();
		doneMeta.setDisplayName(ChatColor.GOLD + "Abgeschlossen!");
		doneMeta.hasEnchants();
		doneMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		doneMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, true);
		done.setItemMeta(doneMeta);
		for (int i = 0; i < size; i++) {
			Material mat = Material.getMaterial(BingoPlugin.map.get(p.getDisplayName()).get(i));
			if (mat != null) {
				inv.setItem(i, new ItemStack(mat, 1));

			}

			else {
				inv.setItem(i, done);
			}

		}

		HashMap<Player, Inventory> map = new HashMap<Player, Inventory>();
		map.put(p, inv);
		return map;
	}


	public static void updateInventory(HashMap<Player, Inventory> map, Player player) {

		Inventory toEdit = map.get(player);
		toEdit.clear();
		;
		int size = BingoPlugin.map.get(player.getDisplayName()).size();
		for (int i = 0; i < 9; i++) {
			if (size > i) {
				Material mat = Material.getMaterial(BingoPlugin.map.get(player.getDisplayName()).get(i));
				if (mat != null) {
					toEdit.setItem(i, new ItemStack(mat, 1));
				}
			} else {
				toEdit.setItem(i, done);
			}
		}

		map.put(player, toEdit);

	}
}
