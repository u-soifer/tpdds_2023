package domain.geodds;

import domain.entities.services.geodds.ubicacion.ServicioUbicacion;
import domain.entities.services.geodds.ubicacion.ServicioUbicacionAdapter;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeoddsUbicacionTest {
    ServicioUbicacion servicioUbicacion;
    ServicioUbicacionAdapter mockAdapter;

    @BeforeEach
    public void init() throws IOException {
        this.mockAdapter = mock(ServicioUbicacionAdapter.class);
        servicioUbicacion = new ServicioUbicacion(mockAdapter);
    }

    @Test
    @DisplayName("UbicacionGeodds devuelve los paises correctamente")
    public void UbicacionGeoddsDevuelvePaisesCorrectos() throws IOException {
        List<Pais> paisesReturn = this.listadoDePaisesMock();
        when(mockAdapter.listadoDePaises(1)).thenReturn(paisesReturn);
        Assertions.assertEquals(4, this.servicioUbicacion.paises(1).size());
    }

    @Test
    @DisplayName("UbicacionGeodds devuelve las provincias correctamente")
    public void UbicacionGeoddsDevuelveProvinciasCorrectas() throws IOException {
        List<Provincia> provinciasReturn = this.listadoDeProvinciasMock();
        when(mockAdapter.listadoDeProvincias(1, 9)).thenReturn(provinciasReturn);
        Assertions.assertEquals(2, this.servicioUbicacion.provincias(1, 9).size());
    }

    @Test
    @DisplayName("UbicacionGeodds devuelve los municipios correctamente")
    public void UbicacionGeoddsDevuelveMunicipiosCorrectos() throws IOException {
        List<Municipio> municipiosReturn = this.listadoDeMunicipiosMock();
        when(mockAdapter.listadoDeMunicipios(1, 15)).thenReturn(municipiosReturn);
        Assertions.assertEquals(1, this.servicioUbicacion.municipios(1, 15).size());
    }

    @Test
    @DisplayName("UbicacionGeodds devuelve las localidades correctamente")
    public void UbicacionGeoddsDevuelveLocalidadesCorrectas() throws IOException {
        List<Localidad> localidadesReturn = this.listadoDeLocalidadesMock();
        when(mockAdapter.listadoDeLocalidades(1, 543)).thenReturn(localidadesReturn);
        Assertions.assertEquals(3, this.servicioUbicacion.localidades(1, 543).size());
    }



    private List<Pais> listadoDePaisesMock(){
        List<Pais> paises = new ArrayList<>();
        paises.add(new Pais(9, "Argentina"));
        paises.add(new Pais(10, "Uruguay"));
        paises.add(new Pais(11, "Chile"));
        paises.add(new Pais(12, "Brasil"));
        return paises;
    }

    private List<Provincia> listadoDeProvinciasMock(){
        Pais argentina = new Pais(9, "Argentina");
        List<Provincia> provincias = new ArrayList<>();
        provincias.add(new Provincia(43, "CABA", argentina));
        provincias.add(new Provincia(43, "Santa Fe", argentina));
        return provincias;
    }

    private List<Municipio> listadoDeMunicipiosMock(){
        List<Municipio> municipios = new ArrayList<>();
        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        municipios.add(new Municipio(1, "A", caba));
        return municipios;
    }

    private List<Localidad> listadoDeLocalidadesMock(){
        List<Localidad> localidades = new ArrayList<>();
        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        localidades.add(new Localidad(4834, "Palermo", 1234, m_caba));
        localidades.add(new Localidad(4834, "Almagro", 1234, m_caba));
        localidades.add(new Localidad(4834, "Flores", 1234, m_caba));
        return localidades;
    }
}
