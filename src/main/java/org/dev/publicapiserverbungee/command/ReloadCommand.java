package org.dev.publicapiserverbungee.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.dev.publicapiserverbungee.PublicAPIServer_Bungee;
import org.dev.publicapiserverbungee.config.ConfigManager;
import org.dev.publicapiserverbungee.server.PublicServer;

public class ReloadCommand extends Command {

    private static final String COMMAND = "reload PublicAPIServer";

    public ReloadCommand() {
        super(COMMAND);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            return;
        }
        PublicServer.getInstance(PublicAPIServer_Bungee.getPluginInstance()).stop();
        ConfigManager.reloadConfig();
        PublicServer.getInstance(PublicAPIServer_Bungee.getPluginInstance()).start();
    }
}
