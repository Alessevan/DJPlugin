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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;

public class FileManager {

    @Getter
    private final DjPlugin main;
    private final Map<String, File> filesMap;
    private final Map<String, YamlConfiguration> yamlMap;

    public FileManager(final DjPlugin main, final String... files) throws IOException, InvalidConfigurationException {
        this.main = main;
        this.filesMap = new HashMap<>();
        this.yamlMap = new HashMap<>();
        this.init(files);
    }

    public void init(final String... files) throws IOException, InvalidConfigurationException {
        // Récupération / Création des fichiers
        for (String file : files) {
            this.filesMap.put(file, new File(main.getDataFolder(), file + ".yml"));
            if (!this.filesMap.get(file).exists()) {
                this.filesMap.get(file).getParentFile().mkdirs();
                main.saveResource(file + ".yml", false);
            }
            this.yamlMap.put(file, new YamlConfiguration());
            this.yamlMap.get(file).load(this.filesMap.get(file));
        }

    }

    public void reload() throws IOException, InvalidConfigurationException {
        // Récupération / Création des fichiers
        for (final String file : this.filesMap.keySet()) {
            this.filesMap.replace(file, new File(this.main.getDataFolder(), file + ".yml"));
            if (!this.filesMap.get(file).exists()) {
                final boolean b = this.filesMap.get(file).getParentFile().mkdirs();
                if (!b) {
                    this.getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve creating file '" + file + "'."));
                    continue;
                }
                main.saveResource(file + ".yml", false);
            }
            this.yamlMap.replace(file, new YamlConfiguration());
            this.yamlMap.get(file).load(this.filesMap.get(file));
        }
        // On récupère les sons de façon asynchrone pour éviter les lags :)
        this.getMain().getServer().getScheduler().runTaskAsynchronously(this.getMain(), () -> {
            try {
                final long current = (new Timestamp(System.currentTimeMillis())).getTime();
                this.getMain().getSongs().clear();
                this.getMain().getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement des musiques en fond..."));
                final File o = new File(this.getMain().getDataFolder().getPath() + "/tracks/");
                if (!o.exists()) {
                    final boolean b = o.mkdirs();
                    if (!b)
                        this.getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve creating 'tracks/'."));

                }
                final File[] arrayOfFile;
                int str1 = ((Objects.requireNonNull(arrayOfFile = (o.listFiles() != null ? o.listFiles() : new File[0]))).length > 0 ? arrayOfFile.length : 0);
                for (int s = 0; s < str1; s++) {
                    File t = arrayOfFile[s];
                    if ((!t.isDirectory()) &&
                            (t.getName().endsWith(".nbs")) && (!NBSDecoder.parse(t).getTitle().equals(""))) {
                        this.getMain().registerNewSong(NBSDecoder.parse(t));
                    }
                }
                this.getMain().getLogger().log(Level.INFO, ChatColor.translateAlternateColorCodes('&', "§3§lDJ Station §8§l» §7Chargement de " + this.getMain().getSongs().size() + " musiques terminé. (" + ((new Timestamp(System.currentTimeMillis())).getTime() - current) + "ms)"));
            } catch (final Exception e) {
                this.getMain().getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', "§c§lDJ Station §4§l» §cError while retrieve songs from 'tracks/'."));
                this.getMain().getServer().getPluginManager().disablePlugin(this.getMain());
            }
        });
    }

    public void setLine(final String file, final String path, final Object value) {
        // On met à jour la valeur
        this.yamlMap.get(file).set(path, value);
        // On save et on recharge le fichier
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
        // En cas de null exception
        if (this.yamlMap.get(file).getString(path) == null)
            return ChatColor.translateAlternateColorCodes('&', "&cPath not found.");
        // On return en remplaçant les couleurs.
        return ChatColor.translateAlternateColorCodes('&', Objects.requireNonNull(this.yamlMap.get(file).getString(path)));
    }

}
