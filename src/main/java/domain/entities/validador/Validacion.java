package domain.entities.validador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class Validacion {

    public Boolean pasaValidacion(String contrasenia){
            return validar(contrasenia);
    }

    public String pasaValidacionConDetalle(String contrasenia){
        if(validar(contrasenia))
            return "";
        else {
            return this.reportarError();
        }
    }

    public abstract Boolean validar(String contrasenia);

    public abstract String reportarError();
}
