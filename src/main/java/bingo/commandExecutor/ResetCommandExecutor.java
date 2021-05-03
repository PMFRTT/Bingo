package bingo.commandExecutor;

import bingo.main.BingoPlugin;
import core.core.CoreResetServer;
import core.debug.DebugSender;
import core.debug.DebugType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ResetCommandExecutor implements CommandExecutor {

    private BingoPlugin bingo;

    public ResetCommandExecutor(BingoPlugin bingo) {
        this.bingo = bingo;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reset")) {
            DebugSender.sendDebug(DebugType.SERVER, "resetting");
            CoreResetServer.resetServer(bingo.getName(), true);
            return true;
        }
        return false;
    }
}
