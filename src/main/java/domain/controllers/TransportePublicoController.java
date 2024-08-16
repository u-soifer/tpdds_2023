package domain.controllers;

import db.EntityManagerHelper;
import domain.entities.mediosDeTransporte.MedioDeTransporte;
import domain.entities.mediosDeTransporte.TipoTransportePublico;
import domain.entities.mediosDeTransporte.TransportePublico;
import domain.entities.notificaciones.Contacto;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Parada;
import domain.entities.ubicacion.Ubicacion;
import domain.entities.usuario.Usuario;
import domain.repositories.RepositorioDeParadas;
import domain.repositories.RepositorioDeTransportePublico;
import domain.repositories.RepositorioDeUbicaciones;
import domain.repositories.RepositorioDeUsuarios;
import org.javatuples.Septet;
import org.javatuples.Sextet;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransportePublicoController {

    private String nombreMedioDeTransporte = "";
    private String nombreLinea = "";
    private List<Parada> paradas;


    RepositorioDeTransportePublico repoTransportePublico;
    RepositorioDeParadas repoParadas;
    RepositorioDeUsuarios repoUsuarios;
    RepositorioDeUbicaciones repositorioDeUbicaciones;

    public TransportePublicoController(){
        this.repoTransportePublico = new RepositorioDeTransportePublico();
        this.repoParadas = new RepositorioDeParadas();
        this.repoUsuarios = new RepositorioDeUsuarios();
        this.paradas = new ArrayList<>();
        this.repositorioDeUbicaciones = new RepositorioDeUbicaciones();
    }

    public ModelAndView mostrar(Request request, Response response){
        Usuario usuario = repoUsuarios.buscar(request.session().attribute("id"));

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", usuario.getUsuario());
        parametros.put("administrador", true);
        parametros.put("javascript", "medios-de-transporte.js");
        if (nombreLinea.length() > 0) {
            parametros.put("paradas", paradas);
            parametros.put("nombre_de_linea", nombreLinea);
            parametros.put("nombre_medio_de_transporte", nombreMedioDeTransporte);
            nombreLinea = "";
            nombreMedioDeTransporte = "";
        } else {
            parametros.put("lineas_subte", this.obtenerLineas("subte"));
            parametros.put("lineas_colectivo", this.obtenerLineas("colectivo"));
            parametros.put("lineas_tren", this.obtenerLineas("tren"));
            parametros.put("linea", 1);
        }

        return new ModelAndView(parametros,"medios-de-transporte.hbs");
    }

    public Response obtenerParadas(Request request, Response response) {
        TransportePublico tp = repoTransportePublico.buscar(new Integer(request.queryParams("id_transporte")));
        nombreMedioDeTransporte = tp.getTipoTransportePublico().getTransporte();
        nombreLinea = tp.getLinea();
        paradas = tp.getParadas();

        response.redirect("/medios-de-transporte");
        return response;
    }

    public List<TransportePublico> obtenerLineas(String nombreTP) {
        List<TransportePublico> retornar = new ArrayList<>();
        switch (nombreTP) {
            case "subte": retornar = repoTransportePublico.transportesDelTipo(1); break;
            case "tren": retornar = repoTransportePublico.transportesDelTipo(0); break;
            case "colectivo": retornar = repoTransportePublico.transportesDelTipo(2); break;
        }
        return retornar;
    }

    public Response guardar(Request request, Response response) {

        TransportePublico tp = new TransportePublico(
                TipoTransportePublico.valueOf(request.queryParams("transporte_a_crear")),
                request.queryParams("nombre_linea_nueva"));

        repoTransportePublico.agregar(tp);
        response.redirect("/medios-de-transporte");
        return response;

    }

    public Response editar(Request request, Response response){
        Parada parada = repoParadas.buscar(new Integer(request.queryParams("id_ubicacion")));

        Distancia distanciaAnterior = new Distancia(Double.valueOf(request.queryParams("distParadaAnterior")), "KM");
        Distancia distanciaSiguiente = new Distancia(Double.valueOf(request.queryParams("distParadaSiguiente")), "KM");

        parada.setDistanciaParadaAnterior(distanciaAnterior);
        parada.setDistanciaParadaSiguiente(distanciaSiguiente);

        repoParadas.actualizar(parada);

        EntityManagerHelper.closeEntityManager();
        response.redirect("/medios-de-transporte");
        return response;

    }

}
