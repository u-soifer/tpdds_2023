package domain.entities.services.geodds.distancia;

import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;

public interface ServicioDistanciaAdapter {
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino);
}
