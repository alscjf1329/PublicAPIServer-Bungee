package org.dev.publicapiserverbungee;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import org.dev.publicapiserverbungee.command.ReloadCommand;
import org.dev.publicapiserverbungee.server.PublicServer;

public final class PublicAPIServer_Bungee extends Plugin {

    @Getter
    private static Plugin pluginInstance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        pluginInstance = this;
        new Thread(() -> PublicServer.getInstance(this).start()).start();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new ReloadCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PublicServer.getInstance(this).stop();
    }

}
