package org.crashwho.crashShop.Utils.Items;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.Utils.ChatFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    protected ItemStack createGenericItem(Material material, int amount, String displayName, List<String> lore, float customModelData, NamespacedKey itemModel, boolean glint, Map<String, Integer> enchantments) {

        ItemStack item = new ItemStack(material, amount);

        if (displayName != null)
            item.setData(DataComponentTypes.CUSTOM_NAME, ChatFormat.format("<italic:false>" + displayName));

        if (lore != null) {
            List<Component> loreComponents = new ArrayList<>();
            lore.forEach(l -> loreComponents.add(ChatFormat.format("<italic:false>" + l)));
            item.setData(DataComponentTypes.LORE, ItemLore.lore(loreComponents));
        }

        if (customModelData != 0) {
            var builder = CustomModelData.customModelData();
            if (builder != null)
                item.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                        .addFloat(customModelData).build());
        }

        if (itemModel != null)
            item.setData(DataComponentTypes.ITEM_MODEL, itemModel);

        item.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, glint);

        if (enchantments != null) {
            Map<Enchantment, Integer> enchantmentMap = new HashMap<>();
            for (Map.Entry<String, Integer> entry : enchantments.entrySet()) {
                NamespacedKey key = NamespacedKey.fromString(entry.getKey());

                if (key != null) {
                    Registry<Enchantment> enchantmentRegistry = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT);
                    Enchantment enchantment = enchantmentRegistry.get(key);
                    enchantmentMap.put(enchantment, entry.getValue());
                }
            }
            item.setData(DataComponentTypes.ENCHANTMENTS, ItemEnchantments.itemEnchantments(enchantmentMap));

        }
        return item;
    }
}