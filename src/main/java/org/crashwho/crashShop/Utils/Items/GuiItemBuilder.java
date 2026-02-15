package org.crashwho.crashShop.Utils.Items;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.CrashShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiItemBuilder extends ItemBuilder {

    private final CrashShop crashShop;

    public GuiItemBuilder(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    public List<SmartItem> setupNavItems() {
        ConfigurationSection section = crashShop.getSettings().getConfigurationSection("general-items");
        return loadItemsFromSection(section);
    }

    public List<SmartItem> setupMainInventoryItems() {
        ConfigurationSection section = crashShop.getMenu().getConfigurationSection("items");
        return loadItemsFromSection(section);
    }

    public List<SmartItem> setupShopInventoryItems(String id) {
        ConfigurationSection section = crashShop.getShopManager().getShop(id).getData().getConfigurationSection("items");
        return  loadItemsFromSection(section);
    }

    public List<SmartItem> getAllShopItems() {

        return crashShop.getShopManager().getShopsIds().stream()
                .flatMap(id -> setupShopInventoryItems(id).stream())
                .distinct()
                .toList();
    }

    private List<SmartItem> loadItemsFromSection(ConfigurationSection section) {
        List<SmartItem> items = new ArrayList<>();

        if (section == null) return items;

        for (String key : section.getKeys(false)) {

            try {
                Material material = Material.getMaterial(section.getString(key + ".material"));
                int amount = section.getInt(key + ".amount", 1);
                String displayName = section.getString(key + ".displayname");
                List<String> lore = section.getStringList(key + ".lore");
                float customModelData = (float) section.getDouble(key + ".custom-model-data");

                String namespaceKey = section.getString(key + ".namespace-key");

                NamespacedKey nsKey = (namespaceKey != null) ? NamespacedKey.fromString(namespaceKey) : null;
                boolean glow = section.getBoolean(key + ".glow", false);

                ConfigurationSection enchantmentSection = section.getConfigurationSection(key + ".enchantments");

                Map<String, Integer> enchantmentMap = new HashMap<>();

                if (enchantmentSection != null) {
                    for (String name: enchantmentSection.getKeys(false)) {
                        int level = enchantmentSection.getInt(name);
                        enchantmentMap.put(name, level);
                    }
                }


                ItemStack item = createGenericItem(material, amount, displayName, lore, customModelData, nsKey, glow, enchantmentMap);
                int slot = section.getInt(key + ".slot", 0);
                int page = section.getInt(key + ".page");
                double buyPrice = section.getDouble(key + ".buy-price", -1.0);
                double sellPrice = section.getDouble(key + ".sell-price", -1.0);


                items.add(new SmartItem(key, item, slot, page, buyPrice, sellPrice));

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return items;
    }

}
