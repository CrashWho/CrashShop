package org.crashwho.crashShop;

import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.crashwho.crashShop.api.ShopImplement;
import org.crashwho.crashShop.internal.Commands.Sell;
import org.crashwho.crashShop.internal.Commands.Shop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.File.FileCreation;
import org.crashwho.crashShop.internal.Utils.File.FileManager;
import org.crashwho.crashShop.internal.Utils.File.ShopManager;
import org.crashwho.crashShop.api.CrashShopAPI;



public final class CrashShop extends JavaPlugin {

    private static Economy econ = null;

    private FileManager config;
    private FileManager messages;
    private FileManager menu;
    private FileCreation fileCreation;
    private ShopManager shopManager;

    @Override
    public void onEnable() {
        setupFiles();
        ChatFormat.init(this);
        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        setupCmd();
        setupAPI();

    }

    private void setupAPI() {
        getServer().getServicesManager().register(CrashShopAPI.class, new ShopImplement(this), this, ServicePriority.Normal);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void setupFiles() {
        config = new FileManager(this, "config");
        messages = new FileManager(this, "messages");
        menu = new FileManager(this, "menu");
        fileCreation = new FileCreation(config, messages, menu);
        shopManager = new ShopManager(this);
        shopManager.loadShops();
    }

    private void setupCmd() {
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, e -> {
            e.registrar().register(new Shop(this).shopCommand());
            e.registrar().register(new Sell(this).sellAllCommand());
        });
    }

    public static Economy getEconomy() {
        return econ;
    }

    public FileConfiguration getSettings() {
        return config.getData();
    }

    public FileConfiguration getMessages() {
        return messages.getData();
    }

    public FileConfiguration getMenu() {
        return menu.getData();
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public void reloadFiles() {
        fileCreation.reloadAll();
    }

}
