package fr.bakaaless.DJPlugin.utils;

import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import fr.bakaaless.DJPlugin.plugin.DjPlugin;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class FileManager {

    @Getter
    private final DjPlugin main;
    private final Map<String, File> filesMap;
    private final Map<String, YamlConfiguration> yamlMap;

    public FileManager(final DjPlugin main, final String... files) {
        this.main = main;
        this.filesMap = new HashMap<>();
        this.yamlMap = new HashMap<>();
        this.init(files);
    }

    public void init(final String... files) {
        for(String file : files) {
            this.filesMap.put(file, new File(main.getDataFolder(), file + ".yml"));
            if (!this.filesMap.get(file).exists()) {
                this.filesMap.get(file).getParentFile().mkdirs();
                main.saveResource(file + ".yml", false);
            }
            this.yamlMap.put(file, new YamlConfiguration());
            try {
                this.yamlMap.get(file).load(this.filesMap.get(file));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
        
    }

    public void reload(){
        for(final String file : this.filesMap.keySet()){
            this.filesMap.replace(file, new File(this.main.getDataFolder(), file + ".yml"));
            if (!this.filesMap.get(file).exists()) {
                this.filesMap.get(file).getParentFile().mkdirs();
                main.saveResource(file + ".yml", false);
            }
            this.yamlMap.replace(file, new YamlConfiguration());
            try {
                this.yamlMap.get(file).load(this.filesMap.get(file));
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }

        this.getMain().getServer().getScheduler().runTaskAsynchronously(this.getMain(), () -> {
            try {
                this.getMain().getSongs().clear();
                this.getMain().getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement des musiques en fond..."));
                final File o = new File(this.getMain().getDataFolder().getPath() + "/tracks/");
                if (!o.exists()) {
                    o.mkdirs();
                }
                final File[] arrayOfFile;
                int str1 = (arrayOfFile = o.listFiles()).length;
                for (int s = 0; s < str1; s++) {
                    File t = arrayOfFile[s];
                    if ((!t.isDirectory()) &&
                            (t.getName().endsWith(".nbs")) && (!NBSDecoder.parse(t).getTitle().equals(""))) {
                        this.getMain().registerNewSong(NBSDecoder.parse(t));
                    }
                }
                this.getMain().getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement de " + this.getMain().getSongs().size() + " musiques terminé."));
            } catch (final Exception e) {
                this.getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve songs from 'tracks/'."));
                this.getMain().getServer().getPluginManager().disablePlugin(this.getMain());
                return;
            }
        });
    }

    public void setLine(final String file, final String path, final Object value) {
        this.yamlMap.get(file).set(path, value);
        try {
            this.yamlMap.get(file).save(this.filesMap.get(file));
            this.yamlMap.get(file).load(this.filesMap.get(file));
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getFile(final String file) {
        return this.yamlMap.get(file);
    }

    public String getMessage(final String file, final String path) {
        if(this.yamlMap.get(file).getString(path) == null) return "";
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.yamlMap.get(file).getString(path)));
    }

}
