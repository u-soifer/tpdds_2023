package domain.mediosDeTransporte;

import domain.entities.mediosDeTransporte.TransportePublico;
import domain.entities.services.geodds.entities.*;
import domain.entities.ubicacion.Parada;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static domain.entities.mediosDeTransporte.TipoTransportePublico.COLECTIVO;

public class TransportePublicoTest {

    Pais argentina;
    Provincia caba;
    Municipio m_caba;
    Localidad palermo;

    TransportePublico bondi;

    Parada parada;
    Parada parada2;
    Parada parada3;

    @BeforeEach
    public void init() throws IOException {

        argentina = new Pais(1, "Argentina");
        caba = new Provincia(174, "CABA", argentina);
        m_caba = new Municipio(241, "CABA");
        palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        bondi = new TransportePublico(COLECTIVO, "60");

        Distancia dParada1a2 = new Distancia(1.5, "km");
        Distancia dParada2a1 = new Distancia(1.0, "km");
        Distancia dParada2a3 = new Distancia(1.5, "km");
        Distancia dParada3a2 = new Distancia(1.0, "km");
        Distancia dParada3T = new Distancia(1.5, "km");
        Distancia dParadaT3 = new Distancia(1.0, "km");

        parada = new Parada(argentina, caba, m_caba, palermo, "A", 20, dParada1a2, dParada2a1);
        parada2 = new Parada(argentina, caba, m_caba, palermo, "B", 30, dParada2a3, dParada3a2);
        parada3 = new Parada(argentina, caba, m_caba, palermo, "C", 40, dParada3T, dParadaT3);

        bondi.agregarParadas(parada, parada2, parada3);

    }

    @Test
    @DisplayName("El colectivo tiene tres paradas")
    public void transportePublicoTieneTresParadas() {
        Assertions.assertEquals(3, bondi.getParadas().size());
    }

    @Test
    @DisplayName("Calcula la distancia de la parada 1 a la parada 3")
    public void transportePublicoCalculaDistanciaOrden() {
        Assertions.assertEquals(3, bondi.calcularDistancia(parada, parada3).valor);
    }

    @Test
    @DisplayName("Calcula la distancia de la parada 3 a la parada 1")
    public void transportePublicoCalculaDistanciaDesorden() {
        Assertions.assertEquals(2, bondi.calcularDistancia(parada3, parada).valor);
    }
}
