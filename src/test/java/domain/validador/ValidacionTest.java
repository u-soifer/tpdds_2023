package domain.validador;


import domain.entities.validador.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ValidacionTest {

    private Validador validador;
    private TieneLongitudCorrecta longCorrecta;
    private ContieneUnaMayuscula contMayuscula;
    private ContieneUnaMinuscula contMinuscula;
    private ContieneUnNumero contNumero;
    private EsContraseniaSencilla esSencilla;


    @BeforeEach
    public void init() throws IOException {

        validador = new Validador();

        longCorrecta = new TieneLongitudCorrecta(8);
        contMayuscula = new ContieneUnaMayuscula();
        contMinuscula = new ContieneUnaMinuscula();
        contNumero = new ContieneUnNumero();
        esSencilla = new EsContraseniaSencilla();
    }

    @Test
    @DisplayName("La contraseña 1234567 no pasa la validacion de longitud")
    public void contraseniaNoPasaLongitud(){
        validador.agregarValidacion(longCorrecta);
        Assertions.assertFalse(validador.esContraseniaValida("1234567"));
    }

    @Test
    @DisplayName("La contraseña 12345678 no pasa la validacion de mayuscula")
    public void contraseniaNoPasaMayuscula(){
        validador.agregarValidacion(contMayuscula);
        Assertions.assertFalse(validador.esContraseniaValida("12345678"));
    }

    @Test
    @DisplayName("La contraseña 12345678 no pasa la validacion de minuscula")
    public void contraseniaNoPasaMinuscula(){
        validador.agregarValidacion(contMinuscula);
        Assertions.assertFalse(validador.esContraseniaValida("12345678"));
    }

    @Test
    @DisplayName("La contraeña abcdefgh no pasa la validacion de contiene un numero")
    public void contraseniaNoPasaNumero(){
        validador.agregarValidacion(contNumero);
        Assertions.assertFalse(validador.esContraseniaValida("abcdefgh"));
    }

    @Test
    @DisplayName("La contraseña 123456 no pasa la validacion de es sencilla")
    public void contraseniaNoPasaContraseniaSencilla(){
        validador.agregarValidacion(esSencilla);
        Assertions.assertFalse(validador.esContraseniaValida("123456"));
    }

    @Test
    @DisplayName("La contraseña 12345678 pasa la validacion de longitud")
    public void contraseniaPasaLongitud(){
        validador.agregarValidacion(longCorrecta);
        Assertions.assertTrue(validador.esContraseniaValida("12345678"));
    }

    @Test
    @DisplayName("La contraseña 12345678A pasa la validacion de mayuscula")
    public void contraseniaPasaMayuscula(){
        validador.agregarValidacion(contMayuscula);
        Assertions.assertTrue( validador.esContraseniaValida("12345678A"));
    }

    @Test
    @DisplayName("La contraseña 12345678a pasa la validacion de minuscula")
    public void contraseniaPasaMinuscula(){
        validador.agregarValidacion(contMinuscula);
        Assertions.assertTrue(validador.esContraseniaValida("12345678a"));
    }

    @Test
    @DisplayName("La contraseña 12345678 pasa la validacion de contiene un numero")
    public void contraseniaPasaNumero(){
        validador.agregarValidacion(contNumero);
        Assertions.assertTrue(validador.esContraseniaValida("12345678"));
    }

    @Test
    @DisplayName("La contraseña NoEsSencilla pasa la validacion de es sencilla")
    public void contraseniaPasaContraseniaSencilla(){
        validador.agregarValidacion(esSencilla);
        Assertions.assertTrue(validador.esContraseniaValida("NoEsSencilla"));
    }

    @Test
    @DisplayName("La contraseña 123456 no pasa todas las validaciones")
    public void contraseniaNoPasaTodasLasValidaciones(){
        validador.agregarValidacion(longCorrecta, contMayuscula, contMinuscula, contNumero, esSencilla);
        Assertions.assertFalse(validador.esContraseniaValida("123456"));
    }

    @Test
    @DisplayName("La contraseña PasaTodasLasValidaciones pasa todas las validaciones")
    public void contraseniaPasaTodasLasValidaciones(){
        validador.agregarValidacion(longCorrecta, contMayuscula, contMinuscula, contNumero, esSencilla);
        Assertions.assertTrue(validador.esContraseniaValida("Prueba123"));
    }
}
