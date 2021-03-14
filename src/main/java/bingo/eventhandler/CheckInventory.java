package bingo.eventhandler;

import bingo.BingoList;
import bingo.BingoPlugin;
import core.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckInventory {

    private static final int min = 60;
    private static final int max = 180;
    private static int multiplier;

    public static void checkInventory(Player player, boolean announcement, boolean singleplayer, int difficulty) {
        getMultiplier(difficulty);
        List<Material> materialsToRemove = new ArrayList<Material>();
        for (Material material : BingoList.getBingoList(player)) {
            if (player.getInventory().contains(material)) {
                if(singleplayer){
                    double random = min + Math.random() * (max - min) * multiplier;
                    BingoPlugin.seconds += random;
                    player.sendMessage(Utils.colorize("&f+&a" + Utils.formatTimerTime((int) random)));
                }
                materialsToRemove.add(material);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0f);
                if (announcement && !singleplayer) {
                    Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + player.getDisplayName() + "&f hat &a" + bingo.Utils.formatMaterialName(material) + "&f gefunden!"));
                }
            }
        }
        for (Material material : materialsToRemove) {
            BingoList.removeMaterialFromList(player, material);
        }

    }

    private static void getMultiplier(int difficulty){
        switch(difficulty){
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
