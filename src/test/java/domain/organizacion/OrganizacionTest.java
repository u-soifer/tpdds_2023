package domain.organizacion;


import domain.entities.organizacion.Area;
import domain.entities.organizacion.Empleado;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static domain.entities.organizacion.ClasificacionOrganizacion.*;
import static domain.entities.organizacion.TipoDocumento.*;
import static domain.entities.organizacion.TipoOrganizacion.*;

public class OrganizacionTest {

    private Organizacion google;

    private Empleado juan;
    private Empleado pedro;
    private Empleado pablo;

    private Area administracion;
    private Area produccion;
    private Area ventas;

    private Pais argentina;
    private Provincia caba;
    private Municipio m_caba;
    private Localidad palermo;

    @BeforeEach
    public void init() throws IOException {
        juan = new Empleado("juan","perez",DNI,7845624);
        pedro = new Empleado("pedro","perez",DNI,7845624);
        pablo = new Empleado("pablo","perez",DNI,7845624);

        administracion =  new Area("administracion");
        produccion = new Area("produccion");
        ventas = new Area("ventas");

        Pais argentina = new Pais(1, "Argentina");
        Provincia caba = new Provincia(174, "CABA", argentina);
        Municipio m_caba = new Municipio(241, "CABA", caba);
        Localidad palermo = new Localidad(5354, "Palermo", 2659, m_caba);

        Ubicacion ubicacionGoogle = new Ubicacion(argentina, caba, m_caba, palermo, "A", 20);

        google = new Organizacion("Google", EMPRESA, ubicacionGoogle, EMPRESA_SECTOR_PRIMARIO);

        google.agregarAreas(administracion, produccion, ventas);
    }

    @Test
    @DisplayName("Un empleado se agrega y se quita a la lista de Ingresantes")
    public void ingresanteSeSumaYSeQuitaAListaIngresantes(){

        google.agregarEmpleadoIngresante(juan);
        google.agregarEmpleadoIngresante(pedro);
        google.agregarEmpleadoIngresante(pablo);

        List<Empleado> aux = new ArrayList<>();
        aux.add(pedro);
        aux.add(pablo);

        google.sacarEmpleadoIngresante(juan);

        Assertions.assertArrayEquals(aux.toArray(), google.getEmpleadosIngresantes().toArray());
    }

    @Test
    @DisplayName("Obtener empleados de una organizacion")
    public void obtenerEmpleadosOrganizacion(){
        List<Empleado> empl = new ArrayList<>(Arrays.asList(juan, pedro, pablo));
        google.ingresarEmpleado(juan, administracion);
        google.ingresarEmpleado(pedro, produccion);
        google.ingresarEmpleado(pablo, produccion);
        Assertions.assertArrayEquals(empl.toArray(), google.getEmpleados().toArray());

    }

    @Test
    @DisplayName("Obtener area de un empleado de ventas")
    public void obtenerAreaEmpleadoVentas(){
        google.ingresarEmpleado(pablo, ventas);
        Assertions.assertEquals(ventas, google.areaDeEmpleado(pablo));
    }

    @Test
    @DisplayName("Obtener area de un empleado de administracion")
    public void obtenerAreaEmpleadoAdministracion(){
        google.ingresarEmpleado(juan, administracion);
        Assertions.assertEquals(administracion, google.areaDeEmpleado(juan));
    }

    @Test
    @DisplayName("Obtener area de un empleado de produccion")
    public void obtenerAreaEmpleadoProduccion(){
        google.ingresarEmpleado(pedro, produccion);
        Assertions.assertEquals(produccion, google.areaDeEmpleado(pedro));
    }

    @Test
    @DisplayName("Agregar y quitar empleados de una organizacion")
    public void ingresarYQuitarEmpleadosOrganizacion(){
        List<Empleado> aux = new ArrayList<>();

        aux.add(juan);
        aux.add(pablo);

        google.ingresarEmpleado(juan, administracion);
        google.ingresarEmpleado(pedro, produccion);
        google.ingresarEmpleado(pablo, ventas);

        google.quitarEmpleado(pedro);

        Assertions.assertArrayEquals(aux.toArray(), google.getEmpleados().toArray());
    }



}