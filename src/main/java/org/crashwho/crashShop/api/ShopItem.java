package org.crashwho.crashShop.api;

import org.bukkit.inventory.ItemStack;

public interface ShopItem {

    boolean canBuy();

    boolean canSell();

    ItemStack getItem();

    double getSellPrice();

    double getBuyPrice();


}
