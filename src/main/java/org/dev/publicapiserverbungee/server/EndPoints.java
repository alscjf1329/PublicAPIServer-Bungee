package org.dev.publicapiserverbungee.server;

import com.sun.net.httpserver.HttpHandler;
import lombok.Getter;
import org.dev.publicapiserverbungee.handler.PlayerStatusHandler;

@Getter
public enum EndPoints {
    PLAYER_STATUS("validateOnlineUserPath", "/player-status", new PlayerStatusHandler());
    private final String name;
    private final String defaultPath;
    private final HttpHandler handler;

    EndPoints(String name, String defaultPath, HttpHandler handler) {
        this.name = name;
        this.defaultPath = defaultPath;
        this.handler = handler;
    }

}
