package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.Optional;

class LeaveCommand {

    @Getter
    private final Executor executor;

    LeaveCommand(final Executor executor) {
        this.executor = executor;
    }

    void handle(final Player player){
        if(!player.hasPermission("djstation.use")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        final Optional<DjEntity> djEntityOptional = this.getExecutor().getMain().getDjEntity(player);
        if(!djEntityOptional.isPresent()){
            Message.create("&c&lDJ Station &4&l» &cVous n'utilisez pas de station.")
                    .sendMessage(player);
            return;
        }
        djEntityOptional.get().removePlayer();
        Message.create("&3&lDJ Station &8&l» &7Vous avez libéré une station.")
                .sendMessage(player);
    }
}
