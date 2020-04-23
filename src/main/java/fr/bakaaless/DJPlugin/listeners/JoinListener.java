package fr.bakaaless.DJPlugin.listeners;

import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import fr.bakaaless.DJPlugin.utils.LocationUtils;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.logging.Level;

class JoinListener {

    @Getter(AccessLevel.PRIVATE)
    private final Listening listening;

    JoinListener(final Listening listening) {
        this.listening = listening;
    }

    void handle(final PlayerJoinEvent e) {
        final DjPlugin main = this.getListening().getMain();
        if (main.isLoadDJ()) {
            main.getServer().getScheduler().runTaskLaterAsynchronously(main, () -> {
                final long current = (new Timestamp(System.currentTimeMillis())).getTime();
                main.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Chargement des DJ."));
                main.getServer().getScheduler().scheduleSyncDelayedTask(main, () -> {
                    for (final String id : main.getFileManager().getFile("data").getConfigurationSection("instances").getKeys(false)) {
                        // En cas d'exception Null
                        try {
                            final Location djLoc = (Location) main.getFileManager().getFile("data").get("instances." + id + ".dj");
                            final Optional<ArmorStand> dj = Optional.of((ArmorStand) djLoc.getWorld().getNearbyEntities(djLoc, 1, 1, 1).parallelStream().filter(entity -> entity.getLocation().equals(djLoc)).filter(entity -> entity instanceof ArmorStand).findFirst().get());
                            final Optional<Location> headLoc = (main.getFileManager().getFile("data").get("instances." + id + ".head") == null ? Optional.empty() : Optional.of((Location) main.getFileManager().getFile("data").get("instances." + id + ".head")));
                            final Optional<ArmorStand> head = (!headLoc.isPresent() ? Optional.empty() : headLoc.get().getWorld().getNearbyEntities(headLoc.get(), 1, 1, 1).parallelStream().filter(entity -> entity instanceof ArmorStand).filter(entity -> LocationUtils.isSameLoc(entity.getLocation(), headLoc.get())).findFirst().isPresent() ? Optional.of((ArmorStand) headLoc.get().getWorld().getNearbyEntities(headLoc.get(), 1, 1, 1).parallelStream().filter(entity -> entity instanceof ArmorStand).filter(entity -> LocationUtils.isSameLoc(entity.getLocation(), headLoc.get())).findFirst().get()) : Optional.empty());
                            final Optional<Location> jukebox = Optional.ofNullable((Location) main.getFileManager().getFile("data").get("instances." + id + ".jukebox"));
                            final Optional<Location> dancer1 = Optional.ofNullable((Location) main.getFileManager().getFile("data").get("instances." + id + ".dancer1"));
                            final Optional<Location> dancer2 = Optional.ofNullable((Location) main.getFileManager().getFile("data").get("instances." + id + ".dancer2"));
                            main.getDjEntities().add(new DjEntity(Integer.parseInt(id), dj, head, jukebox, dancer1, dancer2));

                        } catch (Exception exception) {
                            main.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Erreur lors du chargement du DJ #" + id + ". (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current)) + "ms)", exception);
                        }
                    }
                    main.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Chargement de " + main.getDjEntities().size() + " DJ terminé. (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current)) + "ms)");
                    main.setLoadDJ(false);
                }, 0L);
            }, 20L);
        }
    }
}
