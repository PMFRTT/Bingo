package bingo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class BingoList {

    public static ArrayList<Material> bingoList;
    public static HashMap<String, ArrayList<Material>> playerBingoLists = new HashMap<String, ArrayList<Material>>();


    public static Material[] easyMaterials = {Material.IRON_INGOT, Material.SANDSTONE, Material.GOLD_INGOT,
            Material.COAL_BLOCK, Material.PAPER, Material.GLASS, Material.COOKED_BEEF, Material.BRICKS,
            Material.STONE_BRICKS, Material.RED_WOOL, Material.STONE_HOE, Material.LEATHER_BOOTS, Material.GLASS_BOTTLE,
            Material.TRIPWIRE_HOOK, Material.ARROW, Material.IRON_AXE, Material.BUCKET, Material.BOWL, Material.SHIELD,
            Material.DANDELION, Material.CRAFTING_TABLE, Material.FURNACE, Material.BLACK_DYE, Material.BOW,
            Material.ITEM_FRAME, Material.TALL_GRASS, Material.BOOK, Material.RAIL
    };

    public static Material[] mediumMaterials = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND,
            Material.IRON_CHESTPLATE, Material.GOLDEN_APPLE, Material.BOOKSHELF, Material.EMERALD, Material.ITEM_FRAME,
            Material.SHEARS, Material.DIAMOND_PICKAXE, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.IRON_SWORD,
            Material.REPEATER, Material.LIGHT_BLUE_BED, Material.OAK_SIGN, Material.CARVED_PUMPKIN, Material.FLOWER_POT,
            Material.DISPENSER, Material.IRON_BLOCK, Material.IRON_BARS, Material.CAKE, Material.ANVIL, Material.CAMPFIRE,
            Material.NETHERRACK, Material.COMPASS, Material.CLOCK, Material.BAKED_POTATO, Material.CAULDRON, Material.WARPED_DOOR,
            Material.WARPED_PLANKS, Material.CRIMSON_DOOR, Material.CRIMSON_STEM, Material.TNT, Material.REDSTONE_LAMP, Material.PISTON,
            Material.LECTERN, Material.POWERED_RAIL, Material.SOUL_TORCH, Material.VINE
    };

    public static Material[] hardMaterials = {Material.ENCHANTING_TABLE, Material.BLAZE_ROD, Material.CAKE,
            Material.DIAMOND_BLOCK, Material.REDSTONE_LAMP, Material.HOPPER, Material.OBSERVER, Material.PUFFERFISH,
            Material.GOLDEN_APPLE, Material.DIAMOND_BLOCK, Material.DIAMOND_CHESTPLATE, Material.ENDER_PEARL,
            Material.EMERALD_BLOCK, Material.LIME_BED, Material.MAGMA_BLOCK, Material.SEA_LANTERN, Material.JACK_O_LANTERN,
            Material.GLOWSTONE, Material.QUARTZ_BLOCK, Material.ACACIA_LOG, Material.NETHERITE_INGOT, Material.RABBIT_STEW,
            Material.LEAD, Material.GLISTERING_MELON_SLICE, Material.COAL_ORE, Material.BROWN_STAINED_GLASS, Material.ENDER_CHEST,
            Material.STICKY_PISTON, Material.ACTIVATOR_RAIL, Material.SLIME_BLOCK

    };

    public static ArrayList<Material> scrambleList(Material[] listMat, int size) {
        ArrayList<Material> bingoList = new ArrayList<Material>();
        Random random = new Random();

        while (bingoList.size() < size) {
            int randomInt = random.nextInt(listMat.length);
            if (!bingoList.contains(listMat[randomInt])) {
                bingoList.add(listMat[randomInt]);
            }
        }
        return bingoList;
    }

    public static void generateBingoList(int difficulty, int size) {
        switch (difficulty) {
            case 0:
                bingoList = scrambleList(easyMaterials, size);
                break;
            case 1:
                bingoList = scrambleList(mediumMaterials, size);
                break;
            case 2:
                bingoList = scrambleList(hardMaterials, size);
                break;
        }
    }

    public static void populatePlayerBingoList(int difficulty, int size) {
        BingoList.generateBingoList(difficulty, size);
        for (Player player : Bukkit.getOnlinePlayers()) {
            assert getBingoList() != null;
            ArrayList<Material> bingoList = new ArrayList<Material>(getBingoList());
            playerBingoLists.put(player.getDisplayName(), bingoList);
            BingoInventory.createInventory(player, size);
        }
    }

    public static ArrayList<Material> getBingoList() {
        if (!bingoList.isEmpty()) {
            return bingoList;
        } else return null;
    }

    public static ArrayList<Material> getBingoList(Player player) {
        return playerBingoLists.get(player.getDisplayName());
    }

    public static void removeMaterialFromList(Player player, Material material) {
        playerBingoLists.get(player.getDisplayName()).remove(material);
    }
}
