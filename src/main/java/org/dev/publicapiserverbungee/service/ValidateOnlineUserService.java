package org.dev.publicapiserverbungee.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ValidateOnlineUserService {
    public static boolean validate(String nickname) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(nickname);
        return player != null && player.isConnected();
    }

    public static Map<String, Boolean> validate(List<String> nicknames) {
        return nicknames.stream()
                .collect(Collectors.toMap(
                        nickname -> nickname,
                        ValidateOnlineUserService::validate
                ));
    }
}
