package domain.controllers;

import db.converters.ClasificacionOrganizacionConverter;
import db.converters.TipoOrganizacionConverter;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.agentessectoriales.SectorTerritorial;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.EstadoTrabajo;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.TipoOrganizacion;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.usuario.Usuario;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.*;
import java.util.stream.Collectors;

public class AgentesSectorialesController {
    private RepositorioDeAgentesSectoriales repositorioDeAgentesSectoriales;
    private RepositorioDeOrganizaciones repositorioDeOrganizaciones;
    private RepositorioDeSectoresTerritoriales repositorioDeSectoresTerritoriales;
    private RepositorioDeProvincias repositorioDeProvincias;
    private RepositorioDeMunicipios repositorioDeMunicipios;
    private RepositorioDeUsuarios repoUsuarios;

    public AgentesSectorialesController() {
        this.repositorioDeAgentesSectoriales = new RepositorioDeAgentesSectoriales();
        this.repositorioDeOrganizaciones = new RepositorioDeOrganizaciones();
        this.repositorioDeSectoresTerritoriales = new RepositorioDeSectoresTerritoriales();
        this.repositorioDeProvincias = new RepositorioDeProvincias();
        this.repositorioDeMunicipios = new RepositorioDeMunicipios();
        this.repoUsuarios = new RepositorioDeUsuarios();
    }


    public ModelAndView mostrarTodos(Request request, Response response) {
        Usuario usuario = repoUsuarios.buscar(request.session().attribute("id"));
        List<AgenteSectorial.AgenteSectorialDTO> agentesSectoriales = repositorioDeAgentesSectoriales.buscarTodo().stream().map(a -> a.convertirADTO()).collect(Collectors.toList());
        List<SectorTerritorial.SectorTerritorialDTO> sectoresTerritoriales = repositorioDeSectoresTerritoriales.buscarTodo().stream().map(s -> s.convertirADTO()).collect(Collectors.toList());
        List<Provincia.ProvinciaDTO> provincias = repositorioDeProvincias.buscarTodo().stream().map(p -> p.convertirADTO()).collect(Collectors.toList());
        List<Municipio> municipios = repositorioDeMunicipios.buscarTodo();
        List<AgenteSectorial.AgenteSectorialDTO> agentesPendientes = repositorioDeAgentesSectoriales.buscarPendientes().stream().map(a -> a.convertirADTO()).collect(Collectors.toList());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", usuario.getUsuario());
        parametros.put("administrador", true);
        parametros.put("javascript", "agentes-sectoriales.js");
        parametros.put("agentesSectoriales", agentesSectoriales);
        parametros.put("sectoresTerritoriales", sectoresTerritoriales);
        parametros.put("municipios", municipios);
        parametros.put("provincias", provincias);
        parametros.put("agentesPendientes", agentesPendientes);


        return new ModelAndView(parametros,"agentes-sectoriales.hbs");
    }

    public Response rechazarSolicitud(Request request, Response response){
        AgenteSectorial agenteSectorial = repositorioDeAgentesSectoriales.buscar(new Integer(request.params("id_agenteSectorial")));
        agenteSectorial.setEstadoSolicitud(EstadoTrabajo.RECHAZADA);
        repositorioDeAgentesSectoriales.actualizar(agenteSectorial);
        response.redirect("/agentes-sectoriales");
        return response;
    }

    public Response aceptarSolicitud(Request request, Response response){
        AgenteSectorial agenteSectorial = repositorioDeAgentesSectoriales.buscar(new Integer(request.params("id_agenteSectorial")));
        agenteSectorial.setEstadoSolicitud(EstadoTrabajo.ACTIVO);
        repositorioDeAgentesSectoriales.actualizar(agenteSectorial);
        response.redirect("/agentes-sectoriales");
        return response;
    }

    public ModelAndView mostrar(Request request, Response response) {
        AgenteSectorial agente = repositorioDeAgentesSectoriales.buscar(request.session().attribute("id"));
        List<SectorTerritorial.SectorTerritorialDTO> sectoresTerritoriales = repositorioDeSectoresTerritoriales.buscarTodo().stream().map(s -> s.convertirADTO()).collect(Collectors.toList());

        Integer estadoTrabajo = this.getEstadoTrabajo(agente);

        Map<String, Object> parametros = new HashMap<>();

        if (estadoTrabajo == 1) {
            SectorTerritorial.SectorTerritorialDTO sectorTerritorial = repositorioDeSectoresTerritoriales.buscar(agente.getIdSectorTerritorial()).convertirADTO();
            List<Organizacion.OrganizacionDTO> organizaciones = repositorioDeOrganizaciones.buscarTodo().stream().map(o -> o.convertirADTO()).collect(Collectors.toList());
            organizaciones = organizaciones.stream().filter(o -> this.perteneceASector(o, sectorTerritorial)).collect(Collectors.toList());

            parametros.put("organizaciones", organizaciones);
            parametros.put("provinciaOMunicipio", this.getUbicacion(sectorTerritorial));
            parametros.put("idSectorTerritorial", 1);
        } else if (estadoTrabajo == 0) {
            parametros.put("sectorSolicitado", 1);
        }

        parametros.put("javascript", "mi-sector-territorial.js");
        parametros.put("nombre", agente.getNombre());
        parametros.put("agenteSectorial", agente.getId_agenteSectorial());
        parametros.put("sectoresTerritoriales", sectoresTerritoriales);

        return new ModelAndView(parametros, "mi-sector-territorial.hbs");
    }

