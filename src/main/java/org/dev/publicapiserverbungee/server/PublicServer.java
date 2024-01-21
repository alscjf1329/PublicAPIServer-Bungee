package org.dev.publicapiserverbungee.server;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.dev.publicapiserverbungee.PublicAPIServer_Bungee;
import org.dev.publicapiserverbungee.config.ConfigManager;
import org.dev.publicapiserverbungee.views.ConfigOutView;

public class PublicServer {

    private Plugin plugin;

    private static volatile PublicServer instance = null;
    private static final int DEFAULT_PORT = 15000;
    private HttpServer server;

    private PublicServer(Plugin plugin) {
        this.plugin = plugin;
    }

    public static PublicServer getInstance(Plugin plugin) {
        if (instance == null) {
            synchronized (PublicServer.class) {
                if (instance == null) {
                    instance = new PublicServer(plugin);
                }
            }
        }
        return instance;
    }

    public void start() {
        Configuration config = ConfigManager.getInstance();

        int port = config.getInt(ConfigManager.PORT_OPTION_NAME, DEFAULT_PORT);
        boolean logFlag = config.getBoolean(ConfigManager.LOG_OPTION, true);

        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            if (logFlag) {
                ConfigOutView.SERVER_START_FORMAT.print(port);
                ConfigOutView.API_PATH_LIST_TITLE.print();
            }
            Arrays.stream(EndPoints.values()).forEach(api -> {
                String apiPath = config.getString(
                    String.format(ConfigManager.PATH_OPTION_FORMAT, api.getName()),
                    api.getDefaultPath());
                if (logFlag) {
                    ConfigOutView.API_PATH_FORMAT.print(api.getName(), apiPath);
                }
                server.createContext(apiPath, api.getHandler());
            });
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