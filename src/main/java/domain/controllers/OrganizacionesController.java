package domain.controllers;

import db.EntityManagerHelper;
import db.converters.ClasificacionOrganizacionConverter;
import db.converters.TipoOrganizacionConverter;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.TipoOrganizacion;
import domain.entities.usuario.Usuario;
import domain.repositories.RepositorioDeOrganizaciones;
import domain.repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizacionesController {

    RepositorioDeOrganizaciones repoDeOrganizaciones;
    RepositorioDeUsuarios repoUsuarios;

    public OrganizacionesController() {
        repoUsuarios = new RepositorioDeUsuarios();
        repoDeOrganizaciones = new RepositorioDeOrganizaciones();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Usuario usuario = repoUsuarios.buscar(request.session().attribute("id"));
        List<Organizacion> organizaciones = repoDeOrganizaciones.buscarTodo();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", usuario.getUsuario());
        parametros.put("administrador", true);
        parametros.put("javascript", "organizaciones.js");
        parametros.put("tiposDeOrganizacion", Arrays.asList(TipoOrganizacion.values()));
        parametros.put("clasificacionesOrganizacion", Arrays.asList(ClasificacionOrganizacion.values()));
        parametros.put("organizaciones", organizaciones);
        return new ModelAndView(parametros,"organizaciones.hbs");
    }

    public Response guardar(Request request, Response response) {
        Organizacion nuevaOrganizacion = new Organizacion();

        String razonSocial = request.queryParams("organizacion-razon-social");
        TipoOrganizacion tipoOrganizacion = TipoOrganizacionConverter.convertToEntityAttribute(new Integer (request.queryParams("organizacion-tipo")));
        ClasificacionOrganizacion clasificacionOrganizacion = ClasificacionOrganizacionConverter.convertToEntityAttribute(new Integer (request.queryParams("organizacion-clasificacion")));

        nuevaOrganizacion.setRazonSocial(razonSocial);
        nuevaOrganizacion.setTipoOrganizacion(tipoOrganizacion);
        nuevaOrganizacion.setClasificacion(clasificacionOrganizacion);

        repoDeOrganizaciones.guardar(nuevaOrganizacion);
        EntityManagerHelper.closeEntityManager();
        response.redirect("/organizaciones");
        return response;
    }

    public Response modificar(Request request, Response response) {

        Organizacion organizacion = repoDeOrganizaciones.buscar(new Integer(request.params("id_organizacion")));

        organizacion.setRazonSocial(request.queryParams("organizacion-razon-social"));
        TipoOrganizacion tipoOrganizacion = TipoOrganizacionConverter.convertToEntityAttribute(new Integer (request.queryParams("organizacion-tipo")));
        organizacion.setTipoOrganizacion(tipoOrganizacion);
        ClasificacionOrganizacion clasificacionOrganizacion = ClasificacionOrganizacionConverter.convertToEntityAttribute(new Integer (request.queryParams("organizacion-clasificacion")));
        organizacion.setClasificacion(clasificacionOrganizacion);

        repoDeOrganizaciones.modificar(organizacion);
        EntityManagerHelper.closeEntityManager();
        response.redirect("/organizaciones");
        return response;
    }

    public Response eliminar(Request request, Response response){
        Organizacion organizacion = repoDeOrganizaciones.buscar(new Integer(request.params("id_organizacion")));
        repoDeOrganizaciones.eliminar(organizacion);
        EntityManagerHelper.closeEntityManager();
        response.redirect("/organizaciones");
        return response;
    }
}
