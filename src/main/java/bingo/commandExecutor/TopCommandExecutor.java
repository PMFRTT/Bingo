package bingo.commandExecutor;

import core.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TopCommandExecutor implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getLabel().equalsIgnoreCase("Top")) {
            if (args.length == 0) {
                Player p = null;
                if (sender instanceof Player) {
                    p = (Player) sender;
                }
                World w = Bukkit.getWorld("world");
                assert p != null;
                Location locOfPlayer = p.getLocation();

                float yaw = p.getLocation().getYaw();
                float pitch = p.getLocation().getPitch();

                assert w != null;
                Block highestBlockAbovePlayer = w.getHighestBlockAt(locOfPlayer);
                double height = highestBlockAbovePlayer.getY();
                Location newLocOfPlayer = new Location(w, p.getLocation().getX(), height, p.getLocation().getZ(), yaw,
                        pitch);
                if (p.getLocation().getY() <= height - 1) {

                    p.teleport(newLocOfPlayer);
                    p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.GREEN + "Du wurdest an die Oberfläche teleportiert!");
                    return true;
                } else {
                    sender.sendMessage(Utils.getPrefix("Bingo") + ChatColor.RED + "Du Befindest dich bereits an der Oberfläche!");
                    return true;
                }

            }
        }
        return false;
    }
}
