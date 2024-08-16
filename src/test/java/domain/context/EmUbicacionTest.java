package domain.context;

import db.EntityManagerHelper;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.services.geodds.factory.FactoryServicioUbicacion;
import domain.entities.services.geodds.factory.MetodoCalculoUbicacion;
import domain.entities.services.geodds.ubicacion.ServicioUbicacion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EmUbicacionTest {

    public static void main(String[] args) throws IOException {
        ServicioUbicacion servicioUbicacion = new ServicioUbicacion(FactoryServicioUbicacion.iniciarServicio(MetodoCalculoUbicacion.RETROFIT));

        List<Pais> paises = servicioUbicacion.paises(1);
        List<Provincia> provincias  = new ArrayList<>();
        List<Municipio> municipios  = new ArrayList<>();
        List<Localidad> localidades = new ArrayList<>();

        EntityManagerHelper.beginTransaction();
        for (Pais pais : paises) {
            EntityManagerHelper.getEntityManager().persist(pais);
            provincias.addAll(servicioUbicacion.provincias(1, pais.id));
        }
        EntityManagerHelper.commit();

        EntityManagerHelper.beginTransaction();
        for (Provincia provincia: provincias){
            EntityManagerHelper.getEntityManager().persist(provincia);
            municipios.addAll(servicioUbicacion.municipios(1, provincia.id));
        }
        EntityManagerHelper.commit();

        EntityManagerHelper.beginTransaction();
        for (Municipio municipio: municipios){
            EntityManagerHelper.getEntityManager().persist(municipio);
            localidades.addAll(servicioUbicacion.localidades(1, municipio.id));
        }
        EntityManagerHelper.commit();

        EntityManagerHelper.beginTransaction();
        for (Localidad localidad: localidades){
            EntityManagerHelper.getEntityManager().persist(localidad);
        }
        EntityManagerHelper.commit();

        EntityManagerHelper.closeEntityManager();
    }
}
