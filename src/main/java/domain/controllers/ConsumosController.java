package domain.controllers;

import db.EntityManagerHelper;
import domain.entities.actividades.Consumo;
import domain.entities.actividades.TiposConsumo;
import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.TipoOrganizacion;
import domain.entities.usuario.Usuario;
import domain.repositories.RepositorioDeConsumos;
import domain.repositories.RepositorioDeUsuarios;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumosController {
    RepositorioDeConsumos repoConsumos;
    RepositorioDeUsuarios repoUsuarios;

    public ConsumosController() {
        this.repoConsumos = new RepositorioDeConsumos();
        this.repoUsuarios = new RepositorioDeUsuarios();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Usuario usuario = repoUsuarios.buscar(request.session().attribute("id"));
        List<Consumo> consumos = repoConsumos.buscarTodo();

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", usuario.getUsuario());
        parametros.put("administrador", true);
        parametros.put("javascript", "consumos.js");
        parametros.put("tiposDeConsumo", consumos);

        return new ModelAndView(parametros,"consumos.hbs");
    }

    public Response guardar(Request request, Response response){
        Consumo consumo = repoConsumos.buscar(new Integer(request.queryParams("idConsumo")));

        Double fe = Double.valueOf(request.queryParams("factorEmision"));
        //String tipoConsumo = request.queryParams("tipoConsumo");

        //consumo.setTipoConsumo(TiposConsumo.valueOf(tipoConsumo));
        consumo.setFE(fe);

        repoConsumos.actualizar(consumo);

        EntityManagerHelper.closeEntityManager();
        response.redirect("/consumos");
        return response;
    }

}
