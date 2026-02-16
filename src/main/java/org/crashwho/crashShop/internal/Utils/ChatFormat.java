package org.crashwho.crashShop.internal.Utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.crashwho.crashShop.CrashShop;

import java.io.File;

public class ChatFormat {

    private static String prefix = "";

    public static void init(CrashShop plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        prefix = config.getString("messages.prefix", "");
    }

    public static Component format(String message) {
        return MiniMessage.miniMessage().deserialize(message);
    }

    public static Component prefixFormat(String message) {
        return MiniMessage.miniMessage().deserialize(prefix + message);
    }

    public static Component prefixFormat(String message, TagResolver... resolver) {
        return MiniMessage.miniMessage().deserialize(prefix + message, resolver);
    }

}
