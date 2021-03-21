package bingo.commandExecutor;

import bingo.BingoInventory;
import bingo.BingoList;
import bingo.BingoPlugin;
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
                assert player != null;
                BingoInventory.updateInventory(player);
                player.openInventory(BingoInventory.getPlayerInventory(player));
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("settings")) {
                    if (BingoPlugin.paused) {
                        player.openInventory(main.getBingoSettings().getSettingsInventory().getInventory());
                    }else{
                        player.sendMessage(Utils.getPrefix("Bingo") + Utils.colorize("Du kannst die &cEinstellungen nicht mehr ändern&f, wenn das Bingo gestartet wurde!"));
                    }
                } else if (args[0].equalsIgnoreCase("start")) {
                    main.startBingo();
                }else if(args[0].equalsIgnoreCase("clear")){
                    BingoPlugin.timer.pause();
                    BingoPlugin.paused = true;
                    BingoList.getBingoList().clear();
                    BingoList.playerBingoLists.clear();
                }
            }
        }

        return false;
    }

}
