package domain.entities.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static Properties config = null;
    private static InputStream configInput;

    private Config() throws IOException {

    }

    private static void iniciarConfig() throws IOException {
        config = new Properties();
        configInput = new FileInputStream("src/main/resources/config.properties");
        config.load(configInput);
    }

    public static String getProperty(String prop) throws IOException {
        if(config == null){
            iniciarConfig();
        }

        return config.getProperty(prop);
    }
}
