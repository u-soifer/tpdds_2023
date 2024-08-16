package domain.config;

import domain.entities.config.Config;

import java.io.IOException;

public class ConfigStaticTest {


    public static void main(String[] args) throws IOException {
        System.out.println(Config.getProperty("mail"));
        System.out.println(Config.getProperty("token"));
    }

}
