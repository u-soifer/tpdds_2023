package domain.geodds;

import domain.entities.services.geodds.entities.*;
import domain.entities.services.geodds.factory.FactoryServicioDistancia;
import domain.entities.services.geodds.factory.MetodoCalculoDistancia;
import domain.entities.services.geodds.distancia.ServicioDistancia;
import domain.entities.ubicacion.Ubicacion;

import java.io.IOException;
import java.util.Scanner;

public class GeoddsDistanciaMainTest {


    public static void main(String[] args) throws IOException {

        Scanner entrada = new Scanner(System.in);

        ServicioDistancia servicioDistancia = new ServicioDistancia(FactoryServicioDistancia.iniciarServicio(MetodoCalculoDistancia.RETROFIT));

        Integer localidadOrigenId;
        String  calleOrigen;
        Integer alturaOrigen;
        Integer localidadDestinoId;
        String  calleDestino;
        Integer alturaDestino;

        /*
        localidadOrigenId  = 1
        calleOrigen        = "maipu"
        alturaOrigen       = 100
        localidadDestinoId = 457
        calleDestino       = "O'Higgins"
        alturaDestino      = 200
        */
/*
        System.out.println("Ingrese el ID de la localidad de origen: ");
        localidadOrigenId = Integer.parseInt(entrada.nextLine());

        System.out.println("Ingrese la calle de origen: ");
        calleOrigen = entrada.nextLine();

        System.out.println("Ingrese la altura de origen: ");
        alturaOrigen = Integer.parseInt(entrada.nextLine());

        System.out.println("Ingrese el ID de la localidad de destino: ");
        localidadDestinoId = Integer.parseInt(entrada.nextLine());

        System.out.println("Ingrese la calle de destino: ");
        calleDestino = entrada.nextLine();

        System.out.println("Ingrese la altura de destino: ");
        alturaDestino = Integer.parseInt(entrada.nextLine());
*/
        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA");
        Localidad retiro = new Localidad(5361, "Retiro", 5361, m_caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        Ubicacion origen = new Ubicacion(argentina, caba, m_caba, retiro, "A", 20);
        Ubicacion destino = new Ubicacion(argentina, caba, m_caba, palermo, "B", 30);

        Distancia distancia = servicioDistancia.calcularDistancia(origen, destino);

        System.out.println("Distancia: " + distancia.valor+" "+distancia.unidad);

    }
}
