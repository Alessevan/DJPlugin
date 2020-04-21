package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

class KickCommand {

    @Getter
    private final Executor executor;

    KickCommand(final Executor executor) {
        this.executor = executor;
    }

    void handle(final Player player, final String... args){
        if(!player.hasPermission("djstation.edit")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(StringUtils.isNumeric(args[0])){
            final Optional<DjEntity> djEntityOptional = this.getExecutor().getMain().getDjEntity(Integer.parseInt(args[0]));
            if(djEntityOptional.isPresent()){
                if(djEntityOptional.get().getPlayer().isPresent()){
                    final Player kicked = djEntityOptional.get().getPlayer().get();
                    if(kicked.equals(player)){
                        djEntityOptional.get().removePlayer();
                        Message.create("&3&lDJ Station &8&l» &7Vous avez libéré une station.")
                                .sendMessage(kicked);
                        return;
                    }
                    djEntityOptional.get().removePlayer();
                    Message.create("&c&lDJ Station &4&l» &cVous avez été kick de la station par &4")
                            .append(player.getName())
                            .sendMessage(kicked);
                    Message.create("&3&lDJ Station &8&l» &7Vous avez kick &b")
                            .append(kicked.getName())
                            .append(" &7de la station numéro &b")
                            .append(args[0])
                            .append("&7.")
                            .sendMessage(player);
                    return;
                }
                djEntityOptional.get().removePlayer();
                Message.create("&c&lDJ Station &4&l» &cCette station n'est pas occupée.")
                        .sendMessage(player);
            }
            else {
                Message.create("&c&lDJ Station &4&l» &cAucune station n'a cet id.")
                        .sendMessage(player);
            }
            return;
        }
        Message.create("&c&lDJ Station &4&l» &cVous devez rentrer un id valide.")
                .sendMessage(player);
    }
}
