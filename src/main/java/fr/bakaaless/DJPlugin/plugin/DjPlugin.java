package fr.bakaaless.DJPlugin.plugin;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.bakaaless.DJPlugin.commands.Executor;
import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.listeners.Listening;
import fr.bakaaless.DJPlugin.utils.FileManager;
import fr.bakaaless.DJPlugin.utils.LocationUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;

public class DjPlugin extends JavaPlugin {

    private static DjPlugin INSTANCE;

    @Getter
    private FileManager fileManager;

    @Getter
    private List<Song> songs;

    @Getter
    private List<DjEntity> djEntities;

    @Getter
    private Listening listening;

    @Getter
    private Executor executor;

    @Getter()
    @Setter()
    private boolean loadDJ;

    @Override
    public void onEnable() {
        // Check si la dépendance nécessaire est présente
        if (!this.getServer().getPluginManager().isPluginEnabled("NoteBlockAPI")) {
            this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c&lDJ Station &4&l» &cNoteBlockAPI is required for this plugin."));
            return;
        }
        INSTANCE = this;
        this.songs = new ArrayList<>();
        this.djEntities = new ArrayList<>();
        // Création des fichiers
        try {
            this.fileManager = new FileManager(this, "data");
        } catch (final IllegalArgumentException | IOException | InvalidConfigurationException e) {
            this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c&lDJ Station &4&l» &cError while retrieve data files."));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Récupération asynchrone des sons
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                final long current = (new Timestamp(System.currentTimeMillis())).getTime();
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement des musiques en fond."));
                this.getSongs().clear();
                final File o = new File(this.getDataFolder().getPath() + "/tracks/");
                if (!o.exists()) {
                    final boolean b = o.mkdirs();
                    if (!b)
                        this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve creating 'tracks/'."));
                }
                final File[] arrayOfFile;
                int str1 = ((Objects.requireNonNull(arrayOfFile = (o.listFiles() != null ? o.listFiles() : new File[0]))).length > 0 ? arrayOfFile.length : 0);
                for (int s = 0; s < str1; s++) {
                    File t = arrayOfFile[s];
                    if ((!t.isDirectory()) &&
                            (t.getName().endsWith(".nbs")) && (!NBSDecoder.parse(t).getTitle().equals(""))) {
                        this.registerNewSong(NBSDecoder.parse(t));
                    }
                }
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement de " + this.getSongs().size() + " musiques terminé. (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current) + "ms)"));
            } catch (final Exception e) {
                this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve songs from 'tracks/'."));
                this.getServer().getPluginManager().disablePlugin(this);
            }
        });
        this.listening = new Listening();
        this.executor = new Executor();
        // Réccupération asynchrone des stations
        if (this.getServer().getOnlinePlayers().size() > 0) {
            this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
                final long current = (new Timestamp(System.currentTimeMillis())).getTime();
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Chargement des DJ."));
                for (final String id : this.getFileManager().getFile("data").getConfigurationSection("instances").getKeys(false)) {
                    // En cas d'exception Null
                    try {
                        this.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
                            final Location djLoc = (Location) this.getFileManager().getFile("data").get("instances." + id + ".dj");
                            final Optional<ArmorStand> dj = Optional.of((ArmorStand) djLoc.getWorld().getNearbyEntities(djLoc, 1, 1, 1).parallelStream().filter(entity -> entity.getLocation().equals(djLoc)).filter(entity -> entity instanceof ArmorStand).findFirst().get());
                            final Optional<Location> headLoc = (this.getFileManager().getFile("data").getString("instances." + id + ".head") == null ? Optional.empty() : Optional.of((Location) this.getFileManager().getFile("data").get("instances." + id + ".head")));
                            final Optional<ArmorStand> head = (!headLoc.isPresent() ? Optional.empty() : headLoc.get().getWorld().getNearbyEntities(headLoc.get(), 1, 1, 1).parallelStream().filter(entity -> entity instanceof ArmorStand).filter(entity -> LocationUtils.isSameLoc(entity.getLocation(), headLoc.get())).findFirst().isPresent() ? Optional.of((ArmorStand) headLoc.get().getWorld().getNearbyEntities(headLoc.get(), 1, 1, 1).parallelStream().filter(entity -> entity instanceof ArmorStand).filter(entity -> LocationUtils.isSameLoc(entity.getLocation(), headLoc.get())).findFirst().get()) : Optional.empty());
                            final Optional<Location> jukebox = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".jukebox"));
                            final Optional<Location> dancer1 = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".dancer1"));
                            final Optional<Location> dancer2 = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".dancer2"));
                            this.getDjEntities().add(new DjEntity(Integer.parseInt(id), dj, head, jukebox, dancer1, dancer2));
                        }, 0L);
                    } catch (Exception e) {
                        this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "&c&lDJ Station &4&l» &cErreur lors du chargement du DJ #" + id + ". (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current)) + "ms)", e);
                    }
                }
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Chargement de " + this.getDjEntities().size() + " DJ terminé. (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current)) + "ms)");
                loadDJ = false;
            });
        } else {
            this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "&3&lDJ Station &8&l» &7Chargement des DJ lors de la connexion d'un joueur."));
            loadDJ = true;
        }
    }

    @Override
    public void onDisable() {
        this.getDjEntities().forEach(DjEntity::removePlayer);
        //this.getDjEntities().forEach(DjEntity::save);
    }

    public void registerNewSong(final Song song) {
        if (song == null) {
            return;
        }
        songs.add(song);
    }

    public Optional<DjEntity> getDjEntity(final int id){
        return this.getDjEntities().parallelStream().filter(djEntity -> id == djEntity.getId()).findFirst();
    }

    public Optional<DjEntity> getDjEntity(final ArmorStand dj){
        return this.getDjEntities().parallelStream().filter(djEntity -> Optional.of(dj).equals(djEntity.getDj())).findFirst();
    }

    public Optional<DjEntity> getDjEntityByHead(final ArmorStand head){
        return this.getDjEntities().parallelStream().filter(djEntity -> Optional.of(head).equals(djEntity.getHead())).findFirst();
    }

    public Optional<DjEntity> getDjEntity(final Player player){
        return this.getDjEntities().parallelStream().filter(djEntity -> Optional.of(player).equals(djEntity.getPlayer())).findFirst();
    }

    public static DjPlugin getInstance() {
        return INSTANCE;
    }

}
