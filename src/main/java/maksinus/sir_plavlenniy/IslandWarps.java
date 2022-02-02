package maksinus.sir_plavlenniy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class IslandWarps {

    private final File file;
    private final FileConfiguration config;

    public IslandWarps(String name) {
        file = new File(Sir_plavlenniy.instance.getDataFolder(), name);
        try {
            if (!file.exists() && !file.createNewFile()) throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать конфиг-файл!", e);
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить конфиг-файл", e);
        }
    }
}
