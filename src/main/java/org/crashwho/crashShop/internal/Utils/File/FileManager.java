package org.crashwho.crashShop.internal.Utils.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.crashwho.crashShop.CrashShop;

import java.io.File;

public class FileManager {

    private FileConfiguration data;
    private final File file;
    private final String name;
    private final CrashShop crashShop;

    public FileManager(CrashShop crashShop, String name) {
        this.crashShop = crashShop;
        this.name = name;
        this.file = new File(crashShop.getDataFolder(), name + ".yml");
        setupData();
    }

    public FileManager(CrashShop crashShop, File file) {
        this.crashShop = crashShop;
        this.name = file.getName().replace(".yml", "");
        this.file = file;
        setupData();
    }

    public void reloadFile() {
        setupData();
    }

    private void setupData() {

        if (!crashShop.getDataFolder().exists())
            crashShop.getDataFolder().mkdirs();

        if (!file.exists())
            crashShop.saveResource(name + ".yml", false);

        data = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration getData() {
        return data;
    }

}
