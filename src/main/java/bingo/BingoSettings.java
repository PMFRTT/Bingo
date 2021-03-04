package bingo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BingoSettings {

	public static ItemStack easy = new ItemStack(Material.LIME_DYE, 1);
	public static ItemMeta easyMeta = easy.getItemMeta();

	public static ItemStack medium = new ItemStack(Material.ORANGE_DYE, 1);
	public static ItemMeta mediumMeta = medium.getItemMeta();

	public static ItemStack hard = new ItemStack(Material.RED_DYE, 1);
	public static ItemMeta hardMeta = hard.getItemMeta();
	
	public static ItemStack keepInventory = new ItemStack(Material.GRAY_DYE);
	public static ItemMeta keepInventoryMeta = keepInventory.getItemMeta();
	
	public static ItemStack enableTimber = new ItemStack(Material.GRAY_DYE);
	public static ItemMeta enableTimberMeta = enableTimber.getItemMeta();
	
	public static ItemStack disableMobs = new ItemStack(Material.LIME_DYE);
	public static ItemMeta disableMobsMeta = disableMobs.getItemMeta();
	
	public static ItemStack startBingo = new ItemStack(Material.GREEN_DYE, 1);
	public static ItemMeta startBingoMeta = startBingo.getItemMeta();


	public static Inventory Settings = Bukkit.createInventory(null, 27, "Einstellungen");

	public static Inventory Settings() {

		easyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		easyMeta.setDisplayName(ChatColor.GREEN + "Einfach");
		easy.setItemMeta(easyMeta);

		Settings.setItem(3, easy);

		mediumMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		mediumMeta.setDisplayName(ChatColor.GOLD + "Mittel");
		medium.setItemMeta(mediumMeta);

		Settings.setItem(4, medium);

		hardMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		hardMeta.setDisplayName(ChatColor.RED + "Schwer");
		hard.setItemMeta(hardMeta);

		Settings.setItem(5, hard);
		
		keepInventoryMeta.setDisplayName(ChatColor.WHITE + "Keep Inventory");
		keepInventory.setItemMeta(keepInventoryMeta);
		Settings.setItem(9, keepInventory);
		
		enableTimberMeta.setDisplayName(ChatColor.WHITE + "Timber");
		enableTimber.setItemMeta(enableTimberMeta);
		Settings.setItem(10, enableTimber);
		
		disableMobsMeta.setDisplayName(ChatColor.GREEN + "Monster");
		disableMobs.setItemMeta(disableMobsMeta);
		Settings.setItem(11, disableMobs);
		
		startBingoMeta.setDisplayName(ChatColor.DARK_GREEN + "Start");
		startBingo.setItemMeta(startBingoMeta);
		Settings.setItem(26, startBingo);

		return Settings;
	}

	public static void update() {

		easy.setItemMeta(easyMeta);

		Settings.setItem(3, easy);

		medium.setItemMeta(mediumMeta);

		Settings.setItem(4, medium);

		hard.setItemMeta(hardMeta);

		Settings.setItem(5, hard);
		
		keepInventory.setItemMeta(keepInventoryMeta);
		
		Settings.setItem(9, keepInventory);
		
		enableTimber.setItemMeta(enableTimberMeta);
		
		Settings.setItem(10, enableTimber);
		
		disableMobs.setItemMeta(disableMobsMeta);
		
		Settings.setItem(11, disableMobs);
	}

}
