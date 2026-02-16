package org.crashwho.crashShop.internal.Utils.Items;

import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.api.ShopItem;


public class SmartItem implements ShopItem {

    private final String id;
    private final ItemStack item;
    private final int slot;
    private final int page;
    private final double buyPrice;
    private final double sellPrice;


    public SmartItem(String id, ItemStack item, int slot, int page, Double buyPrice, Double sellPrice) {
        this.id = id;
        this.item = item;
        this.slot = slot;
        this.page = page;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
    }

    @Override
    public boolean canBuy() {
        return buyPrice >= 0;
    }

    @Override
    public boolean canSell() {
        return sellPrice >= 0;
    }

    public String getId() {
        return id;
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    public int getPage() {
        return page;
    }

    @Override
    public double getBuyPrice() {
        return buyPrice;
    }

    @Override
    public double getSellPrice() {
        return sellPrice;
    }

    public int getX() {
        return  slot % 9;
    }

    public int getY() {
        return slot / 9;
    }
}
