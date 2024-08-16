package domain.apachepoi;

import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.TipoOrganizacion;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;

import java.io.IOException;

public class CargaDeExcelMainTest {

    public static void main(String[] args) throws IOException {

        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);
        Ubicacion ubicacionGoogle = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);

        Organizacion google = new Organizacion("Google", TipoOrganizacion.EMPRESA, ubicacionGoogle, ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO);
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");

    }
}
