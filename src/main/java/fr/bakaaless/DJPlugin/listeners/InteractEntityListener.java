package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

class InteractEntityListener {

    void handle(final PlayerInteractAtEntityEvent e) {
        if (e.getRightClicked() instanceof ArmorStand) {
            e.setCancelled(DjEntity.isDj(e.getPlayer(), (ArmorStand) e.getRightClicked()));
        }
        DjPlugin.getInstance().getDjEntities().forEach(djEntity -> {
            if (djEntity.getDancerEntities().contains(e.getRightClicked()))
                e.setCancelled(true);
        });
    }
}
