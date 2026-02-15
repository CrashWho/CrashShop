package org.crashwho.crashShop.api;

import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.Utils.Items.SmartItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrashShopAPI {

    private final CrashShop crashShop;

    public CrashShopAPI(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    public List<ShopItem> getItemList() {
        List<SmartItem> smartItemList = new GuiItemBuilder(crashShop).getAllShopItems();
        List<ShopItem> smartItems = new ArrayList<>(smartItemList);

        return Collections.unmodifiableList(smartItems);
    }

}
