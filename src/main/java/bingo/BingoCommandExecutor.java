package bingo;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import core.CoreSendStringPacket;
import core.Utils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageRecipient;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.Collection;

public class BingoCommandExecutor implements CommandExecutor {

    private BingoPlugin Bingo;
    Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static Player arg;
    public static BukkitScheduler scheduler = Bukkit.getScheduler();

    public BingoCommandExecutor(BingoPlugin Bingo) {
        this.Bingo = Bingo;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getLabel().equalsIgnoreCase("invsee")) {
            if (args.length == 0) {
                return false;
            } else {
                Player p = null;
                if (sender instanceof Player) {
                    p = (Player) sender;
                    p.openInventory(Bukkit.getPlayer(args[0]).getInventory());
                    return true;
                }
            }
        }

        if (command.getName().equalsIgnoreCase("heal")) {
            if (args.length == 0) {
                Player p;
                if (sender instanceof Player) {
                    p = (Player) sender;
                    p.setHealth(20);
                    p.setSaturation(20);
                    p.setFoodLevel(20);
                    p.sendMessage(Utils.getPrefix("Bingo") + "Du wurdest geheilt!");

                }
            } else if (args[0].equalsIgnoreCase("all")) {
                for (Player p : players) {
                    p.setHealth(20);
                    p.setSaturation(20);
                    p.setFoodLevel(20);

                    p.sendMessage(
                            Utils.getPrefix("Bingo") + "Du wurdest von " + ChatColor.AQUA + sender.getName() + ChatColor.WHITE + " geheilt!");
                }
            }

            else if (players.contains(Bukkit.getPlayer(args[0]))) {
                Player p = Bukkit.getPlayer(args[0]);
                assert p != null;
                p.setHealth(20);
                p.setSaturation(20);
                p.setFoodLevel(20);

                p.sendMessage(Utils.getPrefix("Bingo") + "Du wurdest von " + ChatColor.AQUA + sender.getName() + ChatColor.WHITE + " geheilt!");
            }
        }

