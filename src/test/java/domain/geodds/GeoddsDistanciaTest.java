package domain.geodds;

import domain.entities.services.geodds.distancia.ServicioDistancia;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;
import domain.entities.services.geodds.entities.*;
import domain.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.io.IOException;

public class GeoddsDistanciaTest {
        private ServicioDistanciaAdapter mockAdapter;
        private ServicioDistancia servicioDistancia;

        private Pais argentina;
        private Provincia caba;
        private Municipio m_caba;
        private Localidad retiro;
        private Localidad palermo;

        private Ubicacion origen;
        private Ubicacion destino;

    @BeforeEach
    public void init() throws IOException {
        argentina = new Pais(1, "Argentina");
        caba = new Provincia(174, "CABA", argentina);
        m_caba = new Municipio(241, "CABA", caba);
        retiro = new Localidad(5361, "Retiro", 5361, m_caba);
        palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        origen = new Ubicacion(argentina, caba, m_caba, retiro, "A", 20);
        destino = new Ubicacion(argentina, caba, m_caba, palermo, "B", 30);

        this.mockAdapter = mock(ServicioDistanciaAdapter.class);
        servicioDistancia = new ServicioDistancia(mockAdapter);
    }

    @Test
    @DisplayName("DistnaciaGeodds devuelve el valor correctamente")
    public void DistnaciaGeoddsDevuelveValorCorrecto() throws IOException {
        Distancia distanciaReturn = distanciaMock();
        Distancia distanciaMock = mock(Distancia.class);

        when(distanciaMock.getValor()).thenReturn(distanciaReturn.getValor());
        when(mockAdapter.calcularDistancia(origen, destino)).thenReturn(distanciaReturn);

        Assertions.assertEquals(15.3f, this.servicioDistancia.calcularDistancia(origen, destino).getValor());
    }

    @Test
    @DisplayName("DistnaciaGeodds devuelve la unidad correctamente")
    public void DistnaciaGeoddsDevuelveUnidadCorrecta() throws IOException {
        Distancia distanciaReturn = distanciaMock();
        Distancia distanciaMock = mock(Distancia.class);

        when(distanciaMock.getUnidad()).thenReturn(distanciaReturn.getUnidad());
        when(mockAdapter.calcularDistancia(origen, destino)).thenReturn(distanciaReturn);

        Assertions.assertEquals("KM", this.servicioDistancia.calcularDistancia(origen, destino).getUnidad());
    }

    private Distancia distanciaMock(){
        Distancia distancia = new Distancia(15.3, "KM");
        return distancia;
    }
}
