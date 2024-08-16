package domain.entities.services.geodds.distancia;

import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;

public class ServicioDistancia {

    private ServicioDistanciaAdapter adapter;

    public ServicioDistancia(ServicioDistanciaAdapter adapter) {
        this.adapter = adapter;
    }

    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        return this.adapter.calcularDistancia(origen, destino);
    }

    public void setAdapter(ServicioDistanciaAdapter adapter) {
        this.adapter = adapter;
    }
}
