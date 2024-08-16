package domain.entities.validador;

public class ContieneUnaMinuscula extends Validacion{

    @Override
    public Boolean validar(String contrasenia) {
        return contrasenia.matches(".*[a-z].*");
    }

    @Override
    public String reportarError(){
        return "<li>La contraseña debe tener al menos una minuscula</li>";
    }

}
