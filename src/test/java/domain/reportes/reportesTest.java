package domain.reportes;

import domain.entities.actividades.Consumo;
import domain.entities.actividades.TiposConsumo;
import domain.entities.agentessectoriales.SectorTerritorial;
import db.EntityManagerHelper;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.reportes.Reporte;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.repositories.RepositorioDeOrganizaciones;
import domain.repositories.RepositorioDePaises;
import domain.repositories.RepositorioDeSectoresTerritoriales;

import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;

public class reportesTest {

    public static void main(String[] args) throws IOException {
        //Se crea el reporte

        Reporte reporte = new Reporte();

        //Se crean las listas



        Map<SectorTerritorial, Double> listaEmitirReporteHCPorSectorTerritorial = new HashMap<>();
        Map<ClasificacionOrganizacion, Double> listaEmitirReporteHCPorTipoDeOrganizacion = new HashMap<>();
        Map<TiposConsumo, Double> listaEmitirReporteComposicionDeHcPorSectorTerritorial = new HashMap<>();
        Map<Provincia, Double> listaEmitirReporteComposicionDeHcNivelPais = new HashMap<>();
        Map<TiposConsumo, Double> listaEmitirReporteComposicionDeUnaOrganizacion = new HashMap<>();
        Map<Integer, Double> listaEmitirReporteEvolucionHcSectorTerritorial = new HashMap<>();
        Map<Integer, Double> listaEmitirReporteEvolucionHcOrganizacion = new HashMap<>();




        //Obtener algunas clases

        RepositorioDeSectoresTerritoriales repositorioDeSectoresTerritoriales = new RepositorioDeSectoresTerritoriales();
        RepositorioDeOrganizaciones repositorioDeOrganizaciones = new RepositorioDeOrganizaciones();
        RepositorioDePaises repositorioDePaises = new RepositorioDePaises();
        List<SectorTerritorial> sectoresTerritoriales = repositorioDeSectoresTerritoriales.buscarTodo();
        List<Organizacion> organizaciones = repositorioDeOrganizaciones.buscarTodo();
        SectorTerritorial sectorTerritorial = repositorioDeSectoresTerritoriales.buscar(149);
        Pais argentina = repositorioDePaises.buscar(9);
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(9);

        //Se cargan las listas


        listaEmitirReporteHCPorSectorTerritorial = reporte.emitirReporteHCPorSectorTerritorial(sectoresTerritoriales, organizaciones);
        listaEmitirReporteHCPorTipoDeOrganizacion = reporte.emitirReporteHCPorTipoDeOrganizacion(organizaciones);
        listaEmitirReporteComposicionDeHcPorSectorTerritorial = reporte.emitirReporteComposicionDeHcPorSectorTerritorial(sectorTerritorial, organizaciones);
        listaEmitirReporteComposicionDeHcNivelPais = reporte.emitirReporteComposicionDeHcNivelPais(argentina, organizaciones.stream().filter(o -> o.getLugar() != null).collect(Collectors.toList()));
        listaEmitirReporteComposicionDeUnaOrganizacion = reporte.emitirReporteComposicionDeUnaOrganizacion(organizacion);
        listaEmitirReporteEvolucionHcSectorTerritorial = reporte.emitirReporteEvolucionHcSectorTerritorial(sectorTerritorial, 2019, 2022, organizaciones);
        listaEmitirReporteEvolucionHcOrganizacion = reporte.emitirReporteEvolucionHcOrganizacion(organizacion, 2019, 2022);

        /*
        System.out.println(listaEmitirReporteComposicionDeHcNivelPais);
        System.out.println(listaEmitirReporteComposicionDeHcPorSectorTerritorial);
        System.out.println(listaEmitirReporteComposicionDeUnaOrganizacion);
        System.out.println(listaEmitirReporteEvolucionHcOrganizacion);
        System.out.println(listaEmitirReporteEvolucionHcSectorTerritorial);
        System.out.println(listaEmitirReporteHCPorSectorTerritorial);
        System.out.println(listaEmitirReporteHCPorTipoDeOrganizacion);



        //Se guardan en arrays de objetos los reportes para imprimir
        Object[] arrayOfObjects = new Object[listaEmitirReporteHCPorSectorTerritorial.size()];
        for (int i = 0; i < listaEmitirReporteHCPorSectorTerritorial.size(); i++) {
            arrayOfObjects[i] = listaEmitirReporteHCPorSectorTerritorial.get(i);
        }
        Object[] arrayOfObjects0 = new Object[listaEmitirReporteHCPorTipoDeOrganizacion.size()];
        for (int i = 0; i < listaEmitirReporteHCPorTipoDeOrganizacion.size(); i++) {
            arrayOfObjects0[i] = listaEmitirReporteHCPorTipoDeOrganizacion.get(i);
        }
        Object[] arrayOfObjects1 = new Object[listaEmitirReporteComposicionDeHcPorSectorTerritorial.size()];
        for (int i = 0; i < listaEmitirReporteComposicionDeHcPorSectorTerritorial.size(); i++) {
            arrayOfObjects1[i] = listaEmitirReporteComposicionDeHcPorSectorTerritorial.get(i);
        }
        Object[] arrayOfObjects2 = new Object[listaEmitirReporteComposicionDeHcNivelPais.size()];
        for (int i = 0; i < listaEmitirReporteComposicionDeHcNivelPais.size(); i++) {
            arrayOfObjects2[i] = listaEmitirReporteComposicionDeHcNivelPais.get(i);
        }
        Object[] arrayOfObjects3 = new Object[listaEmitirReporteComposicionDeUnaOrganizacion.size()];
        for (int i = 0; i < listaEmitirReporteComposicionDeUnaOrganizacion.size(); i++) {
            arrayOfObjects3[i] = listaEmitirReporteComposicionDeUnaOrganizacion.get(i);
        }
        Object[] arrayOfObjects4 = new Object[listaEmitirReporteEvolucionHcSectorTerritorial.size()];
        for (int i = 0; i < listaEmitirReporteEvolucionHcSectorTerritorial.size(); i++) {
            arrayOfObjects4[i] = listaEmitirReporteEvolucionHcSectorTerritorial.get(i);
        }
        Object[] arrayOfObjects5 = new Object[listaEmitirReporteEvolucionHcOrganizacion.size()];
        for (int i = 0; i < listaEmitirReporteEvolucionHcOrganizacion.size(); i++) {
            arrayOfObjects5[i] = listaEmitirReporteEvolucionHcOrganizacion.get(i);
        }


        //Se imprime en pantalla


        System.out.println("\n------------------------ReporteHCPorSectorTerritorial------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects));
        System.out.println("\n------------------------ReporteHCPorTipoDeOrganizacion------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects0));
        System.out.println("\n------------------------ReporteComposicionDeHcPorSectorTerritorial------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects1));
        System.out.println("\n------------------------ReporteComposicionDeHcNivelPais------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects2));
        System.out.println("\n------------------------ReporteComposicionDeUnaOrganizacion------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects3));
        System.out.println("\n------------------------ReporteEvolucionHcSectorTerritorial------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects4));
        System.out.println("\n------------------------ReporteEvolucionHcOrganizacion------------------------\n");
        System.out.println(Arrays.deepToString(arrayOfObjects5));

          */
    }
}
