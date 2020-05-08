package bingo;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import core.CoreMain;
import core.Utils;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

public final class BingoPlugin extends JavaPlugin implements Listener {

    public static ArrayList<HashMap> PlayersBingo = new ArrayList<HashMap>();
    public static ArrayList<Material> bingoList = new ArrayList<Material>();
    public static ArrayList<String> voteReset = new ArrayList<String>();
    public static ArrayList<Player> joined = new ArrayList<Player>();
    public static ArrayList<Player> onlinePlayers = new ArrayList<Player>();
    public static Collection<? extends Player> players = Bukkit.getOnlinePlayers();
    public static HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    public static HashMap<Player, Inventory> invmap = new HashMap<Player, Inventory>();

    public static int seconds = 0;

    public static boolean unlockedInv = false;
    public static boolean isPaused = false;
    public static boolean cancelFallDamage = false;
    public static boolean keepInventory = false;
    public static boolean timber = false;
    public static boolean enableMobs = true;
    public static boolean hasEnded = false;
    public static boolean hasStarted = false;

    public static Random random = new Random();

    public static World world = Bukkit.getWorld("world");

    public static String difficulty = "";

    @Override
    public void onEnable() {

        CoreMain.setPlugin(this);
        Bukkit.getPluginManager().registerEvents(this,this);
        BingoCommandExecutor commandExecutor = new BingoCommandExecutor(this);
        getCommand("Bingo").setExecutor(commandExecutor);

        this.getLogger().info("Plugin Bingo wurde erfolgreich geladen!");
        BukkitScheduler scheduler1 = getServer().getScheduler();

        scheduler1.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                if (!joined.isEmpty() && !voteReset.isEmpty())
                    if (joined.size() == voteReset.size()) {
                        for (Player p : players) {
                            ByteArrayDataOutput out = ByteStreams.newDataOutput();
                            out.writeUTF("Connect");
                            out.writeUTF("playServer");
                            //p.sendPluginMessage(Bingo, "BungeeCord", out.toByteArray());
                        }
                    }

                for (Player p : players) {
                    onlinePlayers.add(p);
                }

                /**
                 * for (Player p : joined) { if ( hasStarted) {
                 * BingoScoreboard.setSideBarScoreboard(map.get(p.getDisplayName()).size(),
                 * "Fehlende Items für " + p.getDisplayName());
                 *
                 * } }
                 */
                /*for (Player player : players) {
                    if (hasStarted == true && isPaused == false) {
                        String msg = "Das Spiel läuft seit " + BingoMath.formatTime(hours, minutes, seconds);
                        BingoHotBarText.sendPacket(player, msg, ChatColor.DARK_GREEN);
                    } else if (hasStarted == false || isPaused == true) {
                        String msg = "Das Spiel ist pausiert";
                        BingoHotBarText.sendPacket(player, msg, ChatColor.DARK_RED);
                    }

                }**/

                // BingoScoreboard.setSideBarScoreboard(onlinePlayers.get(0), 0, "Timer: " +
                // ChatColor.GREEN + BingoMath.formatTime(hours, minutes, seconds));


                onlinePlayers.clear();
            }
        }, 0L, 20);

        BukkitScheduler scheduler2 = getServer().getScheduler();

        scheduler2.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {

                for (Player plf : joined) {
                    if (hasStarted == true) {
                        if (!map.isEmpty()) {
                            if (map.get(plf.getDisplayName()).size() == 0 && hasEnded == false) {
                                hasEnded = true;
                                hasStarted = false;
                                invmap.clear();
                                map.clear();
                                bingoList.clear();
                                PlayersBingo.clear();
                                hasEnded = false;
                                for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                                    all.sendMessage(Utils.getPrefix("Bingo") + ChatColor.GREEN + plf.getDisplayName() + ChatColor.WHITE
                                            + " hat das Bingo in " + ChatColor.DARK_GREEN
                                            + Utils.formatTimerTime(seconds) + ChatColor.WHITE
                                            + " gewonnen!");
                                    all.playSound(all.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                                }
                            }
                        }
                    }
                }

                for (String string : BingoList.finishedListString) {
                    Material mat = Material.getMaterial(string);
                    for (Player pll : players) {
                        if (mat != null) {
                            if (map.containsKey(pll.getDisplayName())) {
                                if (map.get(pll.getDisplayName()).contains(string)) {
                                    if (pll.getInventory().contains(mat)) {
                                        BingoList.updateMaps(map, string, pll.getDisplayName());
                                        if (invmap.containsKey(pll)) {
                                            BingoInventory.updateInventory(invmap, pll);
                                        }
                                        for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                                            String temp = mat.toString();
                                            temp = temp.replace("_", " ");
                                            all.sendMessage(Utils.getPrefix("Bingo") + ChatColor.GREEN + pll.getDisplayName()
                                                    + ChatColor.WHITE + " hat " + ChatColor.AQUA + temp
                                                    + ChatColor.WHITE + " gefunden");
                                            pll.playSound(pll.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);

                                        }

                                    }
                                }
                            }
                        }
                    }
                }

            }
        }, 0L, 2);

    }

    public void onDisable() {
        this.getLogger().info("Plugin Bingo wurde erfolgreich deaktiviert!");

    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == BingoSettings.Settings) {

            if (e.getSlot() == 3) {
                e.setCancelled(true);
                difficulty = "easy";
                BingoSettings.easyMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.mediumMeta.hasEnchants()) {
                    BingoSettings.mediumMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.hardMeta.hasEnchants()) {
                    BingoSettings.hardMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 4) {
                e.setCancelled(true);
                difficulty = "medium";
                BingoSettings.mediumMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.easyMeta.hasEnchants()) {
                    BingoSettings.easyMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.hardMeta.hasEnchants()) {
                    BingoSettings.hardMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 5) {
                e.setCancelled(true);
                difficulty = "hard";
                BingoSettings.hardMeta.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
                if (BingoSettings.easyMeta.hasEnchants()) {
                    BingoSettings.easyMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                if (BingoSettings.mediumMeta.hasEnchants()) {
                    BingoSettings.mediumMeta.removeEnchant(Enchantment.ARROW_DAMAGE);
                }
                BingoSettings.update();
            }

            if (e.getSlot() == 9) {
                e.setCancelled(true);
                if (keepInventory == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory true");
                    BingoSettings.keepInventory = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.keepInventoryMeta.setDisplayName(ChatColor.GREEN + "Keep Inventory");
                    BingoSettings.update();
                    keepInventory = true;
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory false");
                    BingoSettings.keepInventory = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.keepInventoryMeta.setDisplayName(ChatColor.WHITE + "Keep Inventory");
                    BingoSettings.update();
                    keepInventory = false;
                }
            }
            if (e.getSlot() == 10) {
                e.setCancelled(true);
                if (timber == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timber");
                    BingoSettings.enableTimber = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.enableTimberMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.GREEN + "Timber");
                    BingoSettings.update();
                    timber = true;
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timber");
                    BingoSettings.enableTimber = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.enableTimberMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Timber");
                    BingoSettings.update();
                    timber = false;
                }
            }

            if (e.getSlot() == 11) {
                e.setCancelled(true);
                if (enableMobs == false) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning true");
                    BingoSettings.disableMobs = new ItemStack(Material.LIME_DYE, 1);
                    BingoSettings.disableMobsMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.GREEN + "Monster");
                    BingoSettings.update();
                    enableMobs = true;
                } else {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");
                    BingoSettings.disableMobs = new ItemStack(Material.GRAY_DYE, 1);
                    BingoSettings.disableMobsMeta.setDisplayName(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Monster");
                    BingoSettings.update();
                    enableMobs = false;
                }
            }

            if (e.getSlot() == 26) {
                startBingo(difficulty, p);
                e.setCancelled(true);
                e.getWhoClicked().closeInventory();

            }
        }

        if (unlockedInv == false) {
            if (invmap.containsValue(e.getInventory())) {
                if (e.getCurrentItem() != null) {
                    if (e.getClickedInventory().equals(e.getInventory())) {
                        e.setCancelled(true);
                    }
                }

            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        if (isPaused == true) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent e) {
        if (e.getEntityType().equals(EntityType.PLAYER)) {
            if (e.getCause().toString().equals("FALL")) {
                if (cancelFallDamage == true) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        e.setJoinMessage(getJoinMessage(player));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        e.setQuitMessage(getDisconnectMessage(player));
    }

    public void scatter(Player p) {
        Location temp = p.getLocation();
        Random randome = new Random();
        temp.setX(temp.getX() + random.nextInt(9999));
        temp.setZ(temp.getZ() + randome.nextInt(9999));
        temp.setY(128);
        p.teleport(temp);
        cancelFallDamage = true;
        BukkitScheduler s = Bukkit.getScheduler();
        s.scheduleSyncDelayedTask(this, new Runnable() {

            @Override
            public void run() {
                cancelFallDamage = false;
            }

        }, 80L);
    }

    public void startBingo(String difficulty, Player p) {

        if (difficulty.equalsIgnoreCase("easy")) {
            if (joined.contains(p)) {
                if (hasStarted == false) {
                    hasStarted = true;
                    bingoList = BingoList.ScrambleList(BingoList.BingoEasyMat);
                    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                        all.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Bingo wird gestartet! "
                                + ChatColor.GREEN + "(Leicht)");
                        all.getInventory().clear();
                        all.setExp(0f);
                        all.setLevel(0);
                        scatter(all);

                    }
                    for (Player pl : joined) {
                        map = BingoList.fillMaps(pl.getDisplayName(), map);
                    }

                } else {
                    p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Das Bingo Läuft bereits!");
                }

            } else {
                p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Du musst dem Bingo erst beitreten!");
            }
        }

        if (difficulty.equalsIgnoreCase("medium")) {
            if (joined.contains(p)) {
                if (hasStarted == false) {
                    hasStarted = true;
                    bingoList = BingoList.ScrambleList(BingoList.BingoMediumMat);
                    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                        all.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Bingo wird gestartet! "
                                + ChatColor.GREEN + "(Mittel)");
                        all.getInventory().clear();
                        all.setExp(0f);
                        all.setLevel(0);
                        scatter(all);
                    }
                    for (Player pl : joined) {
                        map = BingoList.fillMaps(pl.getDisplayName(), map);
                    }

                } else {
                    p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Das Bingo Läuft bereits!");
                }

            } else {
                p.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Du musst dem Bingo erst beitreten!");

            }
        }

        if (difficulty.equalsIgnoreCase("hard")) {
            if (joined.contains(p)) {
                if (hasStarted == false) {
                    hasStarted = true;
                    bingoList = BingoList.ScrambleList(BingoList.BingoHardMat);
                    for (Player all : Bukkit.getServer().getOnlinePlayers()) {
                        all.sendMessage(Utils.getPrefix("Bingo") +  ChatColor.WHITE + "Bingo wird gestartet! "
                                + ChatColor.GREEN + "(Schwer)");
                        all.getInventory().clear();
                        all.setExp(0f);
                        all.setLevel(0);
                        scatter(all);

                    }
                    for (Player pl : joined) {
                        map = BingoList.fillMaps(pl.getDisplayName(), map);
                    }

                } else {
                    p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Das Bingo Läuft bereits!");
                }
            } else {
                p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.WHITE + "Du musst dem Bingo erst beitreten!");

            }
        } else if (difficulty.isEmpty()) {

            p.sendMessage(Utils.getPrefix("Bingo") + ChatColor.RED + "Keine Schwierigkeit ausgewählt!");

        }
    }

    private String getJoinMessage(Player player) {
        String message = Utils.getJoinPrefix("Bingo", player);
        return message;
    }

    private String getDisconnectMessage(Player player) {
        String message = Utils.getDisconnectPrefix("Bingo", player);
        return message;
    }

}
