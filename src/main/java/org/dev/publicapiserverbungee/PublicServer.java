package org.dev.publicapiserverbungee;

import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.dev.publicapiserverbungee.views.OutView;

public class PublicServer {
    private static final String CONFIG_FILE_NAME = "config.yml";
    private static final String PATH_OPTION_FORMAT = "server.path.%s";
    private static final String PORT_OPTION_NAME = "server.port";
    private static final String LOG_OPTION = "server.log_flag";
    private static volatile PublicServer instance = null;
    private static final int DEFAULT_PORT = 8080;
    private HttpServer server;

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
        File configFile = new File(plugin.getDataFolder(), CONFIG_FILE_NAME);

        if (!configFile.exists()) {
            try {
                plugin.getDataFolder().mkdirs(); // 데이터 폴더가 없으면 생성
                configFile.createNewFile();
                Configuration config = new Configuration();
                // 여기에 기본 설정 값 설정
                config.set(PORT_OPTION_NAME, DEFAULT_PORT);
                config.set(LOG_OPTION, true);
                Arrays.stream(Handler.values())
                        .forEach(api -> config.set(String.format(PATH_OPTION_FORMAT, api.getName()),
                                api.getDefaultPath()));
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

        int port = config.getInt(PORT_OPTION_NAME, DEFAULT_PORT);
        boolean logFlag = config.getBoolean(LOG_OPTION, true);
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            if (logFlag) {
                OutView.SERVER_START_FORMAT.print(port);
                OutView.API_PATH_LIST_TITLE.print();
            }
            Arrays.stream(Handler.values()).forEach(api -> {
                String apiPath = config.getString(String.format(PATH_OPTION_FORMAT, api.getName()),
                        Handler.PLAYER_STATUS.getDefaultPath());
                if (logFlag) {
                    OutView.API_PATH_FORMAT.print(api.getName(), apiPath);
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