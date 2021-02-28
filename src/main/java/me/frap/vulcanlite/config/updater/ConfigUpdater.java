package me.frap.vulcanlite.config.updater;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigUpdater {

    public void update(final Plugin plugin, final String resourceName, final File toUpdate, final List<String> ignoredSections) throws IOException {
        final BufferedReader newReader = new BufferedReader(new InputStreamReader(plugin.getResource(resourceName), StandardCharsets.UTF_8));
        final List<String> newLines = newReader.lines().collect(Collectors.toList());
        newReader.close();

        final FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(toUpdate);
        final FileConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource(resourceName), StandardCharsets.UTF_8));
        final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(toUpdate), StandardCharsets.UTF_8));

        final List<String> ignoredSectionsArrayList = new ArrayList<>(ignoredSections);
        ignoredSectionsArrayList.removeIf(ignoredSection -> !newConfig.isConfigurationSection(ignoredSection));

        final Yaml yaml = new Yaml();
        final Map<String, String> comments = parseComments(newLines, ignoredSectionsArrayList, oldConfig, yaml);
        write(newConfig, oldConfig, comments, ignoredSectionsArrayList, writer, yaml);
    }

    private void write(final FileConfiguration newConfig, final FileConfiguration oldConfig, final Map<String, String> comments, final List<String> ignoredSections, final BufferedWriter writer, final Yaml yaml) throws IOException {
        outer: for (String key : newConfig.getKeys(true)) {
            final String[] keys = key.split("\\.");
            final String actualKey = keys[keys.length - 1];
            final String comment = comments.remove(key);

            final StringBuilder prefixBuilder = new StringBuilder();
            final int indents = keys.length - 1;
            appendPrefixSpaces(prefixBuilder, indents);
            String prefixSpaces = prefixBuilder.toString();

            if (comment != null) {
                writer.write(comment);
            }

            for (final String ignoredSection : ignoredSections) {
                if (key.startsWith(ignoredSection)) {
                    continue outer;
                }
            }

            final Object newObj = newConfig.get(key);
            final Object oldObj = oldConfig.get(key);

            if (newObj instanceof ConfigurationSection && oldObj instanceof ConfigurationSection) {
                //write the old section
                writeSection(writer, actualKey, prefixSpaces, (ConfigurationSection) oldObj);
            } else if (newObj instanceof ConfigurationSection) {
                //write the new section, old value is no more
                writeSection(writer, actualKey, prefixSpaces, (ConfigurationSection) newObj);
            } else if (oldObj != null) {
                //write the old object
                write(oldObj, actualKey, prefixSpaces, yaml, writer);
            } else {
                //write new object
                write(newObj, actualKey, prefixSpaces, yaml, writer);
            }
        }

        final String danglingComments = comments.get(null);

        if (danglingComments != null) {
            writer.write(danglingComments);
        }

        writer.close();
    }

    private void write(Object obj, String actualKey, String prefixSpaces, Yaml yaml, BufferedWriter writer) throws IOException {
        if (obj instanceof ConfigurationSerializable) {
            writer.write(prefixSpaces + actualKey + ": " + yaml.dump(((ConfigurationSerializable) obj).serialize()));
        } else if (obj instanceof String || obj instanceof Character) {
            if (obj instanceof String) {
                final String s = (String) obj;
                obj = s.replace("\n", "\\n");
            }

            writer.write(prefixSpaces + actualKey + ": " + yaml.dump(obj));
        } else if (obj instanceof List) {
            writeList((List) obj, actualKey, prefixSpaces, yaml, writer);
        } else {
            writer.write(prefixSpaces + actualKey + ": " + yaml.dump(obj));
        }
    }

    private void writeSection(final BufferedWriter writer, final String actualKey, final String prefixSpaces, final ConfigurationSection section) throws IOException {
        if (section.getKeys(false).isEmpty()) {
            writer.write(prefixSpaces + actualKey + ": {}");
        } else {
            writer.write(prefixSpaces + actualKey + ":");
        }

        writer.write("\n");
    }

    //Writes a list of any object
    private void writeList(List list, String actualKey, String prefixSpaces, Yaml yaml, BufferedWriter writer) throws IOException {
        writer.write(getListAsString(list, actualKey, prefixSpaces, yaml));
    }

    private String getListAsString(final List list, final String actualKey, final String prefixSpaces, final Yaml yaml) {
        StringBuilder builder = new StringBuilder(prefixSpaces).append(actualKey).append(":");

        if (list.isEmpty()) {
            builder.append(" []\n");
            return builder.toString();
        }

        builder.append("\n");

        for (int i = 0; i < list.size(); i++) {
            final Object o = list.get(i);

            if (o instanceof String) {
                final String s = String.valueOf(o);
                builder.append(prefixSpaces).append("  - \"").append(s).append("\"");
            } else if(o instanceof Character){
                builder.append(prefixSpaces).append("  - '").append(o).append("'");
            } else if (o instanceof List) {
                builder.append(prefixSpaces).append("  - ").append(yaml.dump(o));
            } else {
                builder.append(prefixSpaces).append("  - ").append(o);
            }

            if (i != list.size()) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    private Map<String, String> parseComments(final List<String> lines, final List<String> ignoredSections, final FileConfiguration oldConfig, Yaml yaml) {
        final Map<String, String> comments = new HashMap<>();
        final StringBuilder builder = new StringBuilder();
        final StringBuilder keyBuilder = new StringBuilder();
        int lastLineIndentCount = 0;

        outer: for (String line : lines) {
            if (line != null && line.trim().startsWith("-"))
                continue;

            if (line == null || line.trim().equals("") || line.trim().startsWith("#")) {
                builder.append(line).append("\n");
            } else {
                lastLineIndentCount = setFullKey(keyBuilder, line, lastLineIndentCount);

                for (final String ignoredSection : ignoredSections) {
                    if (keyBuilder.toString().equals(ignoredSection)) {
                        final Object value = oldConfig.get(keyBuilder.toString());

                        if (value instanceof ConfigurationSection)
                            appendSection(builder, (ConfigurationSection) value, new StringBuilder(getPrefixSpaces(lastLineIndentCount)), yaml);

                        continue outer;
                    }
                }

                if (keyBuilder.length() > 0) {
                    comments.put(keyBuilder.toString(), builder.toString());
                    builder.setLength(0);
                }
            }
        }

        if (builder.length() > 0) {
            comments.put(null, builder.toString());
        }

        return comments;
    }

    private void appendSection(final StringBuilder builder, final ConfigurationSection section, final StringBuilder prefixSpaces, Yaml yaml) {
        builder.append(prefixSpaces).append(getKeyFromFullKey(section.getCurrentPath())).append(":");
        Set<String> keys = section.getKeys(false);

        if (keys.isEmpty()) {
            builder.append(" {}\n");
            return;
        }

        builder.append("\n");
        prefixSpaces.append("  ");

        for (String key : keys) {
            final Object value = section.get(key);
            final String actualKey = getKeyFromFullKey(key);

            if (value instanceof ConfigurationSection) {
                appendSection(builder, (ConfigurationSection) value, prefixSpaces, yaml);
                prefixSpaces.setLength(prefixSpaces.length() - 2);
            } else if (value instanceof List) {
                builder.append(getListAsString((List) value, actualKey, prefixSpaces.toString(), yaml));
            } else {
                builder.append(prefixSpaces.toString()).append(actualKey).append(": ").append(yaml.dump(value));
            }
        }
    }

    //Counts spaces in front of key and divides by 2 since 1 indent = 2 spaces
    private int countIndents(String s) {
        int spaces = 0;

        for (char c : s.toCharArray()) {
            if (c == ' ') {
                spaces += 1;
            } else {
                break;
            }
        }

        return spaces / 2;
    }

    //Ex. keyBuilder = key1.key2.key3 --> key1.key2
    private void removeLastKey(StringBuilder keyBuilder) {
        String temp = keyBuilder.toString();
        String[] keys = temp.split("\\.");

        if (keys.length == 1) {
            keyBuilder.setLength(0);
            return;
        }

        temp = temp.substring(0, temp.length() - keys[keys.length - 1].length() - 1);
        keyBuilder.setLength(temp.length());
    }

    private String getKeyFromFullKey(String fullKey) {
        String[] keys = fullKey.split("\\.");
        return keys[keys.length - 1];
    }

    private int setFullKey(final StringBuilder keyBuilder, final String configLine, final int lastLineIndentCount) {
        int currentIndents = countIndents(configLine);
        String key = configLine.trim().split(":")[0];

        if (keyBuilder.length() == 0) {
            keyBuilder.append(key);
        } else if (currentIndents == lastLineIndentCount) {
            removeLastKey(keyBuilder);

            if (keyBuilder.length() > 0) {
                keyBuilder.append(".");
            }

            keyBuilder.append(key);
        } else if (currentIndents > lastLineIndentCount) {
            keyBuilder.append(".").append(key);
        } else {
            int difference = lastLineIndentCount - currentIndents;

            for (int i = 0; i < difference + 1; i++) {
                removeLastKey(keyBuilder);
            }

            if (keyBuilder.length() > 0) {
                keyBuilder.append(".");
            }

            keyBuilder.append(key);
        }

        return currentIndents;
    }

    private String getPrefixSpaces(final int indents) {
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < indents; i++) {
            builder.append("  ");
        }

        return builder.toString();
    }

    private void appendPrefixSpaces(final StringBuilder builder, final int indents) {
        builder.append(getPrefixSpaces(indents));
    }
}
