package org.crashwho.crashShop.internal.Utils.Inventory;


import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.internal.Utils.Items.SmartItem;

import java.util.List;

public class InvCreation extends InvManager {

    public InvCreation(CrashShop crashShop) {
        super(crashShop);
    }

    public void initMainInventory(Player player) {
        createInventory();
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);

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

            });
            gui.setItem(product.getSlot(), guiItem);

        });

        addFillerItem(gui);
        openGui(player, gui);
    }

    public void initShop(Player player, String shop) {
        createShop(crashShop.getShopManager().getShop(shop).getData().getString("id"));
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);

        guiItemBuilder.setupShopInventoryItems(shop).forEach(product -> {

            GuiItem guiItem = new GuiItem(product.getItem(), event -> shopInventoryEvent(event, product));

            pGui.addItem(guiItem);


        });

        addFillerItem(pGui);
        addNavBar();
        openGui(player, pGui);
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
                if (CrashShop.getEconomy().has(player, product.getBuyPrice() * product.getItem().getMaxStackSize())) {
                    CrashShop.getEconomy().withdrawPlayer(player, product.getBuyPrice() * product.getItem().getMaxStackSize());

                    giveItem(player, product, product.getItem().getMaxStackSize());

                } else
                    player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.no-money")));

            } else {
                if (CrashShop.getEconomy().has(player, product.getBuyPrice())) {

                    CrashShop.getEconomy().withdrawPlayer(player, product.getBuyPrice());
                    giveItem(player, product, 1);

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

    private void addNavBar() {
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        List<SmartItem> items = guiItemBuilder.setupNavItems();



        SmartItem backItem = null;
        SmartItem nextItem = null;
        SmartItem closeItem = null;

        for (SmartItem item : items) {

            if (item.getId().equals("back")) backItem = item;
            if (item.getId().equals("next")) nextItem = item;
            if (item.getId().equals("close")) closeItem = item;

        }

        if (backItem == null || nextItem == null || closeItem == null) return;

        pGui.setItem(backItem.getSlot(), new GuiItem(backItem.getItem(), _ -> pGui.previous()));
        pGui.setItem(nextItem.getSlot(), new GuiItem(nextItem.getItem(), _ -> pGui.next()));
        pGui.setItem(closeItem.getSlot(), new GuiItem(closeItem.getItem(), event -> initMainInventory((Player) event.getWhoClicked())));

    }

    private void giveItem(Player player, SmartItem product, int amount) {

        if (product.isEnableCommand()) {
            String command = product.getGiveCommand().replaceAll("<player>", player.getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            return;
        }

        ItemStack item = product.getItem();
        item.unsetData(DataComponentTypes.CUSTOM_NAME);
        item.unsetData(DataComponentTypes.LORE);
        item.setAmount(amount);

        giveOrDrop(player, item);
        sendShopMessage("buy", player, product, amount);

    }

    private void giveOrDrop(Player player, ItemStack item) {
        var items = player.getInventory().addItem(item);
        if (!items.isEmpty())
            items.values().forEach(i -> player.getWorld().dropItemNaturally(player.getLocation(), i));
    }

    private void addFillerItem(BaseGui gui) {
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);

        if (crashShop.getSettings().getBoolean("general-items.filler.enabled")) {
            GuiItem guiItem = null;
            for (SmartItem item : guiItemBuilder.setupNavItems()) {

                if (item.getId().equals("filler"))
                    guiItem = new GuiItem(item.getItem());

            }
            assert guiItem != null;

            if (gui instanceof Gui)
                gui.getFiller().fill(guiItem);

            if (gui instanceof PaginatedGui)
                gui.getFiller().fillBorder(guiItem);

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
