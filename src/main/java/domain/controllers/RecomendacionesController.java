package domain.controllers;

import db.EntityManagerHelper;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class RecomendacionesController {
    public ModelAndView recomendaciones(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "recomendaciones.hbs");
    }
}
