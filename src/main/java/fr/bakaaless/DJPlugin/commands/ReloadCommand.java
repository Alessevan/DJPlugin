package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

class ReloadCommand {

    @Getter
    private final Executor executor;

    ReloadCommand(final Executor executor){
        this.executor = executor;
    }

    void handle(final CommandSender commandSender) {
        if (!commandSender.hasPermission("djstation.reload")) {
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(commandSender);
            return;
        }
        Message.create("&3&lDJ Station &8&l» &7Reload en cours...").sendMessage(commandSender);
        try {
            this.getExecutor().getMain().getFileManager().reload();
        } catch (final Exception e) {
            this.getExecutor().getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve data files."));
            this.getExecutor().getMain().getServer().getPluginManager().disablePlugin(this.getExecutor().getMain());
            return;
        }
        Message.create("&3&lDJ Station &8&l» &7Reload terminé.").sendMessage(commandSender);
    }
}
