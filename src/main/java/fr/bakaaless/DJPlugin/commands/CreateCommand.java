package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.entity.Player;

class CreateCommand {

    @Getter
    private final Executor executor;

    CreateCommand(final Executor executor){
        this.executor = executor;
    }

    void handle(final Player player){
        if(!player.hasPermission("djstation.create")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(this.getExecutor().getEditPlayerDj().containsKey(player)){
            Message.create("&c&lDJ Station &4&l» &cVous êtes déjà en train d'éditer une station.")
                    .sendMessage(player);
            return;
        }
        Message.create("&3&lDJ Station &8&l» &7Création d'une station...")
                .sendMessage(player);
        final DjEntity djEntity = new DjEntity(this.getExecutor().getMain().getDjEntities().size());
        this.getExecutor().getMain().getDjEntities().add(djEntity);
        Message.create("&3&lDJ Station &8&l» &7Création terminée !")
                .sendMessage(player);
        this.getExecutor().getEditPlayerDj().put(player, djEntity);
        djEntity.newDj(player);
        Message.create("&3&lDJ Station &8&l» &7Vous éditez maintenant la station numéro &b&l")
                .append(String.valueOf(djEntity.getId()))
                .append("&7.")
                .sendMessage(player);
    }
}
