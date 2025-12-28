package Kyo.autofish.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
// XÓA DÒNG NÀY: import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.FileUtils;
// XÓA DÒNG NÀY: import Kyo.autofish.FabricModAutofish;
import Kyo.autofish.platform.Services; // Thêm dòng này

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ConfigManager {

    private Config config;
    private final Gson gson;
    private final File configFile;
    private final Executor executor = Executors.newSingleThreadExecutor();

    // Bỏ tham số FabricModAutofish vì Common không được biết về nó
    public ConfigManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();

        // SỬA ĐỔI QUAN TRỌNG: Lấy đường dẫn config qua Services (Platform Agnostic)
        Path configDir = Services.PLATFORM.getConfigFolder();
        this.configFile = configDir.resolve("autofish.config").toFile();

        readConfig(false);
    }

    public void readConfig(boolean async) {
        Runnable task = () -> {
            try {
                if (configFile.exists()) {
                    String fileContents = FileUtils.readFileToString(configFile, Charset.defaultCharset());
                    config = gson.fromJson(fileContents, Config.class);
                    if (config.enforceConstraints()) writeConfig(true);
                } else {
                    writeNewConfig();
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeNewConfig();
            }
        };

        if (async) executor.execute(task);
        else task.run();
    }

    public void writeNewConfig() {
        config = new Config();
        writeConfig(false);
    }

    public void writeConfig(boolean async) {
        Runnable task = () -> {
            try {
                if (config != null) {
                    String serialized = gson.toJson(config);
                    FileUtils.writeStringToFile(configFile, serialized, Charset.defaultCharset());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        if (async) executor.execute(task);
        else task.run();
    }

    public Config getConfig() {
        return config;
    }
}