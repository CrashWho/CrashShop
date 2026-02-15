package org.crashwho.crashShop.Utils.Inventory;

import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.HumanEntity;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.Utils.ChatFormat;

public class InvManager {


    protected final CrashShop crashShop;
    protected ChestGui inv;

    protected InvManager(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    protected void createInventory() {

        int row = crashShop.getMenu().getInt("rows");
        String title = crashShop.getMenu().getString("title");

        assert title != null;
        inv = new ChestGui(row, title);
        inv.setOnGlobalClick(event -> event.setCancelled(true));

    }

    protected void createShop(String id) {

        int row = crashShop.getShopManager().getShop(id).getData().getInt("rows");
        String title = LegacyComponentSerializer.legacySection().serialize(ChatFormat.format(crashShop.getShopManager().getShop(id).getData().getString("title")));

        inv = new ChestGui(row, title);
        inv.setOnGlobalClick(event -> event.setCancelled(true));

    }

    protected void openGui(HumanEntity player) {
        inv.show(player);
    }

}
