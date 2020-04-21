package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

class InteractEntityListener {

    void handle(final PlayerInteractAtEntityEvent e){
        if(e.getRightClicked() instanceof ArmorStand){
            e.setCancelled(DjEntity.isDj(e.getPlayer(), (ArmorStand) e.getRightClicked()));
        }
    }
}
