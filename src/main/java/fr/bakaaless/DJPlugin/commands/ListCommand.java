package fr.bakaaless.DJPlugin.commands;

import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.entity.Player;

class ListCommand {

    @Getter
    private final Executor executor;

    ListCommand(final Executor executor) {
        this.executor = executor;
    }

    void handle(final Player player, int page) {
        if(!player.hasPermission("djstation.list")){
            Message.create("&c&lDJ Station &4&l» &cVous n'avez pas la permission nécessaire.")
                    .sendMessage(player);
            return;
        }
        if(page == 0) page++;
        if(this.getExecutor().getMain().getDjEntities().size() == 0){
            Message.create("&c&lDJ Station &4&l» &cAucune station n'a été trouvée.")
                    .sendMessage(player);
        }
        else {
            final Message message = Message.create("&3&lDJ Station &8&l» &7Liste des stations page (")
                                            .append(page)
                                            .append("/")
                                            .append((int) Math.ceil(this.getExecutor().getMain().getDjEntities().size() / 9.0))
                                            .append(") :");
            for(int i = 9 * (page - 1); i < 9 * page; i++){
                if(this.getExecutor().getMain().getDjEntities().size() <= i) continue;
                if(this.getExecutor().getMain().getDjEntities().get(i) == null) continue;
                message.addLine("&8 -|> &b(")
                        .append(this.getExecutor().getMain().getDjEntities().get(i).getId())
                        .append("): ");
                if(this.getExecutor().getMain().getDjEntities().get(i).getDj().isPresent()){
                    message.append("&7x=&b")
                            .append(this.getExecutor().getMain().getDjEntities().get(i).getDj().get().getLocation().getBlockX())
                            .append("&7, y=&b")
                            .append(this.getExecutor().getMain().getDjEntities().get(i).getDj().get().getLocation().getBlockY())
                            .append("&7, z=&b")
                            .append(this.getExecutor().getMain().getDjEntities().get(i).getDj().get().getLocation().getBlockZ())
                            .append("&7;");
                }
                else {
                    message.append("&cLocation non définie.");
                }
            }
            message.sendMessage(player);
        }
    }

}
