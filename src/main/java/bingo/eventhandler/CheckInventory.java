package bingo.eventhandler;

import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.SideList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class CheckInventory {

    private static final int min = 60;
    private static final int max = 180;
    private static int multiplier;

    public static List<Integer> alwaysLocked9 = new ArrayList<Integer>() {{
        addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
    }};
    public static List<Integer> alwaysLocked18 = new ArrayList<Integer>() {{
        addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26));
    }};
    public static List<Integer> alwaysLocked27 = new ArrayList<Integer>() {{
        addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26, 36, 37, 38, 39, 40, 41, 42, 43, 44));
    }};

    public static HashMap<String, List<Integer>> playerBans = new HashMap<String, List<Integer>>();

    private static final HashMap<String, ArrayList<Integer>> lockedSlots = new HashMap<String, ArrayList<Integer>>();

    public static void checkInventory(Player player, int size) {
        for (Integer i : getLockedSize(size)) {
            Inventory inventory = BingoInventory.getPlayerInventory(player);
            if (inventory.getItem(i + 9) != null) {
                if (Objects.requireNonNull(inventory.getItem(i)).getType() == Objects.requireNonNull(inventory.getItem(i + 9)).getType()) {
                    if (!BingoList.playerCollectedList.get(player.getDisplayName()).contains(Objects.requireNonNull(inventory.getItem(i)).getType())) {
                        lockSlot(player, i + 9);
                        BingoList.addMaterialToCollected(player, Objects.requireNonNull(inventory.getItem(i)).getType());
                        inventory.setItem(i + 9, BingoInventory.convertToLocked(inventory.getItem(i).getType(), player));
                        BingoInventory.updateInventory(player);
                        SideList.updateScoreboard();
                    }
                } else {
                    if (BingoList.playerCollectedList.get(player.getDisplayName()).contains(Objects.requireNonNull(inventory.getItem(i)).getType())) {
                        BingoList.removeMaterialFromCollected(player, Objects.requireNonNull(inventory.getItem(i).getType()));
                        BingoInventory.updateInventory(player);
                        unlockSlot(player, i + 9);
                        SideList.updateScoreboard();
                    }
                }
            }
        }
    }

    public static void createLock() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            lockedSlots.put(player.getDisplayName(), new ArrayList<Integer>());
            playerBans.put(player.getDisplayName(), new ArrayList<Integer>());
        }
    }

    public static void lockSlot(Player player, int slot) {
        if (!lockedSlots.get(player.getDisplayName()).contains((Integer) slot)) {
            lockedSlots.get(player.getDisplayName()).add(slot);
        }
    }

    public static List<Integer> getLockedSize(int size) {
        switch (size) {
            case 9:
                return alwaysLocked9;
            case 18:
                return alwaysLocked18;
            case 27:
                return alwaysLocked27;
        }
        return null;
    }

    public static void unlockSlot(Player player, int slot) {
        if (lockedSlots.get(player.getDisplayName()).contains((Integer) slot)) {
            lockedSlots.get(player.getDisplayName()).remove((Integer) slot);
        }
    }

    public static ArrayList<Integer> getLockedSlots(Player player) {
        return lockedSlots.get(player.getDisplayName());
    }

    private static void getMultiplier(int difficulty) {
        switch (difficulty) {
            case 0:
                multiplier = 1;
                break;
            case 1:
                multiplier = 2;
                break;
            case 2:
                multiplier = 4;
                break;
        }
    }


}
