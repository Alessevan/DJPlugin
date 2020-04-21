package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.command.CommandSender;

class ReloadCommand {

    @Getter
    private final Executor executor;

    ReloadCommand(final Executor executor){
        this.executor = executor;
    }

    void handle(final CommandSender commandSender){
        if(!commandSender.hasPermission("djstation.reload")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(commandSender);
            return;
        }
        Message.create("&3&lDJ Station &8&l» &7Reload en cours...").sendMessage(commandSender);
        this.getExecutor().getMain().getFileManager().reload();
        Message.create("&3&lDJ Station &8&l» &7Reload terminé.").sendMessage(commandSender);
    }
}
