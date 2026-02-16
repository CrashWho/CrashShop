package org.crashwho.crashShop;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.crashwho.crashShop.api.ShopImplement;
import org.crashwho.crashShop.internal.Commands.Sell;
import org.crashwho.crashShop.internal.Commands.Shop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.File.FileCreation;
import org.crashwho.crashShop.internal.Utils.File.FileManager;
import org.crashwho.crashShop.internal.Utils.File.ShopManager;
import org.crashwho.crashShop.api.CrashShopAPI;
import revxrsal.commands.Lamp;
import revxrsal.commands.bukkit.BukkitLamp;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;



public final class CrashShop extends JavaPlugin {

    private static Economy econ = null;
    private static CrashShopAPI api;

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
        setupLamp();
        api = new ShopImplement(this);

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

    private void setupLamp() {
        Lamp<BukkitCommandActor> lamp = BukkitLamp.builder(this)
                .suggestionProviders(provider -> provider.addProvider(String.class, context -> getShopManager().getShopsIds()))
                .build();
        lamp.register(new Shop(this));
        lamp.register(new Sell(this));
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static CrashShopAPI getApi() {
        return api;
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
