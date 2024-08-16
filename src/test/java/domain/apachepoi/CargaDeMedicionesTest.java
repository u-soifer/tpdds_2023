package domain.apachepoi;

import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static domain.entities.organizacion.ClasificacionOrganizacion.*;
import static domain.entities.organizacion.TipoOrganizacion.*;

public class CargaDeMedicionesTest {
    private Organizacion google;

    @BeforeEach
    public void init() throws IOException {
        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        Ubicacion ubicacionGoogle = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);

        google = new Organizacion("Google", EMPRESA, ubicacionGoogle, EMPRESA_SECTOR_PRIMARIO);
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
    }

    @Test
    @DisplayName("La organizacion carga correctamente todas las actividades")
    public void organizacionCarga11Medicioes() {
        List actividadesDeGoogle = google.getMediciones();
        Assertions.assertEquals(16, actividadesDeGoogle.size());
    }
}