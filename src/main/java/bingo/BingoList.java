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
    private static HashMap<String, ArrayList<Material>> playerBingoLists = new HashMap<String, ArrayList<Material>>();


    public static Material[] easyMaterials = {Material.IRON_INGOT, Material.SANDSTONE, Material.GOLD_INGOT,
            Material.COAL_BLOCK, Material.PAPER, Material.GLASS, Material.COOKED_BEEF, Material.BRICKS,
            Material.STONE_BRICKS, Material.RED_WOOL, Material.STONE_HOE, Material.LEATHER_BOOTS, Material.GLASS_BOTTLE,
            Material.TRIPWIRE_HOOK, Material.ARROW, Material.IRON_AXE, Material.BUCKET, Material.BOWL, Material.SHIELD,
            Material.DANDELION, Material.CRAFTING_TABLE, Material.FURNACE, Material.BLACK_DYE, Material.BOW,
            Material.ITEM_FRAME, Material.TALL_GRASS, Material.BOOK

    };

    public static Material[] mediumMaterials = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND,
            Material.IRON_CHESTPLATE, Material.GOLDEN_APPLE, Material.BOOKSHELF, Material.EMERALD, Material.ITEM_FRAME,
            Material.SHEARS, Material.DIAMOND_PICKAXE, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.IRON_SWORD,
            Material.REPEATER, Material.LIGHT_BLUE_BED, Material.OAK_SIGN, Material.CARVED_PUMPKIN, Material.FLOWER_POT,
            Material.DISPENSER, Material.IRON_BLOCK, Material.IRON_BARS, Material.CAKE, Material.ANVIL,
            Material.CAMPFIRE, Material.NETHERRACK, Material.COMPASS, Material.CLOCK, Material.BAKED_POTATO,

    };

    public static Material[] hardMaterials = {Material.ENCHANTING_TABLE, Material.BLAZE_ROD, Material.CAKE,
            Material.DIAMOND_BLOCK, Material.REDSTONE_LAMP, Material.HOPPER, Material.OBSERVER, Material.PUFFERFISH,
            Material.GOLDEN_APPLE, Material.DIAMOND_BLOCK, Material.DIAMOND_CHESTPLATE, Material.ENDER_PEARL,
            Material.EMERALD_BLOCK, Material.LIME_BED, Material.MAGMA_BLOCK, Material.SEA_LANTERN,
            Material.JACK_O_LANTERN, Material.GLOWSTONE, Material.QUARTZ_BLOCK, Material.ACACIA_LOG

    };

    public static ArrayList<Material> scrambleList(Material[] listMat) {
        ArrayList<Material> bingoList = new ArrayList<Material>();
        Random random = new Random();

        while (bingoList.size() < 9) {
            int randomInt = random.nextInt(listMat.length);
            if (!bingoList.contains(listMat[randomInt])) {
                bingoList.add(listMat[randomInt]);
            }
        }
        return bingoList;
    }

    public static void generateBingoList(int difficulty) {
        switch (difficulty) {
            case 0:
                bingoList = scrambleList(easyMaterials);
                break;
            case 1:
                bingoList = scrambleList(mediumMaterials);
                break;
            case 2:
                bingoList = scrambleList(hardMaterials);
                break;
        }
    }

    public static void populatePlayerBingoList(int difficulty) {
        BingoList.generateBingoList(difficulty);
        for (Player player : Bukkit.getOnlinePlayers()) {
            playerBingoLists.put(player.getDisplayName(), BingoList.getBingoList());
            BingoInventory.createInventory(player);
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
