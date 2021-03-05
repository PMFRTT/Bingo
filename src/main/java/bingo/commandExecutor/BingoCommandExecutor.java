package bingo.commandExecutor;

import bingo.BingoInventory;
import bingo.BingoList;
import bingo.BingoPlugin;
import bingo.BingoSettings;
import core.core.CoreResetServer;
import core.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import settings.SettingCycle;

import java.util.Collection;

public class BingoCommandExecutor implements CommandExecutor {

    private final BingoPlugin Bingo;
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static Player arg;
    public static BukkitScheduler scheduler = Bukkit.getScheduler();

    public BingoCommandExecutor(BingoPlugin Bingo) {
        this.Bingo = Bingo;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }


        if (command.getLabel().equalsIgnoreCase("Bingo")) {
            if (args.length == 0) {
                player.openInventory(BingoInventory.getPlayerInventory(player));
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("settings")) {
                    player.openInventory(Bingo.getBingoSettings().getSettingsInventory().getInventory());
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("start")) {
                    SettingCycle items = (SettingCycle) Bingo.getBingoSettings().getSettingbyName("Items");
                    if (args[1].equalsIgnoreCase("easy")) {
                        Bingo.startBingo(0, items.getValue());
                    } else if (args[1].equalsIgnoreCase("medium")) {
                        Bingo.startBingo(1, items.getValue());
                    } else if (args[1].equalsIgnoreCase("hard")) {
                        Bingo.startBingo(2, items.getValue());
                    }
                }
            }
            if (args.length == 3) {

            }
        }

        return false;
    }

}
