package domain.controllers;

import domain.entities.actividades.TiposConsumo;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.agentessectoriales.SectorTerritorial;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.reportes.Reporte;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.repositories.*;
import org.javatuples.Pair;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

public class ReportesController {
    private Map<String, Double> reporte;
    private String nombre_reporte;
    private RepositorioDeOrganizaciones repositorioDeOrganizaciones;
    private RepositorioDePaises repositorioDePaises;
    private RepositorioDeSectoresTerritoriales repositorioDeSectoresTerritoriales;
    private RepositorioDeAgentesSectoriales repositorioDeAgentesSectoriales;
    private RepositorioDeMediciones repositorioDeMediciones;

    public ReportesController() {
        this.repositorioDeOrganizaciones = new RepositorioDeOrganizaciones();
        this.repositorioDePaises = new RepositorioDePaises();
        this.repositorioDeSectoresTerritoriales = new RepositorioDeSectoresTerritoriales();
        this.repositorioDeAgentesSectoriales = new RepositorioDeAgentesSectoriales();
        this.repositorioDeMediciones = new RepositorioDeMediciones();
        this.reporte = new HashMap<>();
    }

    public ModelAndView mostrar(Request request, Response response) {
        AgenteSectorial agenteSectorial = repositorioDeAgentesSectoriales.buscar(request.session().attribute("id"));
        List<Pais> paises = this.repositorioDePaises.buscarTodo();
        List<SectorTerritorial> sectores = this.repositorioDeSectoresTerritoriales.buscarTodo();
        List<Organizacion> organizaciones = this.repositorioDeOrganizaciones.buscarTodo();
        List<String> periodos = this.repositorioDeMediciones.obtenerPeriodosAnuales();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", agenteSectorial.getNombre());
        parametros.put("agenteSectorial", agenteSectorial.getId_agenteSectorial());
        parametros.put("javascript", "reportes.js");

        if(reporte.isEmpty()) {
            parametros.put("paises", paises);
            parametros.put("sectores", sectores);
            parametros.put("organizaciones", organizaciones);
            parametros.put("tiposDeOrganizacion", Arrays.asList(ClasificacionOrganizacion.values()));
            parametros.put("periodos", periodos);
            parametros.put("reporte", 1);
        } else {
            parametros.put("nombre_reporte", nombre_reporte);
            parametros.put("datosReporte", this.transformarReporte(reporte));
            reporte.clear();
            nombre_reporte = "";
        }

        return new ModelAndView(parametros, "reportes.hbs");
    }

    public List<Pair<String, Double>> transformarReporte(Map<String, Double> map) {
        List<Pair<String, Double>> pares = new ArrayList<>();
        List<String> objetos = this.transformarObjeto(map);
        List<Double> valores = this.transformarValores(map);

        for (int i = 0; i < objetos.size() ; i++) {
            Pair<String, Double> par = Pair.with(objetos.get(i), valores.get(i));
            pares.add(par);
        }

        return pares;
    }

    public List<String> transformarObjeto(Map<String, Double> map) {
        List<String> objetos = new ArrayList<>();
        map.forEach( (a,b) -> objetos.add(a.toString()) );
        return objetos;
    }

    public List<Double> transformarValores(Map<String, Double> map) {
        List<Double> valores = new ArrayList<>();
        map.forEach( (a,b) -> valores.add(b.doubleValue()) );
        return valores;
    }

    public Response obtenerReporte(Request request, Response response) {
        nombre_reporte = request.queryParams("reporte_seleccionado");
        reporte = this.obtenerDatosReporte(request);
        response.redirect("/reportes");
        return response;
    }

    public String obtenerProvOMunicipio(SectorTerritorial sector){
        if (sector != null) {
            if (sector.getMunicipio() != null) return sector.getMunicipio().getNombre();
            else if (sector.getProvincia() != null) return sector.getProvincia().getNombre();
            else return "";
        } else return "";
    }

    public Map<String, Double> obtenerDatosReporte(Request request) {
        String nombre = request.queryParams("reporte_seleccionado");
        List<SectorTerritorial> sectores = repositorioDeSectoresTerritoriales.buscarTodo();
        List<Organizacion> organizaciones = repositorioDeOrganizaciones.buscarTodo();

        Map<String, Double> datosReporte = new HashMap<>();

        Reporte reporte = new Reporte();
        switch (nombre) {
            case "st":
                Map<SectorTerritorial, Double> lista = reporte.emitirReporteHCPorSectorTerritorial(sectores, organizaciones);
                lista.forEach((a,b) -> datosReporte.put(this.obtenerProvOMunicipio(a), b));
                return datosReporte;
            case "to":
                Map<ClasificacionOrganizacion, Double> lista1 = reporte.emitirReporteHCPorTipoDeOrganizacion(organizaciones);
                lista1.forEach((a,b) -> datosReporte.put(a.getClasificacionOrganizacion(), b));
                return datosReporte;
            case "cst":
                SectorTerritorial sector = repositorioDeSectoresTerritoriales.buscar(new Integer(request.queryParams("sector_territorial")));
                Map<TiposConsumo, Double> lista2 = reporte.emitirReporteComposicionDeHcPorSectorTerritorial(sector, organizaciones);
                lista2.forEach((a,b) -> datosReporte.put(a.toString(), b));
                return datosReporte;
            case "cnp":
                Pais pais = repositorioDePaises.buscar(new Integer(request.queryParams("pais")));
                Map<Provincia, Double> lista3 = reporte.emitirReporteComposicionDeHcNivelPais(pais, organizaciones.stream().filter(o -> o.getLugar() != null).collect(Collectors.toList()));
                lista3.forEach((a,b) -> datosReporte.put(a.nombre, b));
                return datosReporte;
            case "co":
                Organizacion organizacion = repositorioDeOrganizaciones.buscar(new Integer(request.queryParams("organizacion")));
                Map<TiposConsumo, Double> lista4 = reporte.emitirReporteComposicionDeUnaOrganizacion(organizacion);
                lista4.forEach((a,b) -> datosReporte.put(a.toString(), b));
                return datosReporte;
            case "est":
                SectorTerritorial sector1 = repositorioDeSectoresTerritoriales.buscar(new Integer(request.queryParams("sector_territorial")));
                Integer fechaInicio = new Integer(request.queryParams("fecha_inicio"));
                Integer fechaFin = new Integer(request.queryParams("fecha_fin"));
                List<Organizacion> organizaciones1 = repositorioDeOrganizaciones.buscarTodo();
                Map<Integer, Double> lista5 = reporte.emitirReporteEvolucionHcSectorTerritorial(sector1, fechaInicio, fechaFin, organizaciones1);
                lista5.forEach((a,b) -> datosReporte.put(String.valueOf(a), b));
                return datosReporte;
            case "eo":
                Organizacion organizacion1 = repositorioDeOrganizaciones.buscar(new Integer(request.queryParams("organizacion")));
                Integer fechaInicio1 = new Integer(request.queryParams("fecha_inicio"));
                Integer fechaFin1 = new Integer(request.queryParams("fecha_fin"));
                Map<Integer, Double> lista6 = reporte.emitirReporteEvolucionHcOrganizacion(organizacion1, fechaInicio1, fechaFin1);
                lista6.forEach((a,b) -> datosReporte.put(a.toString(), b));
                return datosReporte;
            default:
                return datosReporte;
        }
    }

    public List<String> getValores(List<String> lista) {
        List<String> retorno = new ArrayList<>();
        for(int i = 0; i < lista.size() ; i++) {
            i++;
            retorno.add(lista.get(i));
        }
        return retorno;
    }
}
