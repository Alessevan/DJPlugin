package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

class SaveCommand {

    @Getter
    private final Executor executor;

    SaveCommand(final Executor executor) {
        this.executor = executor;
    }

    void handle(final Player player){
        if(!player.hasPermission("djstation.save")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(!this.getExecutor().getEditPlayerDj().containsKey(player)){
            Message.create("&c&lDJ Station &4&l» &cVous devez d'abord éditer une station.")
                    .sendMessage(player);
            return;
        }
        Message.create("&3&lDJ Station &8&l» &7Sauvegarde en cours...").sendMessage(player);
        final DjEntity djEntity = this.getExecutor().getEditPlayerDj().get(player);
        try {
            djEntity.save();
            this.getExecutor().getEditPlayerDj().remove(player);
            Message.create("&3&lDJ Station &8&l» &7Sauvegarde terminée.").sendMessage(player);
        }
        catch (final Exception e){
            Message.create("&c&lDJ Station &4&l» &cUne erreur s'est produite durant la sauvegarde !")
                    .sendMessage(player);
            this.getExecutor().getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c&lDJ Station &4&l» &cUne erreur s'est produite durant une sauvegarde : " + djEntity.toString()), e);
        }
    }
}