        if (command.getName().equalsIgnoreCase("hub")) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("Lobby");
            ((PluginMessageRecipient) sender).sendPluginMessage(Bingo, "BungeeCord", out.toByteArray());
            return true;
        }

        if (command.getName().equalsIgnoreCase("Lobby")) {

            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF("Lobby");
            ((PluginMessageRecipient) sender).sendPluginMessage(Bingo, "BungeeCord", out.toByteArray());
            return true;
        }

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

        else if (command.getLabel().equalsIgnoreCase("Bingo")) {

            if (args.length == 0) {
                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                if (BingoPlugin.joined.contains(player)) {
                    if (!BingoPlugin.isPaused) {
                        if (BingoPlugin.hasStarted) {
                            if (!BingoPlugin.invmap.containsKey(player)) {
                                BingoPlugin.invmap = BingoInventory.createInv(player);
                            }
                            BingoInventory.updateInventory(BingoPlugin.invmap, player);
                            assert player != null;
                            player.openInventory(BingoPlugin.invmap.get(player));
                            return true;
                        } else {
                            sender.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE
                                    + "Du musst das Bingo zuerst starten!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE
                                + "Bitte warte bis das Spiel fortgesetzt wird!");
                        return true;
                    }
                } else {
                    sender.sendMessage(
                            Utils.getPrefix("Bingo") +  ChatColor.RED + "Du musst dem Bingo erst beitreten");
                    return true;
                }

            }

            else if (args[0].equalsIgnoreCase("join")) {
                if (!BingoPlugin.hasStarted) {
                    BingoPlugin.joined.add((Player) sender);
                    for (Player p : players)
                        p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.AQUA
                                + sender.getName() + ChatColor.WHITE + " ist dem Bingo beigetreten!");
                    return true;
                } else {
                    Player player = null;
                    if (sender instanceof Player) {
                        player = (Player) sender;
                    }
                    BingoPlugin.joined.add((Player) sender);
                    BingoPlugin.map = BingoList.fillMaps(player.getDisplayName(), BingoPlugin.map);
                    for (Player p : players)
                        p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.AQUA
                                + sender.getName() + ChatColor.WHITE + " ist dem Bingo beigetreten!");
                    return true;
                }

            }

            else if (args[0].equalsIgnoreCase("Start")) {

                if (args.length == 1) {
                    Player p;
                    if (sender instanceof Player) {
                        p = (Player) sender;
                        p.openInventory(BingoSettings.Settings());
                        return true;
                    }

                    return true;
                }

            }

            else if (args[0].equalsIgnoreCase("solve")) {
                if (args.length == 1) {
                    if (BingoPlugin.hasStarted == true) {
                        sender.sendMessage(Utils.getPrefix("Bingo") + ChatColor.RED
                                + "Bitte gib an, welches Item gelöst werden soll!");
                        return true;
                    } else {
                        sender.sendMessage(
                                Utils.getPrefix("Bingo") + ChatColor.RED + "Das Spiel muss gestartet sein!");
                        return true;
                    }
                } else if (args.length == 2) {
                    if (BingoPlugin.hasStarted == true) {
                        sender.sendMessage(Utils.getPrefix("Bingo") + ChatColor.RED
                                + "Bitte gib an, für welchen Spieler das Item gelöst werden soll!");
                        return true;
                    } else {
                        sender.sendMessage(
                                Utils.getPrefix("Bingo") + ChatColor.RED + "Das Spiel muss gestartet sein!");
                        return true;
                    }
                } else if (args.length == 3) {
                    Player player = Bukkit.getPlayer(args[2]);
                    if (BingoPlugin.hasStarted == true) {
                        if (BingoPlugin.joined.contains(player)) {
                            if (BingoPlugin.map.get(player.getDisplayName()).contains(args[1])) {
                                BingoPlugin.map.get(player.getDisplayName()).remove(args[1]);
                                sender.sendMessage(Utils.getPrefix("Bingo") + "Item " + ChatColor.AQUA + args[1]
                                        + ChatColor.WHITE + " wurde im Bingo von " + ChatColor.GREEN + args[2]
                                        + ChatColor.WHITE + " gelöst!");
                                return true;
                            }
                        } else if (args[2].equalsIgnoreCase("all")) {
                            for (Player all : BingoPlugin.joined) {
                                BingoPlugin.map.get(all.getDisplayName()).remove(args[1]);
                            }
                            sender.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Item " + ChatColor.AQUA
                                    + args[1] + ChatColor.WHITE + " wurde für " + ChatColor.GREEN + "jeden"
                                    + ChatColor.WHITE + " gelöst!");
                        } else {

                            sender.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + " Spieler " + ChatColor.RED
                                    + args[2] + ChatColor.WHITE + " ist nicht im Bingo!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(
                                Utils.getPrefix("Bingo") +  ChatColor.RED + "Das Spiel muss gestartet sein!");
                        return true;

                    }
                }

                return true;
            }

            else if (args[0].equalsIgnoreCase("Reset")) {

                BingoPlugin.hasEnded = false;
                BingoPlugin.hasStarted = false;
                BingoPlugin.bingoList.clear();
                BingoPlugin.map.clear();
                BingoPlugin.invmap.clear();
                BingoPlugin.seconds = 0;
                BingoPlugin.joined.clear();
                BingoPlugin.voteReset.clear();

                for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                    CoreSendStringPacket.sendPacketToTitle(all, ChatColor.BLUE + "Fallback Server", "Du wurdest auf " + ChatColor.GREEN + "[Lobby]" + ChatColor.WHITE + " verschoben");
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("Connect");
                    out.writeUTF("Lobby");
                    all.sendPluginMessage(Bingo, "BungeeCord", out.toByteArray());
                }
                scheduler.scheduleSyncDelayedTask(Bingo, new Runnable() {

                    @Override
                    public void run() {
                        Bukkit.spigot().restart();
                    }

                }, 20L);




                return true;

            }

            else if (args[0].equalsIgnoreCase("Help")) {

                if (args[1].equalsIgnoreCase("1")) {

                    sender.sendMessage(ChatColor.GOLD + "Bingo Help Seite <1>");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo Help:" + ChatColor.WHITE + " Zeigt dir diese Hilfe");
                    sender.sendMessage(
                            ChatColor.GOLD + "/Bingo:" + ChatColor.WHITE + " Zeigt dir deine Bingo-Liste an");
                    sender.sendMessage(
                            ChatColor.GOLD + "/Bingo Start <Schwierigkeit>:" + ChatColor.WHITE + " Startet das Bingo");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo Join: " + ChatColor.WHITE + " Tritt dem Bingo bei");
                    return true;

                }

                else if (args[1].equalsIgnoreCase("2")) {

                    sender.sendMessage(ChatColor.GOLD + "Bingo Help Seite <2>");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo Pause:" + ChatColor.WHITE + " Pausiert das Spiel");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo Reset:" + ChatColor.WHITE + " Lädt die Bingotafel neu");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo Resume:" + ChatColor.WHITE + " Setzt das SPiel fort");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo toggleLock" + ChatColor.WHITE + "");
                    sender.sendMessage(ChatColor.GOLD + "/Bingo debug:" + ChatColor.WHITE + "");
                    return true;

                } else {
                    sender.sendMessage(
                            Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Bitte benutze /Bingo Help [Seite]");
                }
                return true;

            }

            else if (args[0].equalsIgnoreCase("toggleLock")) {
                if (sender.hasPermission("bingo.toggleLock"))

                    if (BingoPlugin.unlockedInv == false) {
                        BingoPlugin.unlockedInv = true;
                        sender.sendMessage(Utils.getPrefix("Bingo") + "Inventar wurde entsperrt!");
                        return true;
                    } else if (BingoPlugin.unlockedInv == true) {
                        BingoPlugin.unlockedInv = false;
                        sender.sendMessage(Utils.getPrefix("Bingo") + "Inventar wurde gesperrt");
                        return true;
                    }
                return true;
            }

            else if (args[0].equals("debug")) {
                sender.sendMessage(ChatColor.DARK_RED + "debug: Spieler Online: " + ChatColor.WHITE + players.size());
                sender.sendMessage(ChatColor.DARK_RED + "debug: Items in der BingoListe: " + ChatColor.WHITE
                        + BingoPlugin.bingoList.size());
                sender.sendMessage(
                        ChatColor.DARK_RED + "debug: hasStarted:  " + ChatColor.WHITE + BingoPlugin.hasStarted);
                sender.sendMessage(ChatColor.DARK_RED + "debug: hasEnded: " + ChatColor.WHITE + BingoPlugin.hasEnded);
                sender.sendMessage(
                        ChatColor.DARK_RED + "debug: invMapSize" + ChatColor.WHITE + BingoPlugin.invmap.size());
                sender.sendMessage(ChatColor.DARK_RED + "debug: invLocked " + ChatColor.WHITE + BingoPlugin.unlockedInv);
                sender.sendMessage(ChatColor.DARK_RED + "debug: isPaused " + ChatColor.WHITE + BingoPlugin.isPaused);
                sender.sendMessage(
                        ChatColor.DARK_RED + "debug: Spieler Im Bingo: " + ChatColor.WHITE + BingoPlugin.joined);
                for (Player pl : players) {
                    if (BingoPlugin.hasStarted == true) {
                        sender.sendMessage(ChatColor.DARK_RED + "debug: PlayerITemsNeeded " + ChatColor.WHITE
                                + BingoPlugin.map.get(pl.getDisplayName()) + ":" + pl.getDisplayName());
                        sender.sendMessage(ChatColor.DARK_RED + "debug: PlayerItemsNeededInt" + ChatColor.WHITE
                                + BingoPlugin.map.get(pl.getDisplayName()).size());
                    }
                }
                return true;
            }

            else if (args[0].equalsIgnoreCase("pause")) {
                BingoPlugin.isPaused = true;
                Bukkit.getWorld("world").setDifficulty(Difficulty.PEACEFUL);
                for (Player p : players) {
                    p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Das Bingo wurde von "
                            + ChatColor.DARK_AQUA + sender.getName() + ChatColor.WHITE + " pausiert!");
                }
                return true;

            }

            else if (args[0].equalsIgnoreCase("resume")) {
                if (sender.hasPermission("bingo.reset")) {
                    BingoPlugin.isPaused = false;
                    Bukkit.getWorld("world").setDifficulty(Difficulty.EASY);
                    for (Player p : players) {
                        p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Das Bingo wurde von "
                                + ChatColor.DARK_AQUA + sender.getName() + ChatColor.WHITE + " fortgesetzt!");
                    }
                    return true;
                }
            }

            else if (args[0].equalsIgnoreCase("reloadPlugin")) {
                if (sender.hasPermission("bingo.toggleLock")) {
                    Bukkit.getServer().reload();
                    sender.sendMessage(Utils.getPrefix("Bingo") + "Server has been reloaded");
                    return true;
                }
            }

            else if (args[0].equalsIgnoreCase("leave")) {
                if (BingoPlugin.hasStarted == false) {
                    BingoPlugin.joined.remove((Player) sender);
                    for (Player p : players)
                        p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.RED
                                + sender.getName() + ChatColor.WHITE + " hat das Bingo verlassen!");
                    return true;
                } else {
                    BingoPlugin.joined.remove(sender);
                    for (Player p : players)
                        p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.RED
                                + sender.getName() + ChatColor.WHITE + " hat das Bingo verlassen!");
                    return true;
                }
            }

            if (players.contains(arg)) {
                if (args[0].equalsIgnoreCase(arg.getDisplayName())) {

                    if (BingoPlugin.joined.contains(arg)) {
                        if (!BingoPlugin.isPaused) {
                            if (BingoPlugin.hasStarted) {
                                if (!BingoPlugin.invmap.containsKey(arg)) {
                                    BingoPlugin.invmap = BingoInventory.createInv(arg);
                                }
                                BingoInventory.updateInventory(BingoPlugin.invmap, arg);
                                ((HumanEntity) sender).openInventory(BingoPlugin.invmap.get(arg));
                                return true;
                            } else {
                                sender.sendMessage(Utils.getPrefix("Bingo") + "Du musst das Bingo zuerst starten!");
                                return true;
                            }
                        } else {
                            sender.sendMessage(Utils.getPrefix("Bingo") + "Bitte warte bis das Spiel fortgesetzt wird!");
                            return true;
                        }
                    } else {
                        sender.sendMessage(Utils.getPrefix("Bingo") + ChatColor.RED + arg.getDisplayName()
                                + ChatColor.WHITE + " muss dem Bingo erst beitreten!");
                        return true;
                    }

                }
            } else if (!players.contains(arg)) {
                sender.sendMessage(
                        Utils.getPrefix("Bingo") + ChatColor.RED + args[0] + ChatColor.WHITE + " ist nicht Online!");
                return true;
            }

            else if (args[0].equalsIgnoreCase("settings")) {
            }

            else {
            }
            sender.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.RED + "/" + label + " " + args[0]
                    + " existiert nicht! Versuche" + ChatColor.YELLOW + " /Bingo Help");
            return false;

        }

        return false;
    }

}
