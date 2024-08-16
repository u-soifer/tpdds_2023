package domain.entities.validador;

public class TieneLongitudCorrecta extends Validacion{

    Integer longitudMinima;

    public TieneLongitudCorrecta(Integer longitudMinima) {
        this.longitudMinima = longitudMinima;
    }

    public void setLongitudMinima(Integer longitudMinima){
        this.longitudMinima = longitudMinima;
    }

    @Override
    public Boolean validar(String contrasenia) {
        return contrasenia.length() >= longitudMinima;
    }

    @Override
    public String reportarError(){
        return "<li>La contraseña debe tener una longitud mínima de " + this.longitudMinima + " caracteres</li>";
    }

}
