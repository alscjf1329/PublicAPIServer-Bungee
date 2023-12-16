package org.dev.publicapiserverbungee;

import com.sun.net.httpserver.HttpHandler;
import org.dev.publicapiserverbungee.handler.StatusHandler;

public enum API {
    PLAYER_STATUS("/player-status", new StatusHandler());

    private final String defaultPath;
    private final HttpHandler handler;

    API(String defaultPath, HttpHandler handler) {
        this.defaultPath = defaultPath;
        this.handler = handler;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public HttpHandler getHandler() {
        return handler;
    }
}
