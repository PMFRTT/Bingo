package bingo;

import core.settings.PluginSettings;
import core.settings.SubSettings;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import core.Utils;

import java.util.ArrayList;

public class BingoSettings extends PluginSettings {

    Plugin plugin;
    SubSettings singlePlayerSubSettings;
    SubSettings scatterPlayerSubSettings;
    SubSettings teleporterSubSettings;

    public BingoSettings(Plugin plugin) {
        super(plugin.getName() + "-Einstellungen", plugin);
        this.plugin = plugin;
        update();
    }

    public void update() {
        this.getSettingsList().clear();
        addSettings();
    }

    private void addSettings() {
        singlePlayerSubSettings = new SubSettings("Singleplayer-Einstellungen", plugin, this);
        scatterPlayerSubSettings = new SubSettings("Scatter-Einstellungen", plugin, this);
        teleporterSubSettings = new SubSettings("Teleporter-Einstellungen", plugin, this);

        this.addSetting("Items", new ArrayList<String>() {{
            add(Utils.colorize("&7Mit dieser Einstellung kannst du wählen"));
            add(Utils.colorize("&7wie viele &6Items &7jeder Spieler"));
            add(Utils.colorize("&7sammeln muss!"));
        }}, Material.COMPARATOR, new ArrayList<Integer>() {{
            add(9);
            add(18);
            add(27);
        }});

        this.addSetting("Schwierigkeit", new ArrayList<String>() {{
            add(core.Utils.colorize("&7Mit dieser Einstellung kannst du"));
            add(core.Utils.colorize("&6Schwierigkeit &7der zu sammelnden"));
            add(core.Utils.colorize("&7Items wählen!"));
        }}, Material.HAY_BLOCK, new ArrayList<Integer>() {{
            add(0);
            add(1);
            add(2);
        }}, new ArrayList<String>() {{
            add(Utils.colorize("&aLeicht"));
            add(Utils.colorize("&eMittel"));
            add(Utils.colorize("&cSchwer"));
        }}, new ArrayList<Material>() {{
            add(Material.LIME_DYE);
            add(Material.ORANGE_DYE);
            add(Material.RED_DYE);
        }});

        this.addSetting("Keep Inventory", new ArrayList<String>() {{
            add(Utils.colorize("&7Legt fest, ob Spieler nach dem Tod"));
            add(Utils.colorize("&7ihre Items &cverlieren &7oder &abehalten"));
        }}, Material.ENDER_EYE, false);

        this.addSetting("Announce Advancements", new ArrayList<String>() {{
            add(Utils.colorize("&7Legt fest, ob Gegenspieler im"));
            add(Utils.colorize("&7Chat erfahren, dass ein"));
            add(Utils.colorize("&6Advancement erreicht &7wurde"));
        }}, Material.NOTE_BLOCK, false);

        this.addSetting("Scatter Players", new ArrayList<String>() {{
            add(Utils.colorize("&7Legt fest, ob Spieler"));
            add(Utils.colorize("&7zu beginn des Bingos an"));
            add(Utils.colorize("&7eine zufällige Position"));
            add(Utils.colorize("&6teleportiert &7werden"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.ENDER_PEARL, true, scatterPlayerSubSettings);

        scatterPlayerSubSettings.addSetting("Scatter-Größe", new ArrayList<String>() {{
            add(Utils.colorize("&7Hier kannst du einstellen,"));
            add(Utils.colorize("&7wie &6groß der Radius&7 des "));
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

        this.addSetting("Teleporter", new ArrayList<String>() {{
            add(Utils.colorize("&7Ermöglicht dem Spieler sich"));
            add(Utils.colorize("&7entweder einmalig oder mit einem"));
            add(Utils.colorize("&7Countdown zu &ateleportieren!"));
            add(Utils.colorize("&8Shift + Rechtsclick -> Einstellungen"));
        }}, Material.ENDERMAN_SPAWN_EGG, false, teleporterSubSettings);

        singlePlayerSubSettings.addSetting("Start-Zeit", new ArrayList<String>() {{
            add(core.Utils.colorize("&7Hier kannst du einstellen, mit wie"));
            add(core.Utils.colorize("&7viel &6Zeit &7du in dem"));
            add(core.Utils.colorize("&7Singleplayer-Modus startest"));
        }}, Material.CLOCK, new ArrayList<Integer>() {{
            add(60);
            add(120);
            add(180);
            add(240);
            add(300);
            add(360);
            add(420);
            add(480);
            add(540);
            add(600);
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

        //this.addSetting();
    }
}
