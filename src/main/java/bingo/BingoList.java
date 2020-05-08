package bingo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;

public class BingoList {

	public static ArrayList<Material> finishedListMat = new ArrayList<Material>();
	public static ArrayList<String> finishedListString = new ArrayList<String>();

	public static Material[] BingoEasyMat = { Material.IRON_INGOT, Material.SANDSTONE, Material.GOLD_INGOT,
			Material.COAL_BLOCK, Material.PAPER, Material.GLASS, Material.COOKED_BEEF, Material.BRICKS,
			Material.STONE_BRICKS, Material.RED_WOOL, Material.STONE_HOE, Material.LEATHER_BOOTS, Material.GLASS_BOTTLE,
			Material.TRIPWIRE_HOOK, Material.ARROW, Material.IRON_AXE, Material.BUCKET, Material.BOWL, Material.SHIELD,
			Material.DANDELION, Material.CRAFTING_TABLE, Material.FURNACE, Material.BLACK_DYE, Material.BOW,
			Material.ITEM_FRAME, Material.TALL_GRASS, Material.BOOK

	};

	public static Material[] BingoMediumMat = { Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND,
			Material.IRON_CHESTPLATE, Material.GOLDEN_APPLE, Material.BOOKSHELF, Material.EMERALD, Material.ITEM_FRAME,
			Material.SHEARS, Material.DIAMOND_PICKAXE, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.IRON_SWORD,
			Material.REPEATER, Material.LIGHT_BLUE_BED, Material.OAK_SIGN, Material.CARVED_PUMPKIN, Material.FLOWER_POT,
			Material.DISPENSER, Material.IRON_BLOCK, Material.IRON_BARS, Material.CAKE, Material.ANVIL,
			Material.CAMPFIRE, Material.NETHERRACK, Material.COMPASS, Material.CLOCK, Material.BAKED_POTATO,

	};

	public static Material[] BingoHardMat = { Material.ENCHANTING_TABLE, Material.BLAZE_ROD, Material.CAKE,
			Material.DIAMOND_BLOCK, Material.REDSTONE_LAMP, Material.HOPPER, Material.OBSERVER, Material.PUFFERFISH,
			Material.GOLDEN_APPLE, Material.DIAMOND_BLOCK, Material.DIAMOND_CHESTPLATE, Material.ENDER_PEARL,
			Material.EMERALD_BLOCK, Material.LIME_BED, Material.MAGMA_BLOCK, Material.SEA_LANTERN,
			Material.JACK_O_LANTERN, Material.GLOWSTONE, Material.QUARTZ_BLOCK, Material.ACACIA_LOG

	};

	public static ArrayList<Material> ScrambleList(Material[] listMat) {

		while (finishedListMat.size() < 9) {

			Random random = new Random();
			int randomInt = random.nextInt(listMat.length);

			if (!finishedListMat.contains(listMat[randomInt])) {
				finishedListMat.add(listMat[randomInt]);
				finishedListString.add(listMat[randomInt].toString());
			}
		}

		return finishedListMat;

	}

	public static HashMap<String, ArrayList<String>> fillMaps(String name, HashMap<String, ArrayList<String>> map) {
		ArrayList<String> temp = new ArrayList<String>();
		for (Material mat : finishedListMat) {
			temp.add(mat.toString());
		}
		map.put(name, temp);
		BingoPlugin.PlayersBingo.add(map);
		return map;
	}

	public static void updateMaps(HashMap<String, ArrayList<String>> map, String mat, String playername) {
		ArrayList<String> temp = map.get(playername);
		temp.remove(mat.toString());
		map.replace(playername, temp);
	}

	public static ArrayList<String> createPersonalBingoList(String name, HashMap<String, ArrayList<String>> map) {
		return map.get(name);

	}

}
