package domain.entities.services.geodds.factory;

import domain.entities.services.geodds.retrofit.ServicioServicioRetrofit;
import domain.entities.services.geodds.ubicacion.ServicioUbicacionAdapter;

import java.io.IOException;

public class FactoryServicioUbicacion {
    public static ServicioUbicacionAdapter iniciarServicio(MetodoCalculoUbicacion metodo) throws IOException {
        ServicioUbicacionAdapter instancia = null;

        switch (metodo){
            case RETROFIT: instancia = ServicioServicioRetrofit.getInstancia();
        }

        return instancia;
    }
}
