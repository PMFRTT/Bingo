package bingo.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import bingo.SideList;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BingoList {

    public static ArrayList<Material> bingoList = new ArrayList<Material>();
    public static HashMap<String, ArrayList<Material>> playerBingoLists = new HashMap<String, ArrayList<Material>>();
    public static HashMap<String, ArrayList<Material>> playerCollectedList = new HashMap<String, ArrayList<Material>>();

    private static int itemCount;


    public static Material[] easyMaterials = {Material.IRON_INGOT, Material.SANDSTONE, Material.GOLD_INGOT,
            Material.COAL_BLOCK, Material.PAPER, Material.GLASS, Material.COOKED_BEEF, Material.BRICKS,
            Material.STONE_BRICKS, Material.RED_WOOL, Material.STONE_HOE, Material.LEATHER_BOOTS, Material.GLASS_BOTTLE,
            Material.TRIPWIRE_HOOK, Material.ARROW, Material.IRON_AXE, Material.BUCKET, Material.BOWL, Material.SHIELD,
            Material.DANDELION, Material.CRAFTING_TABLE, Material.FURNACE, Material.BLACK_DYE, Material.BOW,
            Material.ITEM_FRAME, Material.BOOK, Material.RAIL
    };

    public static Material[] mediumMaterials = {Material.LAPIS_BLOCK, Material.GOLD_BLOCK, Material.DIAMOND,
            Material.IRON_CHESTPLATE, Material.GOLDEN_APPLE, Material.BOOKSHELF, Material.EMERALD,
            Material.SHEARS, Material.DIAMOND_PICKAXE, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.IRON_SWORD,
            Material.REPEATER, Material.LIGHT_BLUE_BED, Material.OAK_SIGN, Material.CARVED_PUMPKIN, Material.FLOWER_POT,
            Material.DISPENSER, Material.IRON_BLOCK, Material.IRON_BARS, Material.ANVIL, Material.CAMPFIRE,
            Material.NETHERRACK, Material.COMPASS, Material.CLOCK, Material.BAKED_POTATO, Material.CAULDRON, Material.WARPED_DOOR,
            Material.WARPED_PLANKS, Material.CRIMSON_DOOR, Material.CRIMSON_STEM, Material.TNT, Material.REDSTONE_LAMP, Material.PISTON,
            Material.LECTERN, Material.POWERED_RAIL, Material.SOUL_TORCH, Material.VINE, Material.PURPLE_WOOL, Material.GLOWSTONE,
            Material.NETHER_BRICKS, Material.RED_CONCRETE, Material.COBWEB, Material.MILK_BUCKET, Material.ENDER_PEARL,
            Material.FIREWORK_ROCKET, Material.QUARTZ_PILLAR, Material.SWEET_BERRIES, Material.DRIED_KELP_BLOCK, Material.GOLDEN_CHESTPLATE,
            Material.GOLDEN_CARROT, Material.FERMENTED_SPIDER_EYE, Material.HOPPER_MINECART,
            Material.PISTON, Material.NOTE_BLOCK, Material.DAYLIGHT_DETECTOR, Material.TARGET, Material.LILY_PAD, Material.ORANGE_CARPET
    };

    public static Material[] hardMaterials = {Material.ENCHANTING_TABLE, Material.BLAZE_ROD, Material.CAKE,
            Material.DIAMOND_BLOCK, Material.HOPPER, Material.OBSERVER, Material.PUFFERFISH,
            Material.DIAMOND_BLOCK, Material.DIAMOND_CHESTPLATE,
            Material.EMERALD_BLOCK, Material.LIME_BED, Material.MAGMA_BLOCK, Material.SEA_LANTERN, Material.JACK_O_LANTERN,
            Material.QUARTZ_BLOCK, Material.ACACIA_LOG, Material.NETHERITE_INGOT, Material.RABBIT_STEW,
            Material.LEAD, Material.GLISTERING_MELON_SLICE, Material.COAL_ORE, Material.BROWN_STAINED_GLASS, Material.ENDER_CHEST,
            Material.STICKY_PISTON, Material.ACTIVATOR_RAIL, Material.SLIME_BLOCK, Material.JUKEBOX, Material.BAMBOO,
            Material.WEEPING_VINES, Material.TWISTING_VINES, Material.WARPED_ROOTS, Material.CRIMSON_ROOTS, Material.SADDLE,
            Material.DIAMOND_HORSE_ARMOR, Material.HONEY_BOTTLE, Material.POISONOUS_POTATO, Material.NETHERITE_HELMET,
            Material.NAME_TAG, Material.LEAD, Material.CROSSBOW

    };

    private static void checkMultiples(){
        for(Material material : easyMaterials){
            for(Material material1 : mediumMaterials){
                if(material == material1){
                    System.err.println(material + "is in easy and medium");
                }
            }
        }
        for(Material material : mediumMaterials){
            for(Material material1 : hardMaterials){
                if(material == material1){
                    System.err.println(material + "is in hard and medium");
                }
            }
        }
        for(Material material : easyMaterials){
            for(Material material1 : hardMaterials){
                if(material == material1){
                    System.err.println(material + "is in easy and hard");
                }
            }
        }
    }

    public static int contains(Material material){
        for(Material material1 : easyMaterials){
            if(material1 == material){
                return 0;
            }
        }
        for(Material material1 : mediumMaterials){
            if(material1 == material){
                return 1;
            }
        }
        for(Material material1 : hardMaterials){
            if(material1 == material){
                return 2;
            }
        }
        return -1;
    }

    public static int getSize(){
        return bingoList.size();
    }

    private static ArrayList<Material> scrambleList(Material[] listMat, int size) {
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

    private static ArrayList<Material> generateBingoList(int difficulty, int size) {
        switch (difficulty) {
            case 0:
                return scrambleList(easyMaterials, size);
            case 1:
                return scrambleList(mediumMaterials, size);
            case 2:
                return scrambleList(hardMaterials, size);
        }
        return null;
    }

    public static void populatePlayerBingoList(int easyItems, int mediumItems, int hardItems) {
        checkMultiples();
        ArrayList<Material> finalBingoList = new ArrayList<Material>();
        finalBingoList.addAll(generateBingoList(0, easyItems));
        finalBingoList.addAll(generateBingoList(1, mediumItems));
        finalBingoList.addAll(generateBingoList(2, hardItems));
        bingoList = finalBingoList;
        itemCount = finalBingoList.size();
        for (Player player : Bukkit.getOnlinePlayers()) {
            assert getBingoList() != null;
            playerBingoLists.put(player.getDisplayName(), bingoList);
            createCollectionLists(player);
            if(finalBingoList.size() % 9 != 0){
                BingoInventory.createInventory(player, finalBingoList.size() + (9 - finalBingoList.size() % 9));
            }else{
                BingoInventory.createInventory(player, finalBingoList.size());
            }
        }
    }

    private static void createCollectionLists(Player player) {
        playerCollectedList.put(player.getDisplayName(), new ArrayList<Material>());
    }

    public static void addMaterialToCollected(Player player, Material material) {
        if (material != Material.BARRIER) {
            if (!playerCollectedList.get(player.getDisplayName()).contains(material)) {
                playerCollectedList.get(player.getDisplayName()).add(material);
                System.out.println("Locking " + material.name());
                SideList.updateScoreboard();
            }
        }
    }

    public static void removeMaterialFromCollected(Player player, Material material) {
        if (playerCollectedList.get(player.getDisplayName()).contains(material)) {
            playerCollectedList.get(player.getDisplayName()).remove(material);
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

    public static Player hasWon = null;

    public static void completed(Player player) {
        int counter = 0;
        for (Material material : playerBingoLists.get(player.getDisplayName())) {
            if (playerCollectedList.get(player.getDisplayName()).contains(material)) {
                counter++;
            }
        }
        if(counter == itemCount){
            hasWon = player;
        }
    }

}
