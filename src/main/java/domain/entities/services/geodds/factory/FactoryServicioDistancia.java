package domain.entities.services.geodds.factory;

import domain.entities.services.geodds.retrofit.ServicioServicioRetrofit;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;

import java.io.IOException;

public class FactoryServicioDistancia {
    public static ServicioDistanciaAdapter iniciarServicio(MetodoCalculoDistancia metodo) throws IOException {
        ServicioDistanciaAdapter instancia = null;

        switch (metodo){
            case RETROFIT: instancia = ServicioServicioRetrofit.getInstancia();
        }

        return instancia;
    }
}
