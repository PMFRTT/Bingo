package bingo.main;

import com.google.common.util.concurrent.SettableFuture;
import core.settings.PluginSettings;
import core.settings.Setting.Setting;
import core.settings.Setting.SettingCycle;
import core.settings.Setting.SettingsType;
import core.settings.SubSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import core.Utils;

import java.util.ArrayList;

public class BingoSettings extends PluginSettings {

    private final Plugin plugin;

    public SubSettings singlePlayerSubSettings;
    public SubSettings scatterPlayerSubSettings;
    public SubSettings teleporterSubSettings;
    public SubSettings banningSettings;
    public SubSettings itemSubSettings;

    public BingoSettings(Plugin plugin) {
        super(plugin.getName() + "-Einstellungen", plugin);
        this.plugin = plugin;
        update();
    }

    public void update() {
        this.getSettingsList().clear();
        addSettings();
        blockChecker();
    }

    private void addSettings() {
        singlePlayerSubSettings = new SubSettings("Singleplayer-Einstellungen", plugin, this);
        scatterPlayerSubSettings = new SubSettings("Scatter-Einstellungen", plugin, this);
        teleporterSubSettings = new SubSettings("Teleporter-Einstellungen", plugin, this);
        banningSettings = new SubSettings("Items-Bannen-Einstellungen", plugin, this);
        itemSubSettings = new SubSettings("Item-Anzahl-Einstellungen", plugin, this);

        this.addSetting("Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dieser Einstellung kannst du wählen"));
            add(Utils.colorize("&7wie viele &6Items &7jeder Spieler"));
            add(Utils.colorize("&7insgesamt sammeln muss!"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.COMPARATOR, itemSubSettings);

        itemSubSettings.addSetting("Einfache Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dieser Einstellung kannst du wählen"));
            add(Utils.colorize("&7wie viele &aeinfache Items &7jeder Spieler"));
            add(Utils.colorize("&7sammeln muss!"));
        }}, Material.LIME_WOOL, new ArrayList<Integer>() {{
            for (int i = 0; i <= 27; i++) {
                add(i);
            }
        }});

        itemSubSettings.addSetting("Mittlere Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dieser Einstellung kannst du wählen"));
            add(Utils.colorize("&7wie viele &6mittlere Items &7jeder Spieler"));
            add(Utils.colorize("&7sammeln muss!"));
        }}, Material.ORANGE_WOOL, new ArrayList<Integer>() {{
            for (int i = 0; i <= 27; i++) {
                add(i);
            }
        }});

        itemSubSettings.addSetting("Schwere Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dieser Einstellung kannst du wählen"));
            add(Utils.colorize("&7wie viele &cschwere Items &7jeder Spieler"));
            add(Utils.colorize("&7sammeln muss!"));
        }}, Material.RED_WOOL, new ArrayList<Integer>() {{
            for (int i = 0; i <= 27; i++) {
                add(i);
            }
        }});

        this.addSetting("Keep Inventory", new ArrayList<String>() {{
            add(Utils.colorize("&7Legt fest, ob Spieler nach dem Tod"));
            add(Utils.colorize("&7ihre Items &cverlieren &7oder &abehalten"));
        }}, Material.ENDER_EYE, false);

        this.addSetting("Scatter Players", new ArrayList<String>() {{
            add(Utils.colorize("&7Legt fest, ob Spieler"));
            add(Utils.colorize("&7zu beginn des Bingos an"));
            add(Utils.colorize("&7eine zufällige Position"));
            add(Utils.colorize("&6teleportiert &7werden"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.ENDER_PEARL, true, scatterPlayerSubSettings);

        scatterPlayerSubSettings.addSetting("Scatter-Größe", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7wie groß der &6Radius&7 des "));
            add(Utils.colorize("&7Scatterns sein soll"));
        }}, Material.CARTOGRAPHY_TABLE, new ArrayList<Integer>() {{
            add(100);
            add(250);
            add(500);
            add(750);
            add(1000);
            add(1500);
            add(2000);
            add(5000);
            add(10000);
            add(20000);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b100 &fBlöcke"));
            add(Utils.colorize("&b250 &fBlöcke"));
            add(Utils.colorize("&b500 &fBlöcke"));
            add(Utils.colorize("&b750 &fBlöcke"));
            add(Utils.colorize("&b1000 &fBlöcke"));
            add(Utils.colorize("&b1500 &fBlöcke"));
            add(Utils.colorize("&b2000 &fBlöcke"));
            add(Utils.colorize("&b5000 &fBlöcke"));
            add(Utils.colorize("&b10000 &fBlöcke"));
            add(Utils.colorize("&b20000 &fBlöcke"));
        }});

        this.addSetting("Singleplayer", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dem Singleplayer-Modus kannst"));
            add(Utils.colorize("&7du &6alleine &7gegen die Zeit spielen"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.TOTEM_OF_UNDYING, false, singlePlayerSubSettings);

        singlePlayerSubSettings.addSetting("Start-Zeit", new ArrayList<String>() {{
            add(core.Utils.colorize("&7Hier kannst du einstellen, mit wie"));
            add(core.Utils.colorize("&7viel &6Zeit &7du in dem"));
            add(core.Utils.colorize("&7Singleplayer-Modus startest"));
        }}, Material.CLOCK, new ArrayList<Integer>() {{
            for (int i = 60; i <= 600; i+=60) {
                add(i);
            }
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&c1 Minute"));
            add(Utils.colorize("&c2 Minuten"));
            add(Utils.colorize("&c3 Minuten"));
            add(Utils.colorize("&e4 Minuten"));
            add(Utils.colorize("&e5 Minuten"));
            add(Utils.colorize("&e6 Minuten"));
            add(Utils.colorize("&e7 Minuten"));
            add(Utils.colorize("&a8 Minuten"));
            add(Utils.colorize("&a9 Minuten"));
            add(Utils.colorize("&a10 Minuten"));
        }});

        singlePlayerSubSettings.addSetting("Zeit-Multiplikator", new ArrayList<String>() {{
            add(core.Utils.colorize("&7Hier kannst du einstellen, wie"));
            add(core.Utils.colorize("&7viel &6Zeit &7man pro Item"));
            add(core.Utils.colorize("&7erhält!"));
        }}, Material.CLOCK, new ArrayList<Integer>() {{
            add(1);
            add(2);
            add(3);
            add(4);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&c0.5x"));
            add(Utils.colorize("&e1.0x"));
            add(Utils.colorize("&e1.5x"));
            add(Utils.colorize("&a2.0x"));
        }});

        this.addSetting("Teleporter", new ArrayList<String>() {{
            add(Utils.colorize("&7Ermöglicht dem Spieler sich"));
            add(Utils.colorize("&7entweder einmalig oder mit einem"));
            add(Utils.colorize("&7Countdown zu &ateleportieren!"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.ENDERMAN_SPAWN_EGG, false, teleporterSubSettings);

        teleporterSubSettings.addSetting("Countdown", new ArrayList<String>() {{
            add(Utils.colorize("&7Wenn diese Einstellung aktiv ist,"));
            add(Utils.colorize("&7kann der Teleporter &6wiederholt nach"));
            add(Utils.colorize("&6einer bestimmten Zeit &7genutzt werden!"));
        }}, Material.CLOCK, true);

        teleporterSubSettings.addSetting("Countdown-Zeit", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier legst du fest, &6wie lange&7 ein"));
            add(Utils.colorize("&7Spieler &6warten &7muss, bis er sich"));
            add(Utils.colorize("&7wieder teleportieren kann!"));
        }}, Material.NAUTILUS_SHELL, new ArrayList<Integer>() {{
            add(120);
            add(300);
            add(600);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b2 Minuten"));
            add(Utils.colorize("&b5 Minuten"));
            add(Utils.colorize("&b10 Minuten"));
        }});

        teleporterSubSettings.addSetting("Teleporter-Radius", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7wie groß der &6Radius&7 des "));
            add(Utils.colorize("&7Telportierens sein soll"));
        }}, Material.CARTOGRAPHY_TABLE, new ArrayList<Integer>() {{
            add(100);
            add(250);
            add(500);
            add(750);
            add(1000);
            add(1500);
            add(2000);
            add(5000);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b100 &fBlöcke"));
            add(Utils.colorize("&b250 &fBlöcke"));
            add(Utils.colorize("&b500 &fBlöcke"));
            add(Utils.colorize("&b750 &fBlöcke"));
            add(Utils.colorize("&b1000 &fBlöcke"));
            add(Utils.colorize("&b1500 &fBlöcke"));
            add(Utils.colorize("&b2000 &fBlöcke"));
            add(Utils.colorize("&b5000 &fBlöcke"));
        }});

        this.addSetting("Items Bannen", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7ob Spieler vor Beginn des"));
            add(Utils.colorize("&7Spiels Items bannen können"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.STRUCTURE_VOID, false, banningSettings);

        banningSettings.addSetting("Anzahl der Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7wieviele Items ein Spieler &6bannen&7 kann"));
        }}, Material.CACTUS, new ArrayList<Integer>() {{
            for (int i = 1; i <= 9; i++) {
                add(i);
            }
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b1 &fItem"));
            add(Utils.colorize("&b2 &fItems"));
            add(Utils.colorize("&b3 &fItems"));
            add(Utils.colorize("&b4 &fItems"));
            add(Utils.colorize("&b5 &fItems"));
            add(Utils.colorize("&b6 &fItems"));
            add(Utils.colorize("&b7 &fItems"));
            add(Utils.colorize("&b8 &fItems"));
            add(Utils.colorize("&b9 &fItems"));
        }});

        banningSettings.addSetting("Automatisches Bannen", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7Ob Items &6automatisch gebannt"));
            add(Utils.colorize("&7werden sollen, wenn der Spieler"));
            add(Utils.colorize("&7zu lange braucht!"));
        }}, Material.CLOCK, true);

        banningSettings.addSetting("Erster Timeout", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen, wie lange"));
            add(Utils.colorize("&7ein Spieler &6Zeit&7 hat, bis das erste"));
            add(Utils.colorize("&7Item &6automatisch gebannt &7wird!"));
        }}, Material.NAUTILUS_SHELL, new ArrayList<Integer>() {{
            add(15);
            add(20);
            add(25);
            add(30);
            add(45);
            add(60);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b15&f Sekunden"));
            add(Utils.colorize("&b20&f Sekunden"));
            add(Utils.colorize("&b25&f Sekunden"));
            add(Utils.colorize("&b30&f Sekunden"));
            add(Utils.colorize("&b45&f Sekunden"));
            add(Utils.colorize("&b60&f Sekunden"));
        }});

        banningSettings.addSetting("Bann Abstand", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen, wie lange"));
            add(Utils.colorize("&7ein Spieler zwischen dem Item bannen &6Zeit"));
            add(Utils.colorize("&7hat, bevor das nächste &6automatisch gebannt&7 wird"));
        }}, Material.NAUTILUS_SHELL, new ArrayList<Integer>() {{
            add(5);
            add(10);
            add(15);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&b5&f Sekunden"));
            add(Utils.colorize("&b10&f Sekunden"));
            add(Utils.colorize("&b15&f Sekunden"));
        }});

    }

    private void blockChecker() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                int i = 0;

                i += core.Utils.getSettingValueInt(itemSubSettings, "Einfache Items");
                i += core.Utils.getSettingValueInt(itemSubSettings, "Mittlere Items");
                i += core.Utils.getSettingValueInt(itemSubSettings, "Schwere Items");

                for (Setting setting : itemSubSettings.getSettingsList()) {
                    if (setting.getType() == SettingsType.CYCLE) {
                        SettingCycle settingCycle = (SettingCycle) setting;
                        if (i < 27) {
                            settingCycle.blockCycleUP(false);
                            settingCycle.blockCycleDO(false);
                        }else if(i == 27 && settingCycle.getValue() != 27){
                            settingCycle.blockCycleUP(true);
                            settingCycle.blockCycleDO(false);
                        }
                        if(i > 0 && settingCycle.getValue() == 0){
                            settingCycle.blockCycleDO(true);
                        }
                    }
                }
                getSettingbyName("Items").value = i;
            }
        }, 0, 1);
    }
}
