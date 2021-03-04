package bingo.eventhandler;

import bingo.BingoList;
import core.Utils;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CheckInventory {

    public static void checkInventory(Player player, boolean announcement) {
        List<Material> materialsToRemove = new ArrayList<Material>();
        for (Material material : BingoList.getBingoList(player)) {
            if (player.getInventory().contains(material)) {
                materialsToRemove.add(material);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 0f);
                if (announcement) {
                    Utils.sendMessageToEveryone(Utils.getPrefix("Bingo") + Utils.colorize("&e" + player.getDisplayName() + "&f hat &a" + bingo.Utils.formatMaterialName(material.toString()) + "&f gefunden!"));
                }
            }
        }
        for (Material material : materialsToRemove) {
            BingoList.removeMaterialFromList(player, material);
        }
    }


}
