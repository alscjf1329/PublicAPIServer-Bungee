package org.dev.publicapiserverbungee;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.dev.publicapiserverbungee.handler.StatusHandler;

public class PublicServer {
    private static volatile PublicServer instance = null;
    private HttpServer server;
    private static final int DEFAULT_PORT = 8080;

    private PublicServer() {
    }

    public static PublicServer getInstance() {
        if (instance == null) {
            synchronized (PublicServer.class) {
                if (instance == null) {
                    instance = new PublicServer();
                }
            }
        }
        return instance;
    }

    public void start(Plugin plugin) {
        File configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs(); // 데이터 폴더가 없으면 생성
                configFile.createNewFile();
                Configuration config = new Configuration();
                // 여기에 기본 설정 값 설정
                config.set("server.port", DEFAULT_PORT);
                config.set("server.contextPath", API.PLAYER_STATUS.getDefaultPath());
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configFile);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        Configuration config;
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int port = config.getInt("server.port", DEFAULT_PORT);
        String contextPath = config.getString("server.contextPath", API.PLAYER_STATUS.getDefaultPath());

        System.out.println("Starting Public API Server on port " + port + " with context path " + contextPath);
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext(contextPath, new StatusHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }
}