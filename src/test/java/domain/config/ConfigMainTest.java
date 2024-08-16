package domain.config;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigMainTest {

    public static void main(String[] args) {

        Properties config = new Properties();
        InputStream configInput = null;

        try{
            configInput = new FileInputStream("src/main/resources/config.properties");
            config.load(configInput);
            System.out.println(config.getProperty("mail"));
            System.out.println(config.getProperty("token"));
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error cargando configuración\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


}
