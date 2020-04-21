package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import lombok.Getter;
import org.bukkit.event.player.PlayerQuitEvent;

class QuitListener {

    @Getter
    private final Listening listening;

    QuitListener(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerQuitEvent e){
        if(this.getListening().getMain().getExecutor().getEditPlayerDj().containsKey(e.getPlayer())){
            DjEntity djEntity = this.getListening().getMain().getExecutor().getEditPlayerDj().get(e.getPlayer());
            djEntity.save();
            this.getListening().getMain().getExecutor().getEditPlayerDj().remove(e.getPlayer());
        }
        if(this.getListening().getMain().getDjEntity(e.getPlayer()).isPresent()){
            this.getListening().getMain().getDjEntity(e.getPlayer()).get().removePlayer();
        }
    }
}
