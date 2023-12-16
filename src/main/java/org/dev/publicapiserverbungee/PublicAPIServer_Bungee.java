package org.dev.publicapiserverbungee;

import net.md_5.bungee.api.plugin.Plugin;

public final class PublicAPIServer_Bungee extends Plugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        new Thread(() -> PublicServer.getInstance().start(this)).start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PublicServer.getInstance().stop();
    }
}
