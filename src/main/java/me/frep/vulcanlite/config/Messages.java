package me.frep.vulcanlite.config;

import lombok.experimental.UtilityClass;
import me.frep.vulcanlite.VulcanLite;
import me.frep.vulcanlite.config.updater.ConfigUpdater;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Messages {

    private final String fileName = "messages";

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

    private String getString(final String path) {
        return config.getString(path);
    }

    private int getInt(final String path) {
        return config.getInt(path);
    }

    private List<String> getStringList(final String path) {
        return config.getStringList(path);
    }

    private long getLong(final String path) {
        return config.getLong(path);
    }

    private double getDouble(final String path) {
        return config.getDouble(path);
    }

    @UtilityClass
    public class Values {

        public void update() {
            try {

            } catch (final Exception exception) {
                VulcanLite.INSTANCE.getPlugin().getLogger().log(Level.SEVERE, "Could not configuration file!");
            }
        }
    }
}
