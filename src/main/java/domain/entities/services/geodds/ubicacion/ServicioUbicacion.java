package domain.entities.services.geodds.ubicacion;

import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Provincia;

import java.io.IOException;
import java.util.List;

public class ServicioUbicacion {
    private ServicioUbicacionAdapter adapter;

    public ServicioUbicacion(ServicioUbicacionAdapter adapter) {
        this.adapter = adapter;
    }

    public void setAdapter(ServicioUbicacionAdapter adapter) {
        this.adapter = adapter;
    }

    public  List<Pais> paises(int offset) throws IOException {
        return this.adapter.listadoDePaises(offset);
    }

    public List<Provincia> provincias(int offset, int paisId) throws IOException {
        return this.adapter.listadoDeProvincias(offset, paisId);
    }

    public List<Municipio> municipios(int offset, int provinciaId) throws IOException {
        return this.adapter.listadoDeMunicipios(offset, provinciaId);
    }

    public List<Localidad> localidades(int offset, int municipioId) throws IOException {
        return this.adapter.listadoDeLocalidades(offset, municipioId);
    }
}