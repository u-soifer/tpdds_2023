package domain.controllers;

import domain.entities.actividades.Medicion;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.Trabajo;
import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;
import domain.entities.usuario.TipoUsuario;
import domain.repositories.*;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CalculadoraHC {

    RepositorioDeEmpleados repoEmpleados;
    RepositorioDeOrganizaciones repoOrganizaciones;
    RepositorioDeTrabajos repoTrabajos;
    RepositorioDeTramos repoTramos;
    RepositorioDeTrayectos repoTrayectos;

    public CalculadoraHC(){
        this.repoOrganizaciones = new RepositorioDeOrganizaciones();
        this.repoEmpleados = new RepositorioDeEmpleados();
        this.repoTrabajos = new RepositorioDeTrabajos();
        this.repoTramos = new RepositorioDeTramos();
        this.repoTrayectos = new RepositorioDeTrayectos();
    }

    public ModelAndView mostrar(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();

        if (request.session().attribute("tipo_usuario").toString().equals(TipoUsuario.EMPLEADO.toString())) {
            Empleado empleado = repoEmpleados.buscar(request.session().attribute("id"));
            List<Trayecto.TrayectoDTO> trayectoDTO = empleado.getTrayectos().stream().map(a -> a.convertirADTO()).collect(Collectors.toList());

            parametros.put("nombre", empleado.getNombre() + " " + empleado.getApellido());
            parametros.put("empleado", empleado.getId_empleado());
            parametros.put("javascript", "calculadoraHC.js");
            parametros.put("trayectos", trayectoDTO);
            parametros.put("HCtotal", trayectoDTO.stream().mapToDouble(a -> a.hc).sum());
        }
        else if (request.session().attribute("tipo_usuario").toString().equals(TipoUsuario.ORGANIZACION.toString())) {
            Organizacion organizacion = repoOrganizaciones.buscar(request.session().attribute("id"));
            List<Area.AreaDTO> areas = organizacion.getAreas()
                    .stream().map(a -> a.convertirADTO())
                    .collect(Collectors.toList());
            List<Medicion.MedicionDTO> mediciones = organizacion.getMediciones()
                    .stream().map(a -> a.convertirADTO())
                    .collect(Collectors.toList());

            Double hcTrayectosTotal = this.obtenerHCTrayectos(organizacion);
            Double hcMedicionesTotal = this.obtenerHCMediciones(organizacion);
            Double hcTotal = organizacion.getHc();

            parametros.put("nombre", organizacion.getRazonSocial());
            parametros.put("organizacion", organizacion.convertirADTO().idOrganizacion);
            parametros.put("javascript", "calculadoraHC.js");
            parametros.put("areas", areas);
            parametros.put("hcTrayectosTotal", hcTrayectosTotal.toString());
            parametros.put("mediciones", mediciones);
            parametros.put("hcMedicionesTotal", hcMedicionesTotal.toString());
            parametros.put("hcTotal", hcTotal.toString());
        }
        return new ModelAndView(parametros,"calculadoraHC.hbs");
    }

    private Double obtenerHCMediciones(Organizacion organizacion) {
        List<Medicion> mediciones = organizacion.getMediciones();
        Double hc = mediciones.stream().mapToDouble(m -> m.getHuellaCarbon()).sum();
        return hc;
    }

    private Double obtenerHCTrayectos(Organizacion organizacion) {
        return organizacion.getHc() - this.obtenerHCMediciones(organizacion);
    }
}