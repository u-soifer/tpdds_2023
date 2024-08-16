package domain.controllers;

import com.google.gson.Gson;
import db.EntityManagerHelper;
import db.converters.TipoCombustibleConverter;
import db.converters.TipoVehiculoConverter;
import domain.entities.mediosDeTransporte.*;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.Trabajo;
import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;
import domain.entities.ubicacion.Ubicacion;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TrayectosController {

    RepositorioDeEmpleados repoEmpleados;
    RepositorioDeTrayectos repoTrayectos;
    RepositorioDeTramos repoTramos;
    RepositorioDeUbicaciones repoUbicaciones;
    RepositorioDeServiciosContratados repoServiciosContratados;
    RepositorioDeTiposDeServiciosContratados repoTiposDeServiciosContratados;
    RepositorioDeTransportePublico repoTransportesPublicos;
    RepositorioDeParadas repoParadas;
    RepositorioDeOrganizaciones repoOrganizaciones;
    RepositorioDeTrabajos repoTrabajos;
    RepositorioDeAreas repoAreas;

    public TrayectosController() {
        repoEmpleados = new RepositorioDeEmpleados();
        repoTrayectos = new RepositorioDeTrayectos();
        repoTramos = new RepositorioDeTramos();
        repoUbicaciones = new RepositorioDeUbicaciones();
        repoServiciosContratados = new RepositorioDeServiciosContratados();
        repoTiposDeServiciosContratados = new RepositorioDeTiposDeServiciosContratados();
        repoTransportesPublicos = new RepositorioDeTransportePublico();
        repoParadas = new RepositorioDeParadas();
        repoOrganizaciones = new RepositorioDeOrganizaciones();
        repoTrabajos = new RepositorioDeTrabajos();
        repoAreas = new RepositorioDeAreas();
    }

    public ModelAndView mostrar(Request request, Response response){
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        List<Trayecto> trayectos = repoTrayectos.trayectosDeEmpleado(empleado.getId_empleado());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", empleado.getNombre()+" "+empleado.getApellido());
        parametros.put("empleado", empleado.getId_empleado());
        parametros.put("javascript", "trayectos.js");
        parametros.put("trayectos", trayectos);
        parametros.put("ubicacionesEmpleado", empleado.getUbicaciones());
        parametros.put("tipoVehiculo", Arrays.asList(TipoVehiculo.values()));
        parametros.put("tipoCombustible", Arrays.asList(TipoCombustible.values()));
        parametros.put("tipoTransportePublico", Arrays.asList(TipoTransportePublico.values()));
        parametros.put("tipoServiciosContratados", repoTiposDeServiciosContratados.buscarTodos());
        parametros.put("organizaciones", empleado.getOrganizaciones());

        return new ModelAndView(parametros,"trayectos.hbs");
    }

    public Response guardar(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Ubicacion origen = repoUbicaciones.buscar(new Integer(request.queryParams("trayecto-origen")));
        Ubicacion destino = repoUbicaciones.buscar(new Integer(request.queryParams("trayecto-destino")));
        Trayecto trayecto = new Trayecto(request.queryParams("trayecto-nombre"), origen, destino, empleado);
        empleado.agregarTrayecto(trayecto);
        repoEmpleados.modificar(empleado);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/trayectos");
        return response;
    }

    public Response modificar(Request request, Response response) {
        Trayecto trayecto = repoTrayectos.buscar(new Integer(request.params("id_trayecto")));
        Ubicacion origen = repoUbicaciones.buscar(new Integer(request.queryParams("trayecto-origen")));
        Ubicacion destino = repoUbicaciones.buscar(new Integer(request.queryParams("trayecto-destino")));
        trayecto.setOrigen(origen);
        trayecto.setDestino(destino);
        trayecto.setNombreTrayecto(request.queryParams("trayecto-nombre"));
        repoTrayectos.modificar(trayecto);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/trayectos");
        return response;
    }

    public Response eliminar(Request request, Response response) {
        Trayecto trayecto = repoTrayectos.buscar(new Integer(request.params("id_trayecto")));
        /*for (Tramo tramo : trayecto.getTramos()){
            repoTramos.eliminar(tramo);
        }*/
        repoTrayectos.eliminar(trayecto);

        EntityManagerHelper.closeEntityManager();

        response.redirect("/trayectos");
        return response;
    }

    public String guardarTramo(Request request, Response response) throws IOException {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Trayecto trayecto = repoTrayectos.buscar(new Integer(request.queryParams("idTrayecto")));
        Organizacion organizacion = repoOrganizaciones.buscar(new Integer(request.queryParams("organizacionResponsable")));
        List<Trabajo> trabajos = repoTrabajos.obtenerTrabajosDe(empleado.getId_empleado());
        List<Area> areas = repoOrganizaciones.obtenerAreas(organizacion.getId_organizacion());

        Ubicacion origen;
        Ubicacion destino;

        if(request.queryParams("medioTransporte").equals("tp")){
            origen = repoParadas.buscar(new Integer(request.queryParams("paradaOrigen")));
            destino =  repoParadas.buscar(new Integer(request.queryParams("paradaDestino")));
        }
        else{
            origen = repoUbicaciones.buscar(new Integer(request.queryParams("tramoOrigen")));
            destino = repoUbicaciones.buscar(new Integer(request.queryParams("tramoDestino")));
        }

        Tramo tramo = new Tramo();
        tramo.setOrigen(origen);
        tramo.setDestino(destino);
        tramo.setOrganizacion(organizacion);
        tramo.agregarEmpleado(repoEmpleados.buscar(6));

        switch (request.queryParams("medioTransporte")){
            case "bp":
                tramo.setMedioDeTransporte(new BiciPie());
                break;
            case "tp":
                tramo.setMedioDeTransporte(repoTransportesPublicos.buscar(new Integer(request.queryParams("lineaTransporte"))));
                break;
            case "vp":
                VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoVehiculoConverter.convertToEntityAttribute(new Integer(request.queryParams("tipoVehiculo"))),
                                                                               TipoCombustibleConverter.convertToEntityAttribute(new Integer(request.queryParams("tipoCombustible"))),
                                                                               new Integer(request.queryParams("capacidadVehiculoParticular")));
                tramo.setMedioDeTransporte(vehiculoParticular);
                break;
            case "sc":
                ServicioContratado servicioContratado = new ServicioContratado(repoTiposDeServiciosContratados.buscar(new Integer(request.queryParams("servicioContratado"))),
                                                                               new Integer(request.queryParams("capacidadServicioContratado")));
                tramo.setMedioDeTransporte(servicioContratado);
                repoTramos.agregar(tramo);
                break;
        }
        tramo.calcularDistancia();

        trayecto.agregarTramos(tramo);
        trayecto.calcularDistancia();
        trayecto.calcularHC();
        repoTrayectos.modificar(trayecto);

        trabajos.forEach(t -> t.setHuellaCarbon());
        trabajos.forEach(t -> repoTrabajos.actualizar(t));

        areas.forEach(a -> a.setHuellaCarbon(trabajos));
        areas.forEach(a -> repoAreas.actualizar(a));

        organizacion.sumarHC(tramo.calcularHC());
        repoOrganizaciones.actualizar(organizacion);

        String tabla = tablaDeTramos(new Integer(request.queryParams("idTrayecto")));

        EntityManagerHelper.closeEntityManager();

        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public Object modificarTramo(Request request, Response response) throws IOException {
        Tramo tramo = repoTramos.buscar(new Integer(request.queryParams("idTramo")));
        Ubicacion origen;
        Ubicacion destino;

        if(request.queryParams("medioTransporte").equals("tp")){
            origen = repoParadas.buscar(new Integer(request.queryParams("paradaOrigen")));
            destino =  repoParadas.buscar(new Integer(request.queryParams("paradaDestino")));
        }
        else{
            origen = repoUbicaciones.buscar(new Integer(request.queryParams("tramoOrigen")));
            destino = repoUbicaciones.buscar(new Integer(request.queryParams("tramoDestino")));
        }

        tramo.setOrigen(origen);
        tramo.setDestino(destino);
        tramo.setOrganizacion(repoOrganizaciones.buscar(new Integer(request.queryParams("organizacionResponsable"))));

        switch (request.queryParams("medioTransporte")){
            case "bp":
                tramo.setMedioDeTransporte(new BiciPie());
                break;
            case "tp":
                tramo.setMedioDeTransporte(repoTransportesPublicos.buscar(new Integer(request.queryParams("lineaTransporte"))));
                break;
            case "vp":
                VehiculoParticular vehiculoParticular = new VehiculoParticular(TipoVehiculoConverter.convertToEntityAttribute(new Integer(request.queryParams("tipoVehiculo"))),
                        TipoCombustibleConverter.convertToEntityAttribute(new Integer(request.queryParams("tipoCombustible"))),
                        new Integer(request.queryParams("capacidadVehiculoParticular")));
                tramo.setMedioDeTransporte(vehiculoParticular);
                break;
            case "sc":
                ServicioContratado servicioContratado = new ServicioContratado(repoTiposDeServiciosContratados.buscar(new Integer(request.queryParams("servicioContratado"))),
                        new Integer(request.queryParams("capacidadServicioContratado")));
                tramo.setMedioDeTransporte(servicioContratado);
                repoTramos.agregar(tramo);
                break;
        }

        repoTramos.modificar(tramo);
        String tabla = tablaDeTramos(new Integer(request.queryParams("idTrayecto")));
        EntityManagerHelper.closeEntityManager();
        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public String eliminarTramo(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Tramo tramo = repoTramos.buscar(new Integer(request.queryParams("idTramo")));
        Trayecto trayecto = repoTrayectos.buscar(new Integer(request.queryParams("idTrayecto")));
        Organizacion organizacion = repoOrganizaciones.buscar(new Integer(tramo.getOrganizacion().getId_organizacion()));
        List<Trabajo> trabajos = repoTrabajos.obtenerTrabajosDe(empleado.getId_empleado());
        List<Area> areas = repoOrganizaciones.obtenerAreas(organizacion.getId_organizacion());

        trayecto.eliminarTramo(tramo);
        repoTrayectos.modificar(trayecto);

        List<Trabajo> finalTrabajos = trabajos.stream().filter(t -> t.getEmpleado().getTramos().contains(tramo)).collect(Collectors.toList());
        finalTrabajos.forEach(t -> t.setHuellaCarbon());
        finalTrabajos.forEach(t -> repoTrabajos.actualizar(t));

        areas.forEach(a -> a.setHuellaCarbon(finalTrabajos));
        areas.forEach(a -> repoAreas.actualizar(a));

        organizacion.restarHC(tramo.calcularHC());
        repoOrganizaciones.actualizar(organizacion);

        String tabla = tablaDeTramos(new Integer(request.queryParams("idTrayecto")));

        EntityManagerHelper.closeEntityManager();

        response.type("text/html;charset=utf-8");
        return tabla;
    }


    public String detalleTramo(Request request, Response response) {
        Tramo tramo = repoTramos.buscar(new Integer(request.queryParams("idTramo")));
        Tramo.TramoDTO tramoDTO = tramo.convertirADTO();
        Gson gson = new Gson();
        String jsonTramo = gson.toJson(tramoDTO);

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonTramo;
    }

    public String tramosDeTrayecto(Request request, Response response) {
        String tabla = tablaDeTramos(new Integer(request.queryParams("idTrayecto")));

        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public String tablaDeTramos(Integer idTrayecto){
        Trayecto trayecto = repoTrayectos.buscar(idTrayecto);
        List<Tramo.TramoDTO> tramosDTO = trayecto.getTramos().stream().map(a -> a.convertirADTO()).collect(Collectors.toList());
        String tabla = "";

        for (int i=0; i<tramosDTO.size(); i++){
        //for (Tramo.TramoDTO tramoDTO : tramosDTO){
            tabla += "<tr>\n";
            tabla += "\t<td>"+tramosDTO.get(i).origen+"</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).destino+"</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).medioDeTransporte+"</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).distancia+"</td>\n";
            tabla += "\t<td>\n";
            tabla += "\t\t<button id=\"editTramo-" + i + "-" + tramosDTO.get(i).id_tramo + "\" type=\"button\" class=\"bi bi-pencil btn btn-outline-primary btn-sm edit-tramo\"></button>\n";
            tabla += "\t\t<button id=\"delTramo-" + i + "-" + tramosDTO.get(i).id_tramo + "\" type=\"button\" class=\"bi bi-trash btn btn-outline-danger btn-sm del-tramo\"></button>\n";
            tabla += "\t</td>\n";
            tabla += "</tr>\n";
        }

        return tabla;
    }

    public String lineas(Request request, Response response) {
        List<TransportePublico> transportePublico = repoTransportesPublicos.transportesDelTipo(new Integer(request.queryParams("id_tipoTransporte")));
        Gson gson = new Gson();
        String jsonTransportes = gson.toJson(transportePublico);


        EntityManagerHelper.closeEntityManager();
        response.type("application/json");
        return jsonTransportes;
    }

    public String paradas(Request request, Response response) {
        TransportePublico transportePublico = repoTransportesPublicos.buscar(new Integer(request.queryParams("id_medioDeTransporte")));
        Gson gson = new Gson();
        String jsonParadas = gson.toJson(transportePublico.getParadas());

        EntityManagerHelper.closeEntityManager();

        response.type("application/json");
        return jsonParadas;
    }

    public String tramosCompartidos(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        List<Tramo> tramos = new ArrayList<>();
        for (Trabajo trabajo: empleado.getTrabajos().stream().distinct().collect(Collectors.toList())){
            tramos.addAll(repoTramos.buscarTramosDeOrganizacion(trabajo.getOrganizacion().getId_organizacion()));
        }
        tramos = tramos.stream().filter(a -> a.esCompartible()).collect(Collectors.toList());
        List<Tramo.TramoDTO> tramosDTO = tramos.stream().map(a -> a.convertirADTO()).collect(Collectors.toList());

        String tabla = "";
        for (int i=0; i<tramosDTO.size(); i++){
            tabla += "<tr>\n";
            tabla += "\t<td>"+tramosDTO.get(i).origen+"</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).destino+"</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).acompaniantes+"\t</td>\n";
            tabla += "\t<td>"+tramosDTO.get(i).medioDeTransporte+"</td>\n";
            tabla += "\t<td>\n";
            tabla += "\t\t<button type=\"button\" id=\"tramo-"+ tramosDTO.get(i).id_tramo + "\" class=\"bi bi-car-front-fill btn btn-outline-primary btn-sm compartir-trayecto\"></button>";
            tabla += "\t</td>\n";
            tabla += "</tr>\n";
        }

        EntityManagerHelper.closeEntityManager();

        response.type("text/html;charset=utf-8");
        return tabla;
    }

    public Object compartirTramo(Request request, Response response) {
        Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
        Tramo tramo = repoTramos.buscar(new Integer(request.queryParams("idTramo")));
        Trayecto trayecto = repoTrayectos.buscar(new Integer(request.queryParams("idTrayecto")));
        tramo.agregarEmpleado(empleado);
        trayecto.agregarTramos(tramo);
        repoEmpleados.modificar(empleado);
        String tabla = tablaDeTramos(new Integer(request.queryParams("idTrayecto")));
        EntityManagerHelper.closeEntityManager();
        response.type("text/html;charset=utf-8");
        return tabla;
    }
}
