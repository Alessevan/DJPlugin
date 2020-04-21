package fr.bakaaless.DJPlugin.listeners;

import com.xxmicloxx.NoteBlockAPI.event.SongEndEvent;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listening implements Listener {

    @Getter
    private final DjPlugin main;

    public Listening() {
        this.main = DjPlugin.getInstance();
        this.getMain().getServer().getPluginManager().registerEvents(this, this.getMain());
    }
    @EventHandler
    public void onPlayerInteractAtEntity(final PlayerInteractAtEntityEvent e){
        new InteractEntityListener().handle(e);
    }

    @EventHandler
    public void onEntityDamageByEntity(final EntityDamageByEntityEvent e){
        new DamageEntityListener().handle(e);
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e){
        new QuitListener(this).handle(e);
    }

    @EventHandler
    public void onSongEnd(final SongEndEvent e){
        new SongEndListener(this).handle(e);
    }
}
