package domain.trayecto;

import domain.entities.actividades.Periodicidad;
import domain.entities.mediosDeTransporte.*;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;
import domain.entities.services.geodds.entities.*;
import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;
import domain.entities.ubicacion.Parada;
import domain.entities.ubicacion.Ubicacion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static domain.entities.mediosDeTransporte.TipoTransportePublico.COLECTIVO;
import static domain.entities.organizacion.ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO;
import static domain.entities.organizacion.TipoOrganizacion.EMPRESA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TrayectoTest {

    //Paises, provincias, municipios, localidades
    private Pais argentina;
    private Provincia caba;
    private Municipio m_caba;
    private Localidad palermo;

    //Ubicaciones
    private Ubicacion origen;
    private Ubicacion tramoA;
    private Ubicacion tramoB;
    private Ubicacion tramoC;
    private Ubicacion destino;

    //BiciPie
    private BiciPie biciPie;
    private Distancia distanciaReturnPie;
    private Distancia distanciaMockPie;
    private ServicioDistanciaAdapter mockAdapterPie;

    //VehiculoParticular
    private VehiculoParticular vehiculoParticular;
    private Distancia distanciaReturnVehiculo;
    private Distancia distanciaMockVehiculo;
    private ServicioDistanciaAdapter mockAdapterVehiculo;

    //ServicioContratado
    private ServicioContratado servicioContratado;
    private Distancia distanciaReturnServicio;
    private Distancia distanciaMockServicio;
    private ServicioDistanciaAdapter mockAdapterServicio;

    //TransportePublico
    private TransportePublico colectivo;
    private Parada Parada;
    private Parada Parada2;
    private Parada parada3;

    //Tramos
    private Tramo tramoPie;
    private Tramo tramoVehiculo;
    private Tramo tramoServicioContratado;
    private Tramo tramoColectivo;

    //Trayecto
    private Trayecto trayecto;

    //Organizacion

    private Organizacion google;

    @BeforeEach
    public void init() throws IOException {

        //Inicio de pais, provincia, municipio y localidad
        argentina = new Pais(1, "Argentina");
        caba = new Provincia(174, "CABA", argentina);
        m_caba = new Municipio(241, "CABA", caba);
        palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        //Inicio las ubicaciones
        origen  = new Ubicacion(argentina, caba, m_caba, palermo,  "O",  10);
        tramoA  = new Ubicacion(argentina, caba, m_caba, palermo,  "TA", 20);
        tramoB  = new Ubicacion(argentina, caba, m_caba, palermo,  "TB", 30);
        tramoC  = new Ubicacion(argentina, caba, m_caba, palermo,  "TC", 40);
        destino = new Ubicacion(argentina, caba, m_caba, palermo, "D",  50);

        //Inicio el biciPie
        distanciaReturnPie = distanciaBiciPieMock();
        mockAdapterPie = mock(ServicioDistanciaAdapter.class);
        distanciaMockPie = mock(Distancia.class);
        when(mockAdapterPie.calcularDistancia(origen, tramoA)).thenReturn(distanciaReturnPie);
        when(distanciaMockPie.getValor()).thenReturn(distanciaReturnPie.getValor());
        //biciPie = new BiciPie(mockAdapterPie);

        //Inicio el vehiculoParticular
        distanciaReturnVehiculo = distanciaVehiculoParticularMock();
        mockAdapterVehiculo = mock(ServicioDistanciaAdapter.class);
        distanciaMockVehiculo = mock(Distancia.class);
        when(mockAdapterVehiculo.calcularDistancia(tramoA, tramoB)).thenReturn(distanciaReturnVehiculo);
        when(distanciaMockVehiculo.getValor()).thenReturn(distanciaReturnVehiculo.getValor());
        //vehiculoParticular = new VehiculoParticular(TipoVehiculo.AUTO, TipoCombustible.NAFTA, 5, mockAdapterVehiculo);

        //Inicio el servicioContratado
        //TipoServicioContratado uber = new TipoServicioContratado("UBER");
        distanciaReturnServicio = distanciaServicioContratadoMock();
        mockAdapterServicio = mock(ServicioDistanciaAdapter.class);
        distanciaMockServicio = mock(Distancia.class);
        when(mockAdapterServicio.calcularDistancia(tramoB, tramoC)).thenReturn(distanciaReturnServicio);
        when(distanciaMockServicio.getValor()).thenReturn(distanciaReturnServicio.getValor());
        //servicioContratado = new ServicioContratado(uber, mockAdapterServicio);

        //Inicio el colectivo
        colectivo = new TransportePublico(COLECTIVO, "60");
        Distancia dParada1a2 = new Distancia(1.5, "km");
        Distancia dParada2a1 = new Distancia(1.0, "km");
        Distancia dParada2a3 = new Distancia(1.5, "km");
        Distancia dParada3a2 = new Distancia(1.0, "km");
        Distancia dParada3T = new Distancia(1.5, "km");
        Distancia dParadaT3 = new Distancia(1.0, "km");

        Parada parada = Parada.parseParada(tramoC, dParada1a2, dParada2a1);
        Parada parada2 = new Parada(argentina, caba, m_caba, palermo, "B", 30, dParada2a3, dParada3a2);
        Parada parada3 = Parada.parseParada(destino, dParada3T, dParadaT3);
        colectivo.agregarParadas(parada, parada2, parada3);

        //Organizacion

        Ubicacion ubicacionGoogle = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);
        google = new Organizacion("Google", EMPRESA, ubicacionGoogle, EMPRESA_SECTOR_PRIMARIO);



        //Inicio de los tramos
        tramoPie = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo = new Tramo(tramoC, destino, colectivo, google);

        //Inicio el trayecto
        trayecto = new Trayecto(origen,destino, Periodicidad.MENSUAL, "01/2019");
        trayecto.agregarTramos(tramoPie, tramoVehiculo, tramoServicioContratado, tramoColectivo);
    }

    @Test
    @DisplayName("Calcula la distancia recorrida a pie")
    public void calculaDistanciaPie() throws IOException {
        Assertions.assertEquals(0.4, tramoPie.calcularDistancia().valor);
    }

    @Test
    @DisplayName("Calcula la distancia recorrida en un vehiculo particular")
    public void calculaDistanciaVehiculoParticular() throws IOException {
        Assertions.assertEquals(3.2, tramoVehiculo.calcularDistancia().valor);
    }

    @Test
    @DisplayName("Calcula la distancia recorrida en un servicio contratado")
    public void calculaDistanciaServicioContratado() throws IOException {
        Assertions.assertEquals(1.4, tramoServicioContratado.calcularDistancia().valor);
    }

    @Test
    @DisplayName("Calcula la distancia recorrida en colectivo")
    public void calculaDistanciaColectivo() throws IOException {
        Assertions.assertEquals(3, tramoColectivo.calcularDistancia().valor);
    }

    @Test
    @DisplayName("Calcula la distancia de todo el trayecto")
    public void calculaDistanciaTrayecto() throws IOException {
        Assertions.assertEquals(8, trayecto.getDistancia());
    }

    private Distancia distanciaBiciPieMock(){
        return new Distancia(0.4, "KM");
    }

    private Distancia distanciaVehiculoParticularMock(){
        return new Distancia(3.2, "KM");
    }

    private Distancia distanciaServicioContratadoMock(){
        return new Distancia(1.4, "KM");
    }
}
