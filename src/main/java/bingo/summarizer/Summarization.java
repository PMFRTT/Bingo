package bingo.summarizer;

import bingo.main.BingoPlugin;
import bingo.Utils;
import core.debug.DebugSender;
import core.debug.DebugType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Summarization {

    private final Player player;
    private final HashMap<Material, HashMap<String, Integer>> itemTimeStamps = new HashMap<Material, HashMap<String, Integer>>();

    public Summarization(Player player) {
        this.player = player;
    }

    public void collectedItem(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            if (!this.itemTimeStamps.get(material).containsKey("first_found")) {
                this.itemTimeStamps.get(material).put("first_found", SummarizerCore.getTimer().getTicks());
                DebugSender.sendDebug(DebugType.PLUGIN, "first_found: " + Utils.formatMaterialName(material) + " " + itemTimeStamps.get(material).get("first_found"));
            } else {
                DebugSender.sendDebug(DebugType.PLUGIN, "last_found: " + Utils.formatMaterialName(material) + " " + itemTimeStamps.get(material).get("last_found"));
                this.itemTimeStamps.get(material).put("last_found", SummarizerCore.getTimer().getTicks());
            }
        } else {
            this.itemTimeStamps.put(material, new HashMap<String, Integer>());
            collectedItem(material);
        }
    }

    public void addSinglePlayerTime(Material material, Integer ticks) {
        if (this.itemTimeStamps.containsKey(material)) {
            if (!this.itemTimeStamps.get(material).containsKey("singleplayer_time")) {
                this.itemTimeStamps.get(material).put("singleplayer_time", ticks);
                DebugSender.sendDebug(DebugType.PLUGIN, "singleplayer_time: " + Utils.formatMaterialName(material) + " " + itemTimeStamps.get(material).get("singleplayer_time"));
            }
        } else {
            this.itemTimeStamps.put(material, new HashMap<String, Integer>());
            collectedItem(material);
        }
    }

    public void lockedItem(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            if (!this.itemTimeStamps.get(material).containsKey("first_locked")) {
                this.itemTimeStamps.get(material).put("first_locked", SummarizerCore.getTimer().getTicks());
                DebugSender.sendDebug(DebugType.PLUGIN, "first_locked: " + Utils.formatMaterialName(material) + " " + itemTimeStamps.get(material).get("first_locked"));

            } else {
                this.itemTimeStamps.get(material).put("last_locked", SummarizerCore.getTimer().getTicks());
                DebugSender.sendDebug(DebugType.PLUGIN, "last_locked: " + Utils.formatMaterialName(material) + " " + itemTimeStamps.get(material).get("last_locked"));

            }
        } else {
            this.itemTimeStamps.put(material, new HashMap<String, Integer>());
            lockedItem(material);
        }
    }

    public Integer getFirstFoundTicks(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            return this.itemTimeStamps.get(material).get("first_found");
        }
        return 0;
    }

    public Integer getLastFoundTicks(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            return this.itemTimeStamps.get(material).get("last_found");
        }
        return 0;
    }

    public Integer getFirstLockedTicks(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            return this.itemTimeStamps.get(material).get("first_locked");
        }
        return 0;
    }

    public Integer getLastLockedTicks(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            return this.itemTimeStamps.get(material).get("last_locked");
        }
        return 0;
    }

    public Integer getSingleplayerTimeTicks(Material material) {
        if (this.itemTimeStamps.containsKey(material)) {
            return this.itemTimeStamps.get(material).get("singleplayer_time");
        }
        return 0;
    }

    public Player getOwner() {
        return this.player;
    }


}
