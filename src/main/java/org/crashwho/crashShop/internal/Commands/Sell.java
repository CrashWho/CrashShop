package org.crashwho.crashShop.internal.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.Items.GuiItemBuilder;
import org.crashwho.crashShop.internal.Utils.Items.SmartItem;

import java.util.Map;
import java.util.stream.Collectors;

public class Sell {

    final CrashShop crashShop;

    public Sell(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    public LiteralCommandNode<CommandSourceStack> sellAllCommand() {
        return Commands.literal("sellall")
                .requires(sender -> sender.getSender().hasPermission("crashshop.sellall"))
                .executes(this::onSell)
                .build();
    }

    private int onSell(CommandContext<CommandSourceStack> ctx) {

        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return Command.SINGLE_SUCCESS;
        }

        GuiItemBuilder guiItemBuilder = new GuiItemBuilder(crashShop);
        Map<Material, Double> sellPrices = guiItemBuilder.getAllShopItems().stream()
                .collect(Collectors.toMap(
                        item ->  {

                            if (item.canSell())
                                return item.getItem().getType();
                            else
                                return null;

                        }, SmartItem::getSellPrice, (existing, replacement) -> existing)
                );

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


        return Command.SINGLE_SUCCESS;
    }

}
