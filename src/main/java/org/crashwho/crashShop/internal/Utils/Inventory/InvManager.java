package org.crashwho.crashShop.internal.Utils.Inventory;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.PaginatedGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.HumanEntity;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;

public class InvManager {


    protected final CrashShop crashShop;
    protected Gui gui;
    protected PaginatedGui pGui;

    protected InvManager(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    protected void createInventory() {

        int rows = crashShop.getMenu().getInt("rows");
        Component title = ChatFormat.format(crashShop.getMenu().getString("title"));

        gui = Gui.gui().title(title).rows(rows).create();
        gui.setDefaultClickAction(event -> event.setCancelled(true));

    }

    protected void createShop(String id) {

        int rows = crashShop.getShopManager().getShop(id).getData().getInt("rows");
        int pageSize = crashShop.getShopManager().getShop(id).getData().getInt("pageSize");
        Component title = ChatFormat.format(crashShop.getShopManager().getShop(id).getData().getString("title"));

        pGui = Gui.paginated().title(title).rows(rows).pageSize(pageSize).create();
        pGui.setDefaultClickAction(event -> event.setCancelled(true));

    }

    protected void openGui(HumanEntity player, BaseGui gui) {
        gui.open(player);
    }

}
