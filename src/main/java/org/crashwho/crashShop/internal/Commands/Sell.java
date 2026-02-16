package org.crashwho.crashShop.internal.Commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.internal.Utils.Items.SmartItem;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.CommandPlaceholder;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.util.Map;
import java.util.stream.Collectors;

@Command("sellall")
@CommandPermission("crashshop.sellall")
public class Sell {

    final CrashShop crashShop;

    public Sell(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    @CommandPlaceholder
    public void onSell(BukkitCommandActor actor) {

        if (actor.isConsole()) {
            actor.reply(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return;
        }

        Player player = actor.requirePlayer();
        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        Map<Material, Double> sellPrices = guiItemBuilder.getAllShopItems().stream()
                .collect(Collectors.toMap(
                        item -> item.getItem().getType(),
                        SmartItem::getSellPrice,
                        (existing, replacement) -> existing
                ));

        double totalProfit = 0;
        for (ItemStack item : player.getInventory().getContents()) {

            if (item == null || item.getType() == Material.AIR) continue;

            if (sellPrices.containsKey(item.getType())) {
                double pricePerUnit = sellPrices.get(item.getType());
                int amount = item.getAmount();
                totalProfit += pricePerUnit * amount;
                player.getInventory().remove(item);
            }

        }

        if (totalProfit > 0) {
            CrashShop.getEconomy().depositPlayer(player, totalProfit);
            player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.sell-all"),
                    Placeholder.parsed("money", CrashShop.getEconomy().format(totalProfit))));

        } else
            player.sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.no-sell")));


    }

}
