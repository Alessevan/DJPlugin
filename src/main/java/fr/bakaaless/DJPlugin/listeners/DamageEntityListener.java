package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

class DamageEntityListener {

    void handle(final EntityDamageByEntityEvent e) {
        if ((e.getDamager() instanceof Player) && (e.getEntity() instanceof ArmorStand)) {
            e.setCancelled(DjEntity.isDj((Player) e.getDamager(), (ArmorStand) e.getEntity()));
        }

        DjPlugin.getInstance().getDjEntities().forEach(djEntity -> {
            if (djEntity.getDancerEntities().contains(e.getEntity()))
                e.setCancelled(true);
        });
    }
}
