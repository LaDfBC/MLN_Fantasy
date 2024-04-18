package com.jaerapps;

import java.io.File;
import java.io.FileReader;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Singleton Configuration class - Load once from a file and then fetch the properties using static
 * calls whenever you need them.
 */
public class Configuration {
    private final Properties properties;
    private static Configuration instance;

    public static final String UPDATE_DB_ON_STARTUP = "update.db";
    public static final String DATABASE_USERNAME = "database.user";
    public static final String DATABASE_PASSWORD = "database.pass";
    public static final String DATABASE_HOST = "database.host";
    public static final String DATABASE_NAME = "database.name";
    public static final String DATABASE_TYPE = "database.type";
    public static final String DATABASE_PORT = "database.port";
    public static final String GENERATE_JOOQ = "generate.jooq";

    /**
     * Private constructor - to keep it singleton. Takes and loads all of the configs from the given file
     * @param configFile - File from which configs should be loaded
     */
    private Configuration(final String configFile) {
        properties = new java.util.Properties();
        try {
            properties.load(new FileReader(new File(configFile)));
        } catch (Exception eta) {
            eta.printStackTrace();
            System.exit(1);
        }
    }

    private Configuration(final Map<String, String> propertyMap) {
        properties = new java.util.Properties();
        properties.putAll(propertyMap);
    }

    private Configuration() {
        properties = new java.util.Properties();
    }

    /**
     * Loads all the configs, changing the state of this class.  Will change it for ALL references to this
     * class since it's a singleton.
     *
     * @param configFile - The file from which configs should be loaded.
     */
    public static void setInstanceFromFile(String configFile) {
        instance = new Configuration(configFile);
    }

    public static void setFromMap(Map<String, String> propMap) {
        instance = new Configuration(propMap);
    }

    private String getValue(String key) {
        return properties.getProperty(key);
    }

    /**
     * Fetches the given property from the set of properties currently stored in this class.  If not found,
     * returns the Optional with no value.
     *
     * @param key The key for which to return the value out of the set of properties
     * @return an Optional: empty if not found and the value if it is.
     */
    public static Optional<String> getProperty(final String key) {
        return instance.getValue(key) == null ? Optional.empty() : Optional.of(instance.getValue(key));
    }

    /**
     * Like {@link #getProperty} except throws an error immediately if the key doesn't exist.  For
     *  mission critical configurations.
     *
     * @param key The key for which to return the value out of the set of properties
     * @return The String value referenced by the passed key
     * @throws IllegalArgumentException if the key does not exist in these properties
     */
    public static String getPropertyOrThrow(final String key) throws IllegalArgumentException {
        Optional<String> fetchedProp = getProperty(key);

        if (fetchedProp.isPresent()) {
            return fetchedProp.get();
        } else {
            throw new IllegalStateException(String.format("The property %s does not exist", key));
        }
    }

    public static String getUrl() {
        String dbType = Configuration.getPropertyOrThrow(DATABASE_TYPE);

        // Mostly for testing - though there is a world where this is a lot cheaper...
        if (dbType.equals("sqlite")) {
            return "jdbc:sqlite://" +
                    Configuration.getPropertyOrThrow(Configuration.DATABASE_NAME) + ".db";
        }

        return "jdbc:postgresql://" +
                Configuration.getPropertyOrThrow(Configuration.DATABASE_HOST) + ":" +
                Configuration.getPropertyOrThrow(Configuration.DATABASE_PORT) + "/" +
                Configuration.getPropertyOrThrow(Configuration.DATABASE_NAME) + "?user=" +
                Configuration.getPropertyOrThrow(Configuration.DATABASE_USERNAME) + "&password=" +
                Configuration.getPropertyOrThrow(Configuration.DATABASE_PASSWORD);
    }
}

