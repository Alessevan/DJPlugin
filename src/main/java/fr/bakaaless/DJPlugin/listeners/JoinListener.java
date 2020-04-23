package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Optional;

class JoinListener {

    @Getter(AccessLevel.PRIVATE)
    private final Listening listening;

    JoinListener(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerJoinEvent e) {
        final DjPlugin main = this.getListening().getMain();
        if (main.getLoadDJ().isPresent()) {
            main.getLoadDJ().get().accept(main.getServer().getOnlinePlayers().iterator().next());
            main.setLoadDJ(Optional.empty());
        }
    }
}
