package bingo.eventhandler;

import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import bingo.summarizer.SummarizerCore;
import core.Utils;
import core.core.CoreMain;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

public class CheckInventory {

    private static final Integer EASY_MIN = 45;
    private static final Integer EASY_MAX = 120;

    private static final Integer MEDIUM_MIN = 150;
    private static final Integer MEDIUM_MAX = 200;

    private static final Integer HARD_MIN = 250;
    private static final Integer HARD_MAX = 360;

    public static HashMap<String, List<Integer>> playerBans = new HashMap<String, List<Integer>>();

    private static final HashMap<String, ArrayList<Integer>> lockedSlots = new HashMap<String, ArrayList<Integer>>();

    public static void checkInventory(int size) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Integer i : createLockedList(size)) {
                Inventory inventory = BingoInventory.getPlayerInventory(player);
                if (inventory.getItem(i + 9) != null) {
                    if (Objects.requireNonNull(inventory.getItem(i)).getType() == Objects.requireNonNull(inventory.getItem(i + 9)).getType()) {
                        if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Singleplayer")) {
                            if (!BingoList.playerCollectedList.get(player.getDisplayName()).contains(Objects.requireNonNull(inventory.getItem(i)).getType())) {
                                int j = getRandomTimerAddition(BingoList.contains(inventory.getItem(i).getType()));
                                BingoPlugin.getTimer().addSeconds(j);
                                CoreMain.hotbarManager.getHotbarScheduler(player).scheduleMessage(Utils.colorize("Du hast &a" + Utils.formatTimerTimeTicks(j * 20) + " &ferhalten"), 100);
                                SummarizerCore.getSummarization(player).addSinglePlayerTime(inventory.getItem(i).getType(), j * 20);
                            }
                        }
                        if (!BingoList.playerCollectedList.get(player.getDisplayName()).contains(Objects.requireNonNull(inventory.getItem(i)).getType())) {
                            lockSlot(player, i + 9);
                            BingoList.addMaterialToCollected(player, Objects.requireNonNull(inventory.getItem(i)).getType());
                            inventory.setItem(i + 9, BingoInventory.convertToLocked(inventory.getItem(i).getType(), player));
                            BingoInventory.updateInventory(player);
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                        }
                    } else {
                        if (BingoList.playerCollectedList.get(player.getDisplayName()).contains(Objects.requireNonNull(inventory.getItem(i)).getType())) {
                            BingoList.removeMaterialFromCollected(player, Objects.requireNonNull(inventory.getItem(i).getType()));
                            BingoInventory.updateInventory(player);
                            unlockSlot(player, i + 9);
                        }
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

    public static void unlockSlot(Player player, int slot) {
        if (lockedSlots.get(player.getDisplayName()).contains((Integer) slot)) {
            lockedSlots.get(player.getDisplayName()).remove((Integer) slot);
        }
    }

    public static ArrayList<Integer> getLockedSlots(Player player) {
        return lockedSlots.get(player.getDisplayName());
    }


    private static int getRandomTimerAddition(Integer difficulty) {
        Random random = new Random();
        float multiplier = 0.5f * core.Utils.getSettingValueInt(BingoPlugin.getBingoSettings().singlePlayerSubSettings, "Zeit-Multiplikator");
        switch (difficulty) {
            case 0:
                double i = (random.nextInt(EASY_MAX - EASY_MIN) + EASY_MIN);
                return (int) ( i * multiplier);
            case 1:
                double j = (random.nextInt(MEDIUM_MAX - MEDIUM_MIN) + MEDIUM_MIN);
                return (int) ( j * multiplier);
            case 2:
                double k = (random.nextInt(HARD_MAX - HARD_MIN) + HARD_MIN);
                return (int) ( k * multiplier);
        }
        return 0;
    }

    public static ArrayList<Integer> createLockedList(Integer size) {
        if (0 < size && size <= 9) {
            return new ArrayList<Integer>() {{
                addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
            }};
        } else if (9 < size && size <= 18) {
            return new ArrayList<Integer>() {{
                addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26));
            }};
        } else if (18 < size && size <= 27) {
            return new ArrayList<Integer>() {{
                addAll(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 18, 19, 20, 21, 22, 23, 24, 25, 26, 36, 37, 38, 39, 40, 41, 42, 43, 44));
            }};
        }
        return null;
    }

}
