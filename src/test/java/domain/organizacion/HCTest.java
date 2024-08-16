package domain.organizacion;

import domain.entities.actividades.Consumo;
import domain.entities.actividades.Periodicidad;
import domain.entities.actividades.TiposConsumo;
import domain.entities.mediosDeTransporte.*;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Empleado;
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
import java.util.List;

import static domain.entities.mediosDeTransporte.TipoTransportePublico.COLECTIVO;
import static domain.entities.organizacion.ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO;
import static domain.entities.organizacion.TipoDocumento.DNI;
import static domain.entities.organizacion.TipoOrganizacion.EMPRESA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HCTest {
    private Organizacion google;

    private Empleado juan;
    private Empleado pedro;
    private Empleado pablo;

    private Area administracion;
    private Area produccion;
    private Area ventas;

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
    private domain.entities.ubicacion.Parada Parada;
    private Parada Parada2;
    private Parada parada3;

    //Tramos
    private Tramo tramoPie;
    private Tramo tramoVehiculo;
    private Tramo tramoServicioContratado;
    private Tramo tramoColectivo;
    private Tramo tramoPie1;
    private Tramo tramoVehiculo1;
    private Tramo tramoServicioContratado1;
    private Tramo tramoColectivo1;
    private Tramo tramoPie2;
    private Tramo tramoVehiculo2;
    private Tramo tramoServicioContratado2;
    private Tramo tramoColectivo2;
    private Tramo tramoPie3;
    private Tramo tramoVehiculo3;
    private Tramo tramoServicioContratado3;
    private Tramo tramoColectivo3;
    private Tramo tramoPie4;
    private Tramo tramoVehiculo4;
    private Tramo tramoServicioContratado4;
    private Tramo tramoColectivo4;
    private Tramo tramoPie5;
    private Tramo tramoVehiculo5;
    private Tramo tramoServicioContratado5;
    private Tramo tramoColectivo5;


    //Trayecto
    private Trayecto trayecto;
    private Trayecto trayecto1;
    private Trayecto trayecto2;
    private Trayecto trayecto3;
    private Trayecto trayecto4;
    private Trayecto trayecto5;

    //Consumos

    private Consumo consumoTransportePublico;
    private Consumo consumoVehiculoParticular;
    private Consumo consumoServicioContratado;
    private Consumo consumoBiciPie;


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


        //Inicio Organizacion
        Ubicacion ubicacionGoogle = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);
        google = new Organizacion("Google", EMPRESA, ubicacionGoogle, EMPRESA_SECTOR_PRIMARIO);

        //Inicio el biciPie
        distanciaReturnPie = distanciaBiciPieMock();
        mockAdapterPie = mock(ServicioDistanciaAdapter.class);
        distanciaMockPie = mock(Distancia.class);
        when(mockAdapterPie.calcularDistancia(origen, tramoA)).thenReturn(distanciaReturnPie);
        when(distanciaMockPie.getValor()).thenReturn(distanciaReturnPie.getValor());
        //biciPie = new BiciPie(mockAdapterPie);
        biciPie = new BiciPie();

        //Inicio el vehiculoParticular
        distanciaReturnVehiculo = distanciaVehiculoParticularMock();
        mockAdapterVehiculo = mock(ServicioDistanciaAdapter.class);
        distanciaMockVehiculo = mock(Distancia.class);
        when(mockAdapterVehiculo.calcularDistancia(tramoA, tramoB)).thenReturn(distanciaReturnVehiculo);
        when(distanciaMockVehiculo.getValor()).thenReturn(distanciaReturnVehiculo.getValor());
        //vehiculoParticular = new VehiculoParticular(TipoVehiculo.AUTO, TipoCombustible.NAFTA, 4, mockAdapterVehiculo);

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

        //Inicio de los tramos
        tramoPie = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo = new Tramo(tramoC, destino, colectivo, google);
        tramoPie1 = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo1 = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado1 = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo1 = new Tramo(tramoC, destino, colectivo, google);
        tramoPie2 = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo2 = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado2 = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo2 = new Tramo(tramoC, destino, colectivo, google);
        tramoPie3 = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo3 = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado3 = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo3 = new Tramo(tramoC, destino, colectivo, google);
        tramoPie4 = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo4 = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado4 = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo4 = new Tramo(tramoC, destino, colectivo, google);
        tramoPie5 = new Tramo(origen, tramoA, biciPie, google);
        tramoVehiculo5 = new Tramo(tramoA, tramoB, vehiculoParticular, google);
        tramoServicioContratado5 = new Tramo(tramoB, tramoC, servicioContratado, google);
        tramoColectivo5 = new Tramo(tramoC, destino, colectivo, google);

        //Inicio el trayecto
        trayecto = new Trayecto(origen,destino, Periodicidad.ANUAL, "2020");
        trayecto.agregarTramos(tramoPie, tramoVehiculo, tramoServicioContratado, tramoColectivo);
        trayecto1 = new Trayecto(origen,destino, Periodicidad.ANUAL, "2020");
        trayecto1.agregarTramos(tramoPie1, tramoVehiculo1, tramoServicioContratado1, tramoColectivo1);
        trayecto2 = new Trayecto(origen,destino, Periodicidad.ANUAL, "2016");
        trayecto2.agregarTramos(tramoPie2, tramoVehiculo2, tramoServicioContratado2, tramoColectivo2);
        trayecto3 = new Trayecto(origen,destino, Periodicidad.MENSUAL, "03/2018");
        trayecto3.agregarTramos(tramoPie3, tramoVehiculo3, tramoServicioContratado3, tramoColectivo3);
        trayecto4 = new Trayecto(origen,destino, Periodicidad.MENSUAL, "03/2016");
        trayecto4.agregarTramos(tramoPie4, tramoVehiculo4, tramoServicioContratado4, tramoColectivo4);
        trayecto5 = new Trayecto(origen,destino, Periodicidad.MENSUAL, "03/2018");
        trayecto5.agregarTramos(tramoPie5, tramoVehiculo5, tramoServicioContratado5, tramoColectivo5);

        //Consumos

        consumoTransportePublico = new Consumo(TiposConsumo.TRANSPORTE_PUBLICO, 5.0);
        consumoVehiculoParticular = new Consumo(TiposConsumo.VEHICULO_PARTICULAR, 20.0);
        consumoServicioContratado = new Consumo(TiposConsumo.SERVICIO_CONTRATADO, 20.0);
        consumoBiciPie = new Consumo(TiposConsumo.BICI_PIE, 0.0);

        TransportePublico.setConsumo(consumoTransportePublico);
        BiciPie.setConsumo(consumoBiciPie);
        VehiculoParticular.setConsumo(consumoVehiculoParticular);
        ServicioContratado.setConsumo(consumoServicioContratado);


        administracion = new Area("administracion");
        produccion = new Area("produccion");
        ventas = new Area("ventas");

        google.agregarAreas(administracion, produccion, ventas);


        juan = new Empleado("juan","perez",DNI,7845624);
        pedro = new Empleado("pedro","perez",DNI,7845624);
        pablo = new Empleado("pablo","perez",DNI,7845624);
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

    @Test
    @DisplayName("Calculo de HC para un empleado")
    public void calculoHCParaUnEmpleado(){
        google.agregarEmpleadoIngresante(juan);

        google.ingresarEmpleado(juan,administracion);

        juan.agregarTrayecto(trayecto);

        /*tramoPie.agregarEmpleado(juan);
        tramoVehiculo.agregarEmpleado(juan);
        tramoServicioContratado.agregarEmpleado(juan);
        tramoColectivo.agregarEmpleado(juan);*/

        Assertions.assertEquals(107, juan.calcularHC());
    }


    @Test
    @DisplayName("Calculo de HC para una organizacion sin mediciones - Modalidad Anual")
    public void calculoHCParaUnaOrganizacionSinMedicionesAnual(){
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto);
        pedro.agregarTrayecto(trayecto1);
        pablo.agregarTrayecto(trayecto2);

        Assertions.assertEquals(214, google.calcularHC(2020));
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion sin mediciones - Modalidad Mensual")
    public void calculoHCParaUnaOrganizacionSinMedicionesMensual(){
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto3);
        pedro.agregarTrayecto(trayecto4);
        pablo.agregarTrayecto(trayecto5);
        Assertions.assertEquals(214, google.calcularHC(2018, 03));
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion sin empleados - Modalidad Anual")
    public void calculoHCParaUnaOrganizacionSinEmpleadosAnual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");

        Assertions.assertEquals(1288.0, google.calcularHC(2020));
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion sin empleados - Modalidad Mensual")
    public void calculoHCParaUnaOrganizacionSinEmpleadosMensual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");

        Assertions.assertEquals(2984.0, google.calcularHC(2018, 03));
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion con mediciones- Modalidad Anual")
    public void calculoHCParaUnaOrganizacionConMedicionesAnual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto);
        pedro.agregarTrayecto(trayecto1);
        pablo.agregarTrayecto(trayecto2);

        Assertions.assertEquals(1288.0 + 214, google.calcularHC(2020));
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion con mediciones- Modalidad Mensual")
    public void calculoHCParaUnaOrganizacionConMedicionesMensual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto3);
        pedro.agregarTrayecto(trayecto4);
        pablo.agregarTrayecto(trayecto5);

        Assertions.assertEquals(214.0 + 2984.0, google.calcularHC(2018, 03));
    }

    @Test
    @DisplayName("Impacto de un empleado en una organizacion- Modalidad Anual")
    public void impactoDeUnEmpleadoOrganizacionAnual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto);
        pedro.agregarTrayecto(trayecto1);
        pablo.agregarTrayecto(trayecto2);

        Assertions.assertEquals(7.123834886817576, google.impactoDeEmpleado(2020, juan));
    }

    @Test
    @DisplayName("Impacto de un empleado en una organizacion- Modalidad Mensual")
    public void impactoDeUnEmpleadoOrganizacionMensual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,produccion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto3);
        pedro.agregarTrayecto(trayecto4);
        pablo.agregarTrayecto(trayecto5);

        Assertions.assertEquals(3.3458411507191994, google.impactoDeEmpleado(2018, 03,juan));
    }


    @Test
    @DisplayName("IndicadorDeArea - Modalidad Anual")
    public void IndicadorDeAreaAnual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,administracion);
        google.ingresarEmpleado(pablo,ventas);

        juan.agregarTrayecto(trayecto);
        pedro.agregarTrayecto(trayecto1);
        pablo.agregarTrayecto(trayecto2);

        Assertions.assertEquals(107, google.indicadorDeArea(2020, administracion));
    }

    @Test
    @DisplayName("IndicadorDeArea - Modalidad Mensual")
    public void IndicadorDeAreaMensual() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        google.ingresarEmpleado(juan,administracion);
        google.ingresarEmpleado(pedro,administracion);
        google.ingresarEmpleado(pablo,administracion);

        juan.agregarTrayecto(trayecto3);
        pedro.agregarTrayecto(trayecto4);
        pablo.agregarTrayecto(trayecto5);

        Assertions.assertEquals(71.33333333333333, google.indicadorDeArea(2018, 03, administracion));
    }

    @Test
    @DisplayName("La organizacion carga correctamente todas las mediciones")
    public void organizacionCarga11Actividades() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");
        List actividadesDeGoogle = google.getMediciones();
        Assertions.assertEquals(16, actividadesDeGoogle.size());
    }

    @Test
    @DisplayName("Calculo de HC para una organizacion sin empleados - Modalidad Mensual - Prueba con mes de dos digitos")
    public void calculoHCParaUnaOrganizacionSinEmpleadosMensualExtra() throws IOException {
        google.cargarExcel("src/main/resources/TestDA11Mediciones.xlsx");

        Assertions.assertEquals(509.07000000000005, google.calcularHC(2016, 11));
    }


}
