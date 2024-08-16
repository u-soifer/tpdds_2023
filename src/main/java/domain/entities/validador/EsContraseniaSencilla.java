package domain.entities.validador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EsContraseniaSencilla extends Validacion{
    ArrayList<String> clavesAValidar = new ArrayList<String>();

    public EsContraseniaSencilla() throws IOException{
        BufferedReader bufReader = new BufferedReader(new FileReader("src/main/resources/top_10k_worst_passwords.txt"));
        String line = bufReader.readLine();

        while (line != null) {
            clavesAValidar.add(line);
            line = bufReader.readLine();
        }
        bufReader.close();
    }

    @Override
    public Boolean validar(String contrasenia){
        return !clavesAValidar.contains(contrasenia.toLowerCase());
    }

    @Override
    public String reportarError(){
        return "<li>La contraseña es debil</li>";
    }
}
