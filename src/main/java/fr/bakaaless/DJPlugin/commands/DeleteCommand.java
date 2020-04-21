package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.logging.Level;

class DeleteCommand {

    @Getter
    private final Executor executor;

    DeleteCommand(final Executor executor){
        this.executor = executor;
    }

    void handle(final Player player){
        if(!player.hasPermission("djstation.delete")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(!this.getExecutor().getEditPlayerDj().containsKey(player)){
            Message.create("&c&lDJ Station &4&l» &cVous n'êtes pas en train d'éditer une station.")
                    .sendMessage(player);
            return;
        }
        final DjEntity djEntity = this.getExecutor().getEditPlayerDj().get(player);
        if(djEntity.getPlayer().isPresent()){
            Message.create("&c&lDJ Station &4&l» &cUn joueur utilise cette station. Vous devez d'abord l'expulser.")
                    .sendMessage(player);
            return;
        }
        Message.create("&c&lDJ Station &4&l» &7Suppression de la station en cours...")
                .sendMessage(player);
        try {
            djEntity.destroy();
            this.getExecutor().getEditPlayerDj().remove(player);
            this.getExecutor().getMain().getDjEntities().remove(djEntity);
            Message.create("&c&lDJ Station &4&l» &7Suppression achevée.")
                    .sendMessage(player);
        }
        catch (final Exception e){
            Message.create("&c&lDJ Station &4&l» &cUne erreur s'est produite durant la suppression !")
                    .sendMessage(player);
            this.getExecutor().getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c&lDJ Station &4&l» &cUne erreur s'est produite durant une sauvegarde : " + djEntity.toString()), e);
        }
    }
}
