package fr.bakaaless.DJPlugin.listeners;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import fr.bakaaless.DJPlugin.entities.DjEntity;
import lombok.AccessLevel;
import lombok.Getter;

class SongEndListener {

    @Getter(AccessLevel.PRIVATE)
    private final Listening listening;

    SongEndListener(final Listening listening) {
        this.listening = listening;
    }

    void handle(final SongEndEvent e){
        for(final DjEntity djEntity : this.getListening().getMain().getDjEntities()){
            if(djEntity.getSongPlayer() != null)
                if(djEntity.getSongPlayer().equals(e.getSongPlayer()))
                    djEntity.stopSound();
        }
    }
}
