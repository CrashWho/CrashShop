package org.crashwho.crashShop.internal.Commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.internal.Utils.ChatFormat;
import org.crashwho.crashShop.internal.Utils.Inventory.InvCreation;

import java.util.concurrent.CompletableFuture;


public class Shop {

    private final CrashShop crashShop;

    public Shop(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    public LiteralCommandNode<CommandSourceStack> shopCommand() {
        return Commands.literal("shop")
                .requires(sender -> sender.getSender().hasPermission("crashshop.use"))
                .executes(this::onUse)

                .then(Commands.literal("reload")
                        .requires(sender -> sender.getSender().hasPermission("crashshop.use"))
                        .executes(this::onReload))

                .then(Commands.literal("open")
                        .requires(sender -> sender.getSender().hasPermission("crashshop.use.specific"))
                        .then(Commands.argument("shop", StringArgumentType.word())
                                .suggests(this::onOpenSuggest)
                                .executes(this::onOpen)))

                .build();
    }


    private int onUse(CommandContext<CommandSourceStack> ctx) {

        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return Command.SINGLE_SUCCESS;
        }

        InvCreation inv = new InvCreation(crashShop);
        inv.initMainInventory(player);
        return Command.SINGLE_SUCCESS;
    }

    private CompletableFuture<Suggestions>  onOpenSuggest(CommandContext<CommandSourceStack> ctx, final SuggestionsBuilder builder) {
        crashShop.getShopManager().getShopsIds().forEach(builder::suggest);
        return builder.buildFuture();
    }

    public int onOpen(CommandContext<CommandSourceStack> ctx) {

        if (!(ctx.getSource().getExecutor() instanceof Player player)) {
            ctx.getSource().getSender().sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return Command.SINGLE_SUCCESS;
        }

        String shop = StringArgumentType.getString(ctx, "shop");
        InvCreation inv = new InvCreation(crashShop);
        inv.initShop(player, shop);

        return Command.SINGLE_SUCCESS;
    }

    private int onReload(CommandContext<CommandSourceStack> ctx) {
        crashShop.reloadFiles();
        ChatFormat.init(crashShop);
        crashShop.getShopManager().reloadShops();

        ctx.getSource().getSender().sendMessage(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.reload")));
        return Command.SINGLE_SUCCESS;
    }

}
