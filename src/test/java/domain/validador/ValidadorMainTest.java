package domain.validador;

import domain.entities.validador.*;

import java.io.IOException;
import java.util.Scanner;

public class ValidadorMainTest {

    public static void main(String[] args) throws IOException {

        String password;

        Validador unValidador = new Validador();

        TieneLongitudCorrecta longCorrecta = new TieneLongitudCorrecta(8);
        ContieneUnaMayuscula contMayuscula = new ContieneUnaMayuscula();
        ContieneUnaMinuscula contMinuscula = new ContieneUnaMinuscula();
        ContieneUnNumero contNumero = new ContieneUnNumero();
        EsContraseniaSencilla sencilla = new EsContraseniaSencilla();

        unValidador.agregarValidacion(longCorrecta, contMayuscula, contMinuscula, contNumero, sencilla);

        Scanner input = new Scanner(System.in);
        System.out.println("Porfavor coloque una contraseña: ");
        password = input.nextLine();

        System.out.println(unValidador.esContraseniaValidaConDetalle(password));

    }
}
