package domain.geodds;

import domain.entities.services.geodds.factory.FactoryServicioUbicacion;
import domain.entities.services.geodds.factory.MetodoCalculoUbicacion;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.services.geodds.ubicacion.ServicioUbicacion;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class GeoddsUbicacionMainTest {

    public static void main(String[] args) throws IOException {

        Scanner entrada = new Scanner(System.in);
        Integer entradaEntero;

        ServicioUbicacion servicioUbicacion = new ServicioUbicacion(FactoryServicioUbicacion.iniciarServicio(MetodoCalculoUbicacion.RETROFIT));

        List<Pais> paises = servicioUbicacion.paises(1);
        for (Pais pais : paises) {
            System.out.println(pais.id +") "+pais.nombre);
        }

        System.out.println("Seleccione un pais: ");
        entradaEntero = Integer.parseInt(entrada.nextLine());

        List<Provincia> provincias = servicioUbicacion.provincias(1, entradaEntero);
        if(provincias.size() == 0){
            System.out.println("No hay provincias para el pais seleccionado");
            return;
        }
        for (Provincia provincia : provincias) {
            System.out.println(provincia.id +") "+provincia.nombre);
        }

        System.out.println("Seleccione una provincia: ");
        entradaEntero = Integer.parseInt(entrada.nextLine());

        List<Municipio> municipios = servicioUbicacion.municipios(1, entradaEntero);
        if(municipios.size() == 0){
            System.out.println("No hay municipios para la provincia seleccionada");
            return;
        }
        for (Municipio municipio : municipios) {
            System.out.println(municipio.id +") "+municipio.nombre);
        }

        System.out.println("Seleccione un municipio: ");
        entradaEntero = Integer.parseInt(entrada.nextLine());

        List<Localidad> localidades = servicioUbicacion.localidades(1, entradaEntero);
        if (localidades.size() == 0) {
            System.out.println("No hay localidades para el municipio seleccionado");
            return;
        }
        for (Localidad localidad : localidades) {
            System.out.println(localidad.id +") "+localidad.nombre);
        }

        System.out.println("Seleccione una localidad: ");
        entradaEntero = Integer.parseInt(entrada.nextLine());
        for (Localidad localidad : localidades) {
            if(localidad.id == entradaEntero)
                System.out.println("El codigo postal es: "+localidad.codPostal);
        }

    }

}
