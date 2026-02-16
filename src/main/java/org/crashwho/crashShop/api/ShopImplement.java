package org.crashwho.crashShop.api;

import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.internal.Utils.Items.SmartItem;

import java.util.ArrayList;
import java.util.List;

public class ShopImplement implements CrashShopAPI{

    final CrashShop crashShop;

    public ShopImplement(CrashShop crashShop) {
        this.crashShop = crashShop;
    }


    @Override
    public List<ShopItem> getShopItems() {
        List<SmartItem> smartItems = new GuiItemBuilder(crashShop).getAllShopItems();

        return new ArrayList<>(smartItems);
    }
}
