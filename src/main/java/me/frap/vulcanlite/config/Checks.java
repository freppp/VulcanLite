package me.frap.vulcanlite.config;

import lombok.experimental.UtilityClass;
import me.frap.vulcanlite.config.updater.ConfigUpdater;
import me.frap.vulcanlite.VulcanLite;
import me.frap.vulcanlite.util.CacheUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.logging.Level;

@UtilityClass
public class Checks {

    private final String fileName = "checks";

    private FileConfiguration config;

    private File file;

    public void initialize() {
        if (!VulcanLite.INSTANCE.getPlugin().getDataFolder().exists()) {
            VulcanLite.INSTANCE.getPlugin().getDataFolder().mkdir();
        }

        file = new File(VulcanLite.INSTANCE.getPlugin().getDataFolder(), fileName + ".yml");

        if(!file.exists()){
            file.getParentFile().mkdirs();
            copy(VulcanLite.INSTANCE.getPlugin().getResource(fileName + ".yml"), file);
        }
        config = YamlConfiguration.loadConfiguration(file);
        save();
        reload();
    }

    public void save() {
        try {
            ConfigUpdater.update(VulcanLite.INSTANCE.getPlugin(), fileName + ".yml", file, Arrays.asList());
        } catch (final Exception e){
            final File f = new File(VulcanLite.INSTANCE.getPlugin().getDataFolder(), fileName + ".broken." + new Date().getTime());
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Could not save: " + fileName + ".yml");
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Regenerating file and renaming the current file to: " + f.getName());
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "You can try fixing the file with a yaml parser online!");
            file.renameTo(f);
            initialize();
        }
    }

    public void reload(){
        try {
            config.load(file);
        } catch (final Exception e){
            final File f = new File(VulcanLite.INSTANCE.getPlugin().getDataFolder(), fileName + ".broken." + new Date().getTime());
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Could not reload: " + fileName + ".yml");
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Regenerating file and renaming the current file to: " + f.getName());
            VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "You can try fixing the file with a yaml parser online!");
            file.renameTo(f);
            initialize();
        }
    }

    private void copy(final InputStream in, final File file) {
        try {
            final OutputStream out = new FileOutputStream(file);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(final String path) {
        return config.getString(path);
    }

    public int getInt(final String path) {
        return config.getInt(path);
    }

    public boolean getBoolean(final String path) {
        return config.getBoolean(path);
    }

    public List<String> getStringList(final String path) {
        return config.getStringList(path);
    }

    public long getLong(final String path) {
        return config.getLong(path);
    }

    public double getDouble(final String path) {
        return config.getDouble(path);
    }

    @UtilityClass
    public class Values {

        public final Map<String, Boolean> ENABLED_CHECKS = new HashMap<>();
        public final Map<String, Boolean> PUNISHABLE = new HashMap<>();
        public final Map<String, Boolean> BROADCAST_PUNISHMENT = new HashMap<>();

        public final Map<String, Integer> ALERT_INTERVAL = new HashMap<>();
        public final Map<String, Integer> MAX_VIOLATIONS = new HashMap<>();

        public final Map<String, List<String>> PUNISHMENT_COMMANDS = new HashMap<>();

        public void update() {
            ENABLED_CHECKS.clear();
            PUNISHABLE.clear();
            BROADCAST_PUNISHMENT.clear();

            ALERT_INTERVAL.clear();
            MAX_VIOLATIONS.clear();

            PUNISHMENT_COMMANDS.clear();

            CacheUtil.cacheCheckValues();
        }
    }
}