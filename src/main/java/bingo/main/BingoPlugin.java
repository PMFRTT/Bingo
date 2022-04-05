package bingo.main;

import bingo.SideList;
import bingo.commandExecutor.BingoCommandExecutor;
import bingo.commandExecutor.BingoTabCompleter;
import bingo.commandExecutor.ResetCommandExecutor;
import bingo.commandExecutor.TopCommandExecutor;
import bingo.eventhandler.BingoEventhandler;
import bingo.teleporter.Respawner;
import core.core.CoreHandler;
import core.core.CoreMain;
import core.debug.DebugSender;
import core.debug.DebugType;
import core.timer.Timer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class BingoPlugin extends JavaPlugin {

    private static BingoSettings bingoSettings;
    private static Timer timer;
    public static SideList sideList;
    public static Respawner respawner;

    public static boolean paused = true;

    @Override
    public void onEnable() {

        bingoSettings = new BingoSettings(this);

        bingo.Utils utils = new bingo.Utils(this);
        utils.init();

        CoreHandler.setGuestPlugin(this);

        BingoEventhandler bingoEventhandler = new BingoEventhandler(this);
        bingoEventhandler.initialize();

        sideList = new SideList(this);
        respawner = new Respawner(this);

        BingoCommandExecutor bingoCommandExecutor = new BingoCommandExecutor(this);
        TopCommandExecutor topCommandExecutor = new TopCommandExecutor();
        ResetCommandExecutor resetCommandExecutor = new ResetCommandExecutor(this);

        Objects.requireNonNull(getCommand("Bingo")).setExecutor(bingoCommandExecutor);
        Objects.requireNonNull(getCommand("bingo")).setTabCompleter(new BingoTabCompleter());
        Objects.requireNonNull(getCommand("Top")).setExecutor(topCommandExecutor);
        Objects.requireNonNull(getCommand("reset")).setExecutor(resetCommandExecutor);
        Objects.requireNonNull(getCommand("rtp")).setExecutor(bingoCommandExecutor);

        DebugSender.sendDebug(DebugType.PLUGIN, "bingo loaded", "Bingo");
    }

    public void onDisable() {
    }

    public static BingoSettings getBingoSettings() {
        return bingoSettings;
    }

    public static Timer getTimer() {
        return timer;
    }

    public static void setTimer(Timer timer){
        BingoPlugin.timer = timer;
    }

}
