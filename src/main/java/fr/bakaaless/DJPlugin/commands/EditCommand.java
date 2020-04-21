package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import java.util.Optional;

class EditCommand {

    @Getter
    private final Executor executor;

    EditCommand(final Executor executor) {
        this.executor = executor;
    }

    void handle(final Player player, final String... args){
        if(!player.hasPermission("djstation.edit")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(this.getExecutor().getEditPlayerDj().containsKey(player)){
            Message.create("&c&lDJ Station &4&l» &cVous êtes déjà en train d'éditer une station.")
                    .sendMessage(player);
            return;
        }
        if(StringUtils.isNumeric(args[0])){
            final Optional<DjEntity> djEntityOptional = this.getExecutor().getMain().getDjEntity(Integer.parseInt(args[0]));
            if(djEntityOptional.isPresent()){
                if(this.getExecutor().getEditPlayerDj().containsValue(djEntityOptional.get())){
                    Message.create("&c&lDJ Station &4&l» &cQuelqu'un est déjà en train d'éditer cette station.")
                            .sendMessage(player);
                    return;
                }
                if(djEntityOptional.get().getPlayer().isPresent()){
                    Message.create("&c&lDJ Station &4&l» &cQuelqu'un utilise cette station, vous ne pouvez pas la modifier.")
                            .sendMessage(player);
                    return;
                }
                this.getExecutor().getEditPlayerDj().put(player, djEntityOptional.get());
                Message.create("&3&lDJ Station &8&l» &7Vous éditez maintenant la station numéro &b&l")
                        .append(args[0])
                        .append("&7.")
                        .sendMessage(player);
                return;
            }
            else {
                Message.create("&c&lDJ Station &4&l» &cAucune station n'a cet id.")
                        .sendMessage(player);
                return;
            }
        }
        Message.create("&c&lDJ Station &4&l» &cVous devez rentrer un id valide.")
                .sendMessage(player);
    }
}
