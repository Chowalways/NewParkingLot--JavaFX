package config;

import java.util.ResourceBundle;

public class ConfigManager {
    private static ResourceBundle _rb;

    // instance value
    private static ResourceBundle getResourceBundle() {
        if(_rb == null) {
            new ConfigManager("config");
        }
        return _rb;
    }

    public static String getString(String key) {
        ResourceBundle rb = getResourceBundle();
        if(rb.keySet().contains(key)) {
            return rb.getString(key);
        }
        return "Wrong Key";
    }

    public static void init(String filename) {
        new ConfigManager(filename);
    }

    public ConfigManager(String name) {
        ResourceBundle rb = ResourceBundle.getBundle(name);
        ConfigManager._rb = rb;

    }

}
