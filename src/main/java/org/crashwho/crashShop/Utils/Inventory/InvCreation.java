package org.crashwho.crashShop.Utils.Inventory;


import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.PagingButtons;
import com.github.stefvanschie.inventoryframework.pane.util.Slot;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.Utils.ChatFormat;
import org.crashwho.crashShop.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.Utils.Items.SmartItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvCreation extends InvManager {

    public InvCreation(CrashShop crashShop) {
        super(crashShop);
    }

    public void initMainInventory(Player player) {
        createInventory();
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        StaticPane pane = new StaticPane(0, 0, 9, inv.getRows());

        guiItemBuilder.setupMainInventoryItems().forEach(product -> {
            GuiItem guiItem = new GuiItem(product.getItem(), event -> {

                if (event.getClick().isRightClick())
                    return;

               String shop = product.getId();

               if (crashShop.getShopManager().getShop(product.getId()) == null) {
                   player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.shop-not-found")));
                   return;
               }

               initShop(player, shop);
               openGui(player);

            });
            pane.addItem(guiItem, product.getX(), product.getY());
        });

        addFillerItem();
        inv.addPane(pane);
        openGui(player);
    }

    public void initShop(Player player, String shop) {
        createShop(crashShop.getShopManager().getShop(shop).getData().getString("id"));
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        PaginatedPane paginatedPane = new PaginatedPane(0, 0, 9, inv.getRows());
        Map<Integer, StaticPane> tempPages = new HashMap<>();

        guiItemBuilder.setupShopInventoryItems(shop).forEach(product -> {

            StaticPane pagePane = tempPages.computeIfAbsent(product.getPage(), p -> {
                StaticPane newPane = new StaticPane(0, 0, 9, inv.getRows());
                paginatedPane.addPane(p, newPane);
                return newPane;
            });

            GuiItem guiItem = new GuiItem(product.getItem(), event -> shopInventoryEvent(event, product));
            pagePane.addItem(guiItem, product.getX(), product.getY());


        });

        addNavBar(paginatedPane);
        addFillerItem();
        inv.addPane(paginatedPane);
        addNavBar(paginatedPane);
        openGui(player);
    }

    private void shopInventoryEvent(InventoryClickEvent event, SmartItem product) {

        if (event.isLeftClick()) {
            buyItems(event, product);

        } else if(event.isRightClick())
            sellItems(event, product);

    }

    private void buyItems(InventoryClickEvent event, SmartItem product) {
        Player player = (Player) event.getWhoClicked();

        if (product.canBuy()) {

            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.full-inv")));
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                return;
            }

            if (event.isShiftClick()) {
                if (CrashShop.getEconomy().has(player, product.getBuyPrice() * 64)) {
                    CrashShop.getEconomy().withdrawPlayer(player, product.getBuyPrice() * 64);
                    player.give(ItemStack.of(product.getItem().getType(), 64));
                    sendShopMessage("buy", player, product, 64);

                } else
                    player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.no-money")));

            } else {
                if (CrashShop.getEconomy().has(player, product.getBuyPrice())) {

                    CrashShop.getEconomy().withdrawPlayer(player, product.getBuyPrice());
                    player.give(ItemStack.of(product.getItem().getType()));
                    sendShopMessage("buy", player, product, 1);

                } else
                    player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.no-money")));
            }

        } else
            player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.cant-buy")));

    }

    private void sellItems(InventoryClickEvent event, SmartItem product) {
        Player player = (Player) event.getWhoClicked();

        if (product.canSell()) {
            if (player.getInventory().contains(product.getItem().getType())) {
                if (event.isShiftClick()) {

                    int amount = 0;
                    for (ItemStack item : player.getInventory().getContents()) {

                        if (item == null || item.getType().equals(Material.AIR)) continue;

                        if (item.getType() ==  product.getItem().getType())
                            amount += item.getAmount();
                    }
                    CrashShop.getEconomy().depositPlayer(player, product.getSellPrice() * amount);
                    player.getInventory().remove(product.getItem().getType());
                    sendShopMessage("sell", player, product, amount);

                } else {

                    for (ItemStack item : player.getInventory().getContents()) {

                        if (item == null || item.getType().equals(Material.AIR)) continue;

                        if (item.getType() ==  product.getItem().getType()) {
                            item.setAmount(item.getAmount() - 1);
                            break;
                        }

                    }
                    CrashShop.getEconomy().depositPlayer(player, product.getSellPrice());
                    sendShopMessage("sell", player, product, 1);

                }
            } else
                player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.no-item")));

        } else
            player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.cant-sell")));

    }

    private void addNavBar(PaginatedPane paginatedPane) {
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        List<SmartItem> items = guiItemBuilder.setupNavItems();



        SmartItem backItem = null;
        SmartItem nextItem = null;
        SmartItem main = null;

        for (SmartItem item : items) {

            if (item.getId().equals("back")) backItem = item;
            if (item.getId().equals("next")) nextItem = item;
            if (item.getId().equals("main")) main = item;

        }

        if (backItem == null || nextItem == null) return;

        int Y = inv.getRows() - 1;

        PagingButtons pagingButtons = new PagingButtons(Slot.fromXY(0, Y), 9, paginatedPane);
        StaticPane staticPane = new StaticPane(0, 0, 9, inv.getRows());
        pagingButtons.setBackwardButton(new GuiItem(backItem.getItem()));
        pagingButtons.setForwardButton(new GuiItem(nextItem.getItem()));
        staticPane.addItem(new GuiItem(main.getItem(), event -> {

            initMainInventory((Player) event.getWhoClicked());

        }), main.getX(), main.getY());


        pagingButtons.setButtonsAlwaysVisible(true);
        inv.addPane(pagingButtons);
        inv.addPane(staticPane);

    }

    private void addFillerItem() {
        OutlinePane pane = new OutlinePane(0, 0, 9, inv.getRows(), Pane.Priority.LOWEST);
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);

        if (crashShop.getSettings().getBoolean("general-items.filler.enabled")) {
            GuiItem guiItem = null;
            for (SmartItem item : guiItemBuilder.setupNavItems()) {

                if (item.getId().equals("filler"))
                    guiItem = new GuiItem(item.getItem());

            }
            pane.addItem(guiItem);
            pane.setRepeat(true);
            inv.addPane(pane);
        }


    }

    private void sendShopMessage(String string, Player player, SmartItem product, int amount) {

        String money = "";

        if (string.equals("buy"))
            money = CrashShop.getEconomy().format(product.getBuyPrice() * amount);
        else if (string.equals("sell"))
            money = CrashShop.getEconomy().format(product.getSellPrice() * amount);


        player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages." + string),
                Formatter.number("amount", amount),
                Placeholder.component("item", product.getItem().effectiveName()),
                Placeholder.parsed("money", money)));

    }

}
