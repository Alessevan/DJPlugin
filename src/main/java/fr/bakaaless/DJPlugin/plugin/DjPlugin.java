package fr.bakaaless.DJPlugin.plugin;

import com.xxmicloxx.NoteBlockAPI.NoteBlockAPI;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.bakaaless.DJPlugin.commands.Executor;
import fr.bakaaless.DJPlugin.entities.DjEntity;
import fr.bakaaless.DJPlugin.listeners.Listening;
import fr.bakaaless.DJPlugin.utils.FileManager;
import fr.bakaaless.DJPlugin.utils.Message;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    @Override
    public void onEnable() {
        if(!this.getServer().getPluginManager().isPluginEnabled("NoteBlockAPI")){
            return;
        }
        INSTANCE = this;
        this.songs = new ArrayList<>();
        this.djEntities = new ArrayList<>();
        try {
             this.fileManager = new FileManager(this, "data");
        }
        catch(final Exception e){
            this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve data files."));
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            try {
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement des musiques en fond."));
                this.getSongs().clear();
                final File o = new File(this.getDataFolder().getPath() + "/tracks/");
                if (!o.exists()) {
                    o.mkdirs();
                }
                final File[] arrayOfFile;
                int str1 = (arrayOfFile = o.listFiles()).length;
                for (int s = 0; s < str1; s++) {
                    File t = arrayOfFile[s];
                    if ((!t.isDirectory()) &&
                            (t.getName().endsWith(".nbs")) && (!NBSDecoder.parse(t).getTitle().equals(""))) {
                        this.registerNewSong(NBSDecoder.parse(t));
                    }
                }
                this.getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement de " + this.getSongs().size() + " musiques terminé."));
            } catch (final Exception e) {
                this.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve songs from 'tracks/'."));
                this.getServer().getPluginManager().disablePlugin(this);
                return;
            }
        });
        this.listening = new Listening();
        this.executor = new Executor();
        this.getServer().getScheduler().runTaskAsynchronously(this, () -> {
            for(final String id : this.getFileManager().getFile("data").getConfigurationSection("instances").getKeys(false)){
                final Optional<ArmorStand> dj = Optional.ofNullable((ArmorStand) this.getServer().getEntity(UUID.fromString(this.getFileManager().getFile("data").getString("instances." + id + ".dj"))));
                final Optional<ArmorStand> head = (this.getFileManager().getFile("data").getString("instances." + id + ".head") == null ? Optional.empty() : Optional.ofNullable((ArmorStand) this.getServer().getEntity(UUID.fromString(this.getFileManager().getFile("data").getString("instances." + id + ".head")))));
                final Optional<Location> jukebox = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".jukebox"));
                final Optional<Location> dancer1 = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".dancer1"));
                final Optional<Location> dancer2 = Optional.ofNullable((Location) this.getFileManager().getFile("data").get("instances." + id + ".dancer2"));
                this.getDjEntities().add(new DjEntity(Integer.parseInt(id), dj, head, jukebox, dancer1, dancer2));
            }
        });
    }

    @Override
    public void onDisable() {
        this.getDjEntities().forEach(DjEntity::removePlayer);
        this.getDjEntities().forEach(DjEntity::save);
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
