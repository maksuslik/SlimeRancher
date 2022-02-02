package maksinus.sir_plavlenniy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class Players {
    public final File pfile;
    private final FileConfiguration config;

    public Players(String name) {
        pfile = new File(Sir_plavlenniy.instance.getDataFolder(), name);
        try {
            if (!pfile.exists() && !pfile.createNewFile()) throw new IOException();
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать конфиг-файл!", e);
        }
        config = YamlConfiguration.loadConfiguration(pfile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(pfile);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить конфиг-файл", e);
        }
    }

    public String getLastLine(final File file)
    {
        String line = null;
        String tmp = null;
        BufferedReader in = null;



        try
        {
            in = new BufferedReader(new FileReader(file));

            while ((tmp = in.readLine()) != null)
            {
                line = tmp;
            }
        }
        catch (FileNotFoundException exception)
        {
            System.out.println("bebra " + exception);
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }

        return line;
    }
}
