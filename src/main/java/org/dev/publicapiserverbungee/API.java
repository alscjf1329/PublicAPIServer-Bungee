package org.dev.publicapiserverbungee;

import com.sun.net.httpserver.HttpHandler;
import org.dev.publicapiserverbungee.api.PlayerStatusAPI;

public enum API {
    PLAYER_STATUS("validateOnlineUserPath", "/player-status", new PlayerStatusAPI());
    private final String name;
    private final String defaultPath;
    private final HttpHandler handler;

    API(String name, String defaultPath, HttpHandler handler) {
        this.name = name;
        this.defaultPath = defaultPath;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public HttpHandler getHandler() {
        return handler;
    }
}
