package domain.controllers;

import db.EntityManagerHelper;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.Trabajo;
import domain.repositories.RepositorioDeEmpleados;
import domain.repositories.RepositorioDeOrganizaciones;
import domain.repositories.RepositorioDeTrabajos;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrabajoController {
    RepositorioDeEmpleados repoEmpleados;
    RepositorioDeTrabajos repoTrabajos;
    RepositorioDeOrganizaciones repoOrganizaciones;

    public TrabajoController() {
        this.repoEmpleados = new RepositorioDeEmpleados();
        this.repoTrabajos = new RepositorioDeTrabajos();
        this.repoOrganizaciones = new RepositorioDeOrganizaciones();
    }

    public ModelAndView mostrar(Request request, Response response){
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        List<Trabajo> trabajos = repoTrabajos.obtenerTrabajosDe(request.session().attribute("id"));
        List<Trabajo> solicitudes = repoTrabajos.obtenerSolicitudesDe(request.session().attribute("id"));
        List<Organizacion> organizaciones = repoOrganizaciones.buscarTodo();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", empleado.getNombre()+" "+empleado.getApellido());
        parametros.put("empleado", empleado.getId_empleado());
        parametros.put("javascript", "inicio.js");
        parametros.put("trabajos", trabajos);
        parametros.put("solicitudes", solicitudes);
        parametros.put("organizaciones", organizaciones);

        return new ModelAndView(parametros,"trabajos.hbs");
    }

    public Response guardar(Request request, Response response){
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Organizacion organizacion = repoOrganizaciones.buscar(new Integer(request.queryParams("organizacion")));

        organizacion.agregarEmpleadoIngresante(empleado);
        repoOrganizaciones.actualizar(organizacion);

        EntityManagerHelper.closeEntityManager();
        response.redirect("/trabajos");
        return response;
    }
}
