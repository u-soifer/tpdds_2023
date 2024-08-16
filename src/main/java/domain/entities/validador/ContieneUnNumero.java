package domain.entities.validador;

public class ContieneUnNumero extends Validacion{

    @Override
    public Boolean validar(String contrasenia) {
        return contrasenia.matches(".*[1-9].*");
    }

    @Override
    public String reportarError(){
        return "<li>La contraseña debe tener al menos un numero</li>";
    }
}
