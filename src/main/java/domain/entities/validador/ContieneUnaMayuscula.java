package domain.entities.validador;

public class ContieneUnaMayuscula extends Validacion{

    @Override
    public Boolean validar(String contrasenia) {
        return contrasenia.matches(".*[A-Z].*");
    }

    @Override
    public String reportarError(){
        return "<li>La contraseña debe tener al menos una mayuscula</li>";
    }
}
