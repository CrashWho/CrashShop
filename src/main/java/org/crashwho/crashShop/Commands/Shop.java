package org.crashwho.crashShop.Commands;

import org.bukkit.entity.Player;
import org.crashwho.crashShop.CrashShop;
import org.crashwho.crashShop.Utils.ChatFormat;
import org.crashwho.crashShop.Utils.Inventory.InvCreation;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.actor.BukkitCommandActor;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"shop"})
@CommandPermission("crashshop.use")
public class Shop {

    private final CrashShop crashShop;

    public Shop(CrashShop crashShop) {
        this.crashShop = crashShop;
    }

    @CommandPlaceholder
    @CommandPermission("crashshop.use")
    public void onUse(BukkitCommandActor actor) {

        if (actor.isConsole()) {
            actor.reply(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return;
        }

        Player player = actor.requirePlayer();
        InvCreation inv = new InvCreation(crashShop);
        inv.initMainInventory(player);

    }

    @Subcommand("open")
    @CommandPermission("crashshop.use.specific")
    public void onOpen(BukkitCommandActor actor, String shop) {

        if (actor.isConsole()) {
            actor.reply(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.console")));
            return;
        }

        Player player = actor.requirePlayer();
        InvCreation inv = new InvCreation(crashShop);
        inv.initShop(player, shop);

    }

    @Subcommand("reload")
    @CommandPermission("crashshop.reload")
    public void onReload(BukkitCommandActor actor) {
        crashShop.reloadFiles();
        ChatFormat.init(crashShop);
        crashShop.getShopManager().reloadShops();

        actor.reply(ChatFormat.prefixFormat(crashShop.getMessages().getString("messages.reload")));
    }

}
