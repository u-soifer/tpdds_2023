package domain.controllers;

import com.google.gson.Gson;
import db.EntityManagerHelper;
import domain.entities.actividades.Medicion;
import domain.entities.notificaciones.Contacto;
import domain.entities.organizacion.*;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;
import domain.entities.ubicacion.Ubicacion;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiOrganizacionController {
    private RepositorioDeOrganizaciones repositorioDeOrganizaciones;
    private RepositorioDeAreas repositorioDeAreas;
    private RepositorioDePaises repoPaises;
    private RepositorioDeProvincias repoProvincias;
    private RepositorioDeMunicipios repoMunicipios;
    private RepositorioDeLocalidades repoLocalidades;
    private RepositorioDeEmpleados repoEmpleados;
    private RepositorioDeTrabajos repositorioDeTrabajos;

    public MiOrganizacionController() {
        this.repositorioDeOrganizaciones = new RepositorioDeOrganizaciones();
        this.repositorioDeAreas = new RepositorioDeAreas();
        this.repoPaises = new RepositorioDePaises();
        this.repoProvincias = new RepositorioDeProvincias();
        this.repoMunicipios = new RepositorioDeMunicipios();
        this.repoLocalidades = new RepositorioDeLocalidades();
        this.repoEmpleados = new RepositorioDeEmpleados();
        this.repositorioDeTrabajos = new RepositorioDeTrabajos();
    }

    public ModelAndView mostrar(Request request, Response response){
        Organizacion.OrganizacionDTO organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id")).convertirADTO();
        List<Area.AreaDTO> areas = organizacion.getAreas()
                                    .stream().map(a -> a.convertirADTO())
                                    .collect(Collectors.toList());
        List<Pais> paises = repoPaises.buscarTodo();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", organizacion.getRazonSocial());
        parametros.put("organizacion", organizacion);
        parametros.put("areas", areas);
        parametros.put("ubicacion", organizacion.getUbicacionOrganizacion());
        parametros.put("paises", paises);
        parametros.put("javascript", "mi-organizacion.js");

        return new ModelAndView(parametros,"mi-organizacion.hbs");
    }


    public Response crear(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        String nombreArea = request.queryParams("nombre");
        Area area = new Area(nombreArea,organizacion);
        repositorioDeOrganizaciones.actualizar(organizacion);
        repositorioDeAreas.actualizar(area);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/organizacion");
        return response;

    }

    public Response modificarArea(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        Area area = repositorioDeAreas.buscar(new Integer(request.params("id_area")));

        area.setNombreDeArea(request.queryParams("nombre"));
        repositorioDeOrganizaciones.actualizar(organizacion);
        repositorioDeAreas.actualizar(area);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/organizacion");
        return response;
    }

    public Response modificarUbi(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        Ubicacion ubicacion = organizacion.getLugar();

        ubicacion.setPais(repoPaises.buscar(new Integer(request.queryParams("pais"))));
        ubicacion.setProvincia(repoProvincias.buscar(new Integer(request.queryParams("provincia"))));
        ubicacion.setMunicipio(repoMunicipios.buscar(new Integer(request.queryParams("municipio"))));
        ubicacion.setLocalidad(repoLocalidades.buscar(new Integer(request.queryParams("localidad"))));
        ubicacion.setCalle(request.queryParams("calle"));
        ubicacion.setAltura(new Integer(request.queryParams("altura")));

        repositorioDeOrganizaciones.modificar(organizacion);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/organizacion");
        return response;
    }

    public Response eliminar(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        Area area = repositorioDeAreas.buscar(new Integer(request.params("id_area")));

        organizacion.eliminarArea(area);
        area.setOrganizacion(null);
        repositorioDeOrganizaciones.actualizar(organizacion);
        repositorioDeAreas.actualizar(area);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/organizacion");
        return response;
    }

    public String provincias(Request request, Response response) {
        List<Provincia> provincias = repoProvincias.provinciasDelPais(new Integer(request.queryParams("id_pais")));
        Gson gson = new Gson();
        String jsonProvincias = gson.toJson(provincias);

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonProvincias;
    }

    public String municipios(Request request, Response response) {
        List<Municipio> municipios = repoMunicipios.municipiosDeProvincia(new Integer(request.queryParams("id_provincia")));
        Gson gson = new Gson();
        String jsonMunicipios = gson.toJson(municipios);

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonMunicipios;
    }

    public String localidades(Request request, Response response) {
        List<Localidad> localidades = repoLocalidades.localidadesDeMunicipio(new Integer(request.queryParams("id_municipio")));
        Gson gson = new Gson();
        String jsonLocalidades = gson.toJson(localidades);

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonLocalidades;
    }

    //Empleados

    public String empleadosDeArea(Request request, Response response) {
        String tabla = tablaDeEmpleados(new Integer(request.queryParams("idArea")));

        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public String tablaDeEmpleados(Integer idArea){
        //TODO No esta recuperando los empleados luego de buscarlo en la base
        Area area = repositorioDeAreas.buscar(idArea);
        List<Empleado.EmpleadoDTO> empleadosDTO = repoEmpleados.buscarEmpleados(idArea).stream().map(a -> a.convertirADTO()).collect(Collectors.toList());
        //List<Empleado.EmpleadoDTO> empleadosDTO = area.getEmpleados().stream().map(a -> a.convertirADTO()).collect(Collectors.toList());
        String tabla = "";

        for (int i=0; i<empleadosDTO.size(); i++){
            tabla += "<tr>\n";
            tabla += "\t<td>"+empleadosDTO.get(i).nombreEmpleado+"</td>\n";
            tabla += "\t<td>"+empleadosDTO.get(i).documentoEmpleado+"</td>\n";
            tabla += "\t<td>\n";
            tabla += "\t\t<button id=\"delEmpleado-" + i + "-" + empleadosDTO.get(i).idEmpleado + "\" type=\"button\" class=\"bi bi-trash btn btn-outline-danger btn-sm del-empleado\"></button>\n";
            tabla += "\t</td>\n";
            tabla += "</tr>\n";
        }

        return tabla;
    }

    public String eliminarEmpleado(Request request, Response response) {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        Empleado empleado = repoEmpleados.buscar(new Integer(request.queryParams("idEmpleado")));
        Area area = repositorioDeAreas.buscar(new Integer(request.queryParams("idArea")));
        Trabajo trabajo = empleado.getTrabajo(organizacion);

        System.out.println("La organizacion:" + organizacion.getRazonSocial() + "El area: " + area.getNombreDeArea() + "El empleado: " + empleado.getNombre());

        trabajo.cambiarEstado(EstadoTrabajo.BAJA);
        area.quitarEmpleado(empleado);

        repositorioDeOrganizaciones.actualizar(organizacion);
        repositorioDeAreas.actualizar(area);
        repositorioDeTrabajos.actualizar(trabajo);

        String tabla = tablaDeEmpleados(area.getId_area());

        EntityManagerHelper.closeEntityManager();

        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public String detalleEmpleado(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(new Integer(request.queryParams("idEmpleado")));
        Empleado.EmpleadoDTO empleadoDTO = empleado.convertirADTO();
        Gson gson = new Gson();
        String jsonEmpleado = gson.toJson(empleadoDTO);

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonEmpleado;
    }

}
