package domain.entities.reportes;

import domain.entities.actividades.Medicion;
import domain.entities.actividades.Periodicidad;
import domain.entities.actividades.TiposConsumo;
import domain.entities.agentessectoriales.SectorTerritorial;
import db.EntityManagerHelper;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.repositories.RepositorioDeMediciones;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Reporte {
    public void Reporte(){

    }
    public Map<SectorTerritorial, Double> emitirReporteHCPorSectorTerritorial(List<SectorTerritorial> sectorTerritoriales, List<Organizacion> organizaciones){
        Map<SectorTerritorial, Double> reporte = new HashMap<>();
        int size = sectorTerritoriales.size();
        for (int i = 0; i < size; i++)   {
            reporte.put(sectorTerritoriales.get(i), sectorTerritoriales.get(i).ObtenerHCSector(organizaciones));
        }
        return reporte;
    }

    public Map<ClasificacionOrganizacion, Double> emitirReporteHCPorTipoDeOrganizacion(List<Organizacion> organizaciones){
        Map<ClasificacionOrganizacion, Double> reporte = new HashMap<>();
        List<Organizacion> organizacionesOp;

        for (ClasificacionOrganizacion clasificacionOrganizacion : ClasificacionOrganizacion.values()){
            organizacionesOp = organizaciones;
            reporte.put(clasificacionOrganizacion, organizacionesOp.stream().filter(a -> a.getClasificacion().equals(clasificacionOrganizacion)).mapToDouble(a -> a.calcularHC()).sum());
        }
        return reporte;
    }

    public Map<TiposConsumo, Double> emitirReporteComposicionDeHcPorSectorTerritorial(SectorTerritorial sectorTerritorial, List<Organizacion> organizaciones){
        Map<TiposConsumo, Double> reporte = new HashMap<>();
        RepositorioDeMediciones repo = new RepositorioDeMediciones();
        List<Organizacion> organizacionesOP = sectorTerritorial.getOrganizaciones(organizaciones);
        List<Medicion> mediciones = new ArrayList<>();
        int size = organizacionesOP.size();

        mediciones.addAll(organizacionesOP.stream().map(o -> repo.obtenerMedicionesDe(o.getId_organizacion())).collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList()));

        for (TiposConsumo tiposConsumo : TiposConsumo.values()){
            reporte.put(tiposConsumo, mediciones.stream().filter(a -> a.getConsumo().getTiposConsumo().equals(tiposConsumo)).mapToDouble(a -> a.getHuellaCarbon()).sum());
        }

        return reporte;
    }

    public Map<Provincia, Double> emitirReporteComposicionDeHcNivelPais(Pais pais, List<Organizacion> organizaciones){
        Map<Provincia, Double> reporte = new HashMap<>();
        List<Organizacion> organizacionesOP = organizaciones.stream().filter(o -> o.getLugar().getPais().nombre == pais.nombre).collect(Collectors.toList());
        List<Provincia> provincias = organizacionesOP.stream().map(a-> a.getLugar().getProvincia()).collect(Collectors.toList());
        Provincia provincia;
        int size = provincias.size();
        for (int i = 0; i < size; i++){
            provincia = provincias.get(i);
            Provincia finalProvincia = provincia;
            reporte.put(finalProvincia, organizacionesOP.stream().filter(a ->a.getLugar().getProvincia().equals(finalProvincia)).mapToDouble(a->a.calcularHC()).sum());
        }
        return reporte;
    }

    public Map<TiposConsumo, Double> emitirReporteComposicionDeUnaOrganizacion(Organizacion organizacion){
        RepositorioDeMediciones repo = new RepositorioDeMediciones();
        Map<TiposConsumo, Double> reporte = new HashMap<>();
        List<Medicion> mediciones = repo.obtenerMedicionesDe(organizacion.getId_organizacion());

        for (TiposConsumo tiposConsumo : TiposConsumo.values()){
            reporte.put(tiposConsumo, mediciones.stream().filter(m -> m.getConsumo().getTiposConsumo().equals(tiposConsumo)).mapToDouble(a -> a.getHuellaCarbon()).sum());
        }
        return reporte;
    }

    public Map<Integer, Double> emitirReporteEvolucionHcSectorTerritorial(SectorTerritorial sectorTerritorial, int fechaDesde, int fechaHasta, List<Organizacion> organizaciones){
        Map<Integer, Double> reporte = new HashMap<>();
        List<Organizacion> organizacionesOP = sectorTerritorial.getOrganizaciones(organizaciones);

        for (int i = fechaDesde; i <= fechaHasta; i++)   {
            Integer finalI = i;
            reporte.put(finalI, organizacionesOP.stream().mapToDouble(a -> a.calcularHC(finalI)).sum());
        }
        return reporte;
    }

    public Map<Integer, Double> emitirReporteEvolucionHcOrganizacion(Organizacion organizacion, int fechaDesde, int fechaHasta){
        Map<Integer, Double> reporte = new HashMap<>();
        for (int i = fechaDesde; i <= fechaHasta; i++)   {
            Integer finalI = i;
            reporte.put(finalI, organizacion.calcularHC(finalI));
        }
        return reporte;
    }
}
