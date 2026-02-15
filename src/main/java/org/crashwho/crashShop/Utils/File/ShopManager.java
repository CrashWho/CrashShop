package org.crashwho.crashShop.Utils.File;

import org.crashwho.crashShop.CrashShop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopManager {

    private final CrashShop crashShop;
    private final Map<String, FileManager> shopFiles = new HashMap<>();

    public ShopManager(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    public void loadShops() {
        shopFiles.clear();
        File folder = new File(crashShop.getDataFolder(), "shops");

        if (!folder.exists()) {
            folder.mkdirs();
            crashShop.saveResource("shops/farming.yml", false);
            crashShop.saveResource("shops/food.yml", false);
            crashShop.saveResource("shops/ore.yml", false);
            crashShop.saveResource("shops/dye.yml", false);
            crashShop.saveResource("shops/mobs.yml", false);
        }

        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".yml")) {
                    FileManager shopConfig = new FileManager(crashShop, file);
                    shopFiles.put(shopConfig.getData().getString("id", file.getName().replace(".yml", "")) , shopConfig);
                }
            }
        }

    }

    public FileManager getShop(String id) {
        return shopFiles.get(id);
    }

    public List<String> getShopsIds() {
        return new ArrayList<>(shopFiles.keySet());
    }

    public void reloadShops() {
        loadShops();
    }
}
