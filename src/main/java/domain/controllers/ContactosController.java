package domain.controllers;

import com.google.gson.Gson;
import domain.entities.notificaciones.Contacto;
import domain.entities.organizacion.Organizacion;
import domain.entities.trayecto.Trayecto;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactosController {
    private RepositorioDeOrganizaciones repoOrganizaciones;
    private RepositorioDeContactos repoContactos;

    public ContactosController() {
        this.repoOrganizaciones = new RepositorioDeOrganizaciones();
        this.repoContactos = new RepositorioDeContactos();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        //List<Contacto> contactos = repoOrganizaciones.contactosDeOrganizacion(organizacion.getId_organizacion());
        List<Contacto> contactos = organizacion.getContactos();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", organizacion.getRazonSocial());
        parametros.put("organizacion", organizacion.getId_organizacion());
        parametros.put("contactos", contactos);
        parametros.put("javascript", "contactos.js");

        return new ModelAndView(parametros,"contactos.hbs");
    }

    public Response guardar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));

        Contacto contacto = new Contacto(
                request.queryParams("nombre"),
                request.queryParams("mail"),
                request.queryParams("telefono"));

        organizacion.agregarContacto(contacto);
        contacto.setOrganizacion(organizacion);
        repoOrganizaciones.actualizar(organizacion);
        response.redirect("/contactos");
        return response;

    }

    public Response modificar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        Contacto contacto = repoContactos.buscar(new Integer(request.params("id_contacto")));


        contacto.setNombre(request.queryParams("nombre"));
        contacto.setMail(request.queryParams("mail"));
        contacto.setTelefono(request.queryParams("telefono"));

        repoContactos.modificar(contacto);
        response.redirect("/contactos");
        return response;
    }

    public Response eliminar(Request request, Response response) {
        Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
        Contacto contacto = repoContactos.buscar(new Integer(request.params("id_contacto")));

        organizacion.sacarContacto(contacto);

        contacto.setOrganizacion(null);
        repoContactos.modificar(contacto);
        response.redirect("/contactos");
        return response;
    }

}
