package domain.controllers;

import db.EntityManagerHelper;
import domain.entities.organizacion.*;
import domain.entities.services.geodds.entities.Pais;
import domain.repositories.RepositorioDeAreas;
import domain.repositories.RepositorioDeEmpleados;
import domain.repositories.RepositorioDeOrganizaciones;
import domain.repositories.RepositorioDeTrabajos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

public class VinculacionController {

    private RepositorioDeOrganizaciones repoOrganizaciones;
    private RepositorioDeEmpleados repoEmpleados;
    private RepositorioDeAreas repoAreas;
    private RepositorioDeTrabajos repoTrabajos;

    public VinculacionController() {
        this.repoOrganizaciones = new RepositorioDeOrganizaciones();
        this.repoAreas = new RepositorioDeAreas();
        this.repoTrabajos = new RepositorioDeTrabajos();
        this.repoEmpleados = new RepositorioDeEmpleados();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        List<Area.AreaDTO> areas = organizacion.getAreas()
                .stream().map(a -> a.convertirADTO())
                .collect(Collectors.toList());
                /*
        List<Area.AreaDTO> areas = repoOrganizaciones.obtenerAreas(organizacion.getId_organizacion())
                                    .stream().map(a -> a.convertirADTO())
                                    .collect(Collectors.toList());

                 */
        List<Trabajo.TrabajoDTO> solicitudes = repoTrabajos.solicitudesOrganizacion(organizacion.getId_organizacion())
                                                .stream().map(a -> a.convertirADTO())
                                                .collect(Collectors.toList());



        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", organizacion.getRazonSocial());
        parametros.put("organizacion", organizacion.getId_organizacion());
        parametros.put("areas", areas);
        parametros.put("solicitudes", solicitudes);
        parametros.put("javascript", "vinculacion.js");

        return new ModelAndView(parametros,"vinculacion.hbs");
    }

    public Response aceptar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        Trabajo solicitud = repoTrabajos.buscar(new Integer(request.params("id_trabajo")));
        Empleado empleado = repoEmpleados.buscar(new Integer(solicitud.getIdEmpleado()));

        //System.out.println("El id del trabajo:"+request.params("id_trabajo"));
        //System.out.println("El id del area:"+request.queryParams("area"));
        //TODO no puedo verificarlo mediante form porque me pide completar todos los campos, hay otra alternativa que no sea esta?
        if (request.queryParams("area") == null) {

            response.redirect("/vinculacion");
            return response;
        }
        else {
            Area area = repoAreas.buscar(new Integer(request.queryParams("area")));
            organizacion.ingresarEmpleado(empleado, area);
            //area.ingresarEmpleado(solicitud);
            repoOrganizaciones.actualizar(organizacion);
            repoAreas.actualizar(area);
            repoEmpleados.modificar(empleado);
            response.redirect("/vinculacion");
            return response;
        }
    }


    public Response rechazar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        Trabajo solicitud = repoTrabajos.buscar(new Integer(request.params("id_trabajo")));
        Empleado empleado = repoEmpleados.buscar(new Integer(solicitud.getIdEmpleado()));

        organizacion.rechazarEmpleado(empleado);
        solicitud.cambiarEstado(EstadoTrabajo.RECHAZADA);
        repoOrganizaciones.actualizar(organizacion);
        repoTrabajos.actualizar(solicitud);
        repoEmpleados.modificar(empleado);

        response.redirect("/vinculacion");
        return response;
    }

}
