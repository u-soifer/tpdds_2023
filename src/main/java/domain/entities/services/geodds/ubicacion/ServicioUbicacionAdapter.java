package domain.entities.services.geodds.ubicacion;

import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;

import java.io.IOException;
import java.util.List;

public interface ServicioUbicacionAdapter {

    public List<Pais> listadoDePaises(int offset) throws IOException;
    public List<Provincia> listadoDeProvincias(int offset, int paisId) throws IOException;
    public List<Municipio> listadoDeMunicipios(int offset, int provinciaId) throws IOException;
    public List<Localidad> listadoDeLocalidades(int offset, int municipioId) throws IOException;

}
