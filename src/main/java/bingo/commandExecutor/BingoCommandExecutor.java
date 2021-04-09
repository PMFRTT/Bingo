package bingo.commandExecutor;

import bingo.BingoInventory;
import bingo.BingoList;
import bingo.BingoPlugin;
import bingo.teleporter.Respawner;
import bingo.teleporter.Teleporter;
import bingo.teleporter.TeleporterTimer;
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
                }else{
                    assert player != null;
                    player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Das Bingo hat noch &cnicht begonnen&f!"));
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("settings")) {
                    if (BingoPlugin.getTimer().isPaused() || player.isOp()) {
                        player.openInventory(main.getBingoSettings().getSettingsInventory().getInventory());
                    } else {
                        player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du kannst die &cEinstellungen nicht mehr ändern&f, wenn das Bingo gestartet wurde!"));
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    main.startBingo();
                } else if (args[0].equalsIgnoreCase("clear")) {
                    BingoPlugin.getTimer().pause();
                    BingoList.getBingoList().clear();
                    BingoList.playerBingoLists.clear();
                }else if(args[0].equalsIgnoreCase("respawn")){
                    Respawner.respawn(player);
                }
            }
        } else if (command.getLabel().equalsIgnoreCase("rtp")) {
            assert player != null;
            if (BingoPlugin.tpEnabled) {
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
