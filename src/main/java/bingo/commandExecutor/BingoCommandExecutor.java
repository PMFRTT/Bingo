package bingo.commandExecutor;

import bingo.main.BingoInventory;
import bingo.main.BingoList;
import bingo.main.BingoPlugin;
import bingo.teleporter.Respawner;
import bingo.teleporter.Teleporter;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import core.Utils;

import java.util.Collection;

public class BingoCommandExecutor implements CommandExecutor {

    private final BingoPlugin main;
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static Player arg;
    public static BukkitScheduler scheduler = Bukkit.getScheduler();

    public BingoCommandExecutor(BingoPlugin main) {
        this.main = main;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }


        if (command.getLabel().equalsIgnoreCase("Bingo")) {
            if (args.length == 0) {
                if (!BingoPlugin.getTimer().isPaused()) {
                    assert player != null;
                    BingoInventory.updateInventory(player);
                    player.openInventory(BingoInventory.getPlayerInventory(player));
                } else {
                    assert player != null;
                    player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Das Bingo hat noch &cnicht begonnen&f!"));
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("settings")) {
                    if (BingoPlugin.getTimer().isPaused() || player.isOp()) {
                        player.openInventory(BingoPlugin.getBingoSettings().getSettingsInventory().getInventory());
                    } else {
                        player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du kannst die &cEinstellungen nicht mehr ändern&f, wenn das Bingo gestartet wurde!"));
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    if (BingoPlugin.getTimer().isPaused()) {
                        bingo.Utils.startBingo();
                    }
                } else if (args[0].equalsIgnoreCase("clear")) {
                    BingoPlugin.getTimer().pause();
                    assert BingoList.getBingoList() != null;
                    BingoList.getBingoList().clear();
                    BingoList.playerBingoLists.clear();
                } else if (args[0].equalsIgnoreCase("respawn")) {
                    assert player != null;
                    Respawner.respawn(player);
                } else if (args[0].equalsIgnoreCase("pause")) {
                    if (!BingoPlugin.getTimer().isPaused()) {
                        BingoPlugin.getTimer().pause();
                        for(Player player1 : Bukkit.getOnlinePlayers()){
                            core.core.CoreSendStringPacket.sendPacketToTitle(player1, Utils.colorize("&cPause!"), Utils.colorize("Das Bingo wurde &cpausiert&f!"));
                        }
                    }
                }
                else if (args[0].equalsIgnoreCase("resume")) {
                    if (BingoPlugin.getTimer().isPaused()) {
                        BingoPlugin.getTimer().resume();
                        for(Player player1 : Bukkit.getOnlinePlayers()){
                            core.core.CoreSendStringPacket.sendPacketToTitle(player1, Utils.colorize("&aWeiter!"), Utils.colorize("Das Bingo wurde &afortgesetzt&f!"));
                        }
                    }
                }
            }
        } else if (command.getLabel().equalsIgnoreCase("rtp")) {
            assert player != null;
            if (core.Utils.getSettingValueBool(BingoPlugin.getBingoSettings(), "Teleporter")) {
                if (Teleporter.canTP(player)) {
                    Teleporter.teleport(player);
                    player.sendMessage(Utils.getPrefix("Teleporter") + Utils.colorize("Du wurdest &ateleportiert&f!"));
                } else {
                    player.sendMessage(Utils.getPrefix("Teleporter") + Utils.colorize("Du musst noch &b" + Utils.formatTimerTimeText(Teleporter.getTimer(player).getSeconds()) + "&f warten!"));
                }
            } else {
                player.sendMessage(Utils.getPrefix("Teleporter") + Utils.colorize("Das Teleport-Feature ist &cdeaktiviert&f!"));
            }
            return false;
        }

        return false;
    }

}
