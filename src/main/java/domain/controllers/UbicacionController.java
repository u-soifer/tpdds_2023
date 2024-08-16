package domain.controllers;

import com.google.gson.Gson;
import db.EntityManagerHelper;
import domain.entities.organizacion.Empleado;
import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UbicacionController {
    private RepositorioDeEmpleados repoEmpleados;
    private RepositorioDeUbicaciones repoUbicaciones;
    private RepositorioDePaises repoPaises;
    private RepositorioDeProvincias repoProvincias;
    private RepositorioDeMunicipios repoMunicipios;
    private RepositorioDeLocalidades repoLocalidades;

    public UbicacionController() {
        this.repoEmpleados = new RepositorioDeEmpleados();
        this.repoUbicaciones = new RepositorioDeUbicaciones();
        this.repoPaises = new RepositorioDePaises();
        this.repoProvincias = new RepositorioDeProvincias();
        this.repoMunicipios = new RepositorioDeMunicipios();
        this.repoLocalidades = new RepositorioDeLocalidades();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        List<Pais> paises = repoPaises.buscarTodo();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", empleado.getNombre()+" "+empleado.getApellido());
        parametros.put("empleado", empleado.getId_empleado());
        parametros.put("javascript", "ubicaciones.js");
        parametros.put("ubicaciones", empleado.getUbicaciones());
        parametros.put("paises", paises);

        return new ModelAndView(parametros,"ubicaciones.hbs");
    }

    public Response guardar(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));

        Pais pais = repoPaises.buscar(new Integer(request.queryParams("pais")));
        Provincia provincia = repoProvincias.buscar(new Integer(request.queryParams("provincia")));
        Municipio municipio = repoMunicipios.buscar(new Integer(request.queryParams("municipio")));
        Localidad localidad = repoLocalidades.buscar(new Integer(request.queryParams("localidad")));

        Ubicacion ubicacion = new Ubicacion(request.queryParams("nombre"),
                                            pais,
                                            provincia,
                                            municipio,
                                            localidad,
                                            request.queryParams("calle"),
                                            new Integer(request.queryParams("altura")));

        empleado.agregarUbicaciones(ubicacion);
        repoEmpleados.modificar(empleado);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/ubicaciones");
        return response;

    }

    public Response modificar(Request request, Response response) {
        Ubicacion ubicacion = repoUbicaciones.buscar(new Integer(request.params("id_ubicacion")));

        ubicacion.setNombre(request.queryParams("nombre"));
        ubicacion.setPais(repoPaises.buscar(new Integer(request.queryParams("pais"))));
        ubicacion.setProvincia(repoProvincias.buscar(new Integer(request.queryParams("provincia"))));
        ubicacion.setMunicipio(repoMunicipios.buscar(new Integer(request.queryParams("municipio"))));
        ubicacion.setLocalidad(repoLocalidades.buscar(new Integer(request.queryParams("localidad"))));
        ubicacion.setCalle(request.queryParams("calle"));
        ubicacion.setAltura(new Integer(request.queryParams("altura")));

        repoUbicaciones.modificar(ubicacion);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/ubicaciones");
        return response;
    }

    public Response eliminar(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Ubicacion ubicacion = repoUbicaciones.buscar(new Integer(request.params("id_ubicacion")));

        empleado.sacarUbicacion(ubicacion);

        repoEmpleados.modificar(empleado);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/ubicaciones");
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
}