    public Response solicitarVinculacion(Request request, Response response) {
        AgenteSectorial agenteAModificar = repositorioDeAgentesSectoriales.buscar(request.session().attribute("id"));
        SectorTerritorial sector = this.obtenerSectorTerritorial(request.queryParams("sector_territorial"));

        agenteAModificar.setSolicitudVinculacion(sector);
        this.repositorioDeAgentesSectoriales.modificar(agenteAModificar);

        response.redirect("/mi-sector-territorial");
        return response;
    }

    public Response modificarSectorTerritorial(Request request, Response response){
        SectorTerritorial sectorTerritorial = this.repositorioDeSectoresTerritoriales.buscar(new Integer(request.queryParams("id_sectorTerritorial")));
        Provincia provincia = this.obtenerSectorTerritorial(request.queryParams("id_sectorTerritorial")).getProvincia();
        Municipio municipio = this.obtenerSectorTerritorial(request.queryParams("id_sectorTerritorial")).getMunicipio();

        sectorTerritorial.setProvincia(provincia);
        sectorTerritorial.setMunicipio(municipio);
        this.repositorioDeSectoresTerritoriales.modificar(sectorTerritorial);

        response.redirect("/agentes-sectoriales");
        return response;
    }

    public Response eliminarSectorTerritorial(Request request, Response response){
        SectorTerritorial sectorTerritorial = repositorioDeSectoresTerritoriales.buscar(new Integer(request.params("id_sectorTerritorial")));
        repositorioDeSectoresTerritoriales.eliminar(sectorTerritorial);
        response.redirect("/agentes-sectoriales");
        return response;
    }

    public SectorTerritorial obtenerSectorTerritorial(String nombreDeSector) {
        List<SectorTerritorial> sectores = repositorioDeSectoresTerritoriales.buscarTodo();

        sectores = sectores.stream().filter(s -> Objects.equals(this.getUbicacion(s.convertirADTO()), nombreDeSector)).collect(Collectors.toList());

        SectorTerritorial sector = sectores.get(0);
        return sector;
    }

    public Integer obtenerIdSectorTerritorialDeAgente(AgenteSectorial agente) {
        if(agente.getSectorTerritorial() != null) return agente.getIdSectorTerritorial();
        else return 0;
    }

    public Integer getEstadoTrabajo(AgenteSectorial agente) {
        if(agente.getEstadoSolicitud() != null) return agente.getEstadoSolicitud().ordinal();
        else return 2;
    }

    public Integer getIdMunicipioDeSector(SectorTerritorial.SectorTerritorialDTO sector) {
        if (sector.municipio != null) return sector.getMunicipio().id;
        else return -1;
    }

    public Integer getIdProvinciaDeSector(SectorTerritorial.SectorTerritorialDTO sector) {
        if (sector.provincia != null) return sector.getProvincia().id;
        else return -2;
    }

    public Integer getIdMunicipioOrganizacion(Organizacion.OrganizacionDTO organizacion) {
        if (organizacion.getUbicacionOrganizacion() != null) {
            if (organizacion.getUbicacionOrganizacion().getMunicipio() != null) return organizacion.getUbicacionOrganizacion().getIdMunicipio();
            else return -2;
        } else return -3;
    }

    public Integer getIdProvinciaOrganizacion(Organizacion.OrganizacionDTO organizacion) {
        if (organizacion.getUbicacionOrganizacion() != null) {
            if (organizacion.getUbicacionOrganizacion().getProvincia() != null) return organizacion.getUbicacionOrganizacion().getIdProvincia();
            else return -2;
        } else return -3;
    }

    public Boolean perteneceASector(Organizacion.OrganizacionDTO organizacion, SectorTerritorial.SectorTerritorialDTO sector) {
        if( (this.getIdMunicipioOrganizacion(organizacion) == this.getIdMunicipioDeSector(sector))
         || (this.getIdProvinciaOrganizacion(organizacion) == this.getIdProvinciaDeSector(sector)) ) {
            return true;
        } else return false;
    }

    public String getUbicacion(SectorTerritorial.SectorTerritorialDTO sectorTerritorial) {
        if (sectorTerritorial.getProvincia() != null) { return sectorTerritorial.getProvincia().nombre; }
        else { return sectorTerritorial.getMunicipio().nombre; }
    }

    public Response crear(Request request, Response response) {
        Provincia provincia;
        Municipio municipio;
        SectorTerritorial sectorTerritorial = new SectorTerritorial();

        if (request.queryParams("sector_T").toLowerCase() == "provincia") {
            provincia = repositorioDeProvincias.buscar(new Integer(request.queryParams("sector-provincia")));
            sectorTerritorial.setProvincia(provincia);
            repositorioDeSectoresTerritoriales.guardar(sectorTerritorial);
        } else if (request.queryParams("sector_T").toLowerCase() == "municipio") {
            municipio = repositorioDeMunicipios.buscar(new Integer(request.queryParams("sector-municipio")));
            sectorTerritorial.setMunicipio(municipio);
            repositorioDeSectoresTerritoriales.guardar(sectorTerritorial);
        }

        response.redirect("/agentes-sectoriales");
        return response;
    }

    public Response guardar(Request request, Response response) {
        SectorTerritorial sectorTerritorial = repositorioDeSectoresTerritoriales.buscar(request.session().attribute("id"));
        Provincia provincia = repositorioDeProvincias.buscar(new Integer(request.queryParams("provincia")));
        Municipio municipio = repositorioDeMunicipios.buscar(new Integer(request.queryParams("municipio")));

        sectorTerritorial.setProvincia(provincia);
        sectorTerritorial.setMunicipio(municipio);

        repositorioDeSectoresTerritoriales.guardar(sectorTerritorial);
        response.redirect("/agentes-sectoriales");
        return response;
    }




}
