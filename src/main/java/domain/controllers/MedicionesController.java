package domain.controllers;

import db.EntityManagerHelper;
import domain.entities.actividades.Medicion;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Organizacion;
import domain.repositories.RepositorioDeMediciones;
import domain.repositories.RepositorioDeOrganizaciones;
import spark.ModelAndView;
import spark.Request;
import spark.Response;


import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;


import javax.servlet.http.Part;
import javax.transaction.*;

public class MedicionesController {

    private RepositorioDeOrganizaciones repositorioDeOrganizaciones;
    private RepositorioDeMediciones repositorioDeMediciones;

    public MedicionesController() {
        this.repositorioDeOrganizaciones = new RepositorioDeOrganizaciones();
        this.repositorioDeMediciones = new RepositorioDeMediciones();
    }

    public ModelAndView mostrar(Request request, Response response){
        Organizacion.OrganizacionDTO organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id")).convertirADTO();
        List<Medicion.MedicionDTO> mediciones = organizacion.getMediciones()
                .stream().map(a -> a.convertirADTO())
                .collect(Collectors.toList());

        Map<String, Object> parametros = new HashMap<>();
        parametros.put("nombre", organizacion.getRazonSocial());
        parametros.put("organizacion", organizacion);
        parametros.put("mediciones", mediciones);
        parametros.put("javascript", "mediciones.js");

        return new ModelAndView(parametros,"mediciones.hbs");
    }

    public Response cargar(Request request, Response response) throws IOException{
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        organizacion.setCargarExcel();
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        //Get the uploaded file
        Part uploadedFile = null;
        try {
            uploadedFile = request.raw().getPart("myFile");
            System.out.println("Cargo: " + uploadedFile.getName());
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        Part finalUploadedFile = uploadedFile;
        //Se utilizar hilos
        new Thread(new Runnable(){
                @Override
                public void run() {
                    // Time consuming task.
                    try {
                        finalUploadedFile.write("/Excel.xlsx");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        organizacion.cargarExcel("C:\\Users\\Usuario\\AppData\\Local\\Temp\\temp\\Excel.xlsx");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    repositorioDeOrganizaciones.actualizar(organizacion);
                }
            }).start();
        EntityManagerHelper.closeEntityManager();
        response.redirect("/mediciones");
        return response;

    }

    public Response cargarDos(Request request, Response response) throws IOException {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));
        organizacion.setCargarExcel();
        //C:\Users\Usuario\AppData\Local\Temp\temp\Excel.xlsx
        String path = request.queryParams("path");
        System.out.println("El path es:" + path);

        //Se utilizar hilos
        new Thread(new Runnable(){
            @Override
            public void run() {
                // Time consuming task.
                try {
                    organizacion.cargarExcel(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                repositorioDeOrganizaciones.actualizar(organizacion);
            }
        }).start();
        EntityManagerHelper.closeEntityManager();
        response.redirect("/mediciones");
        return response;

    }

    public Response limpiar(Request request, Response response) throws HeuristicRollbackException, SystemException, HeuristicMixedException, RollbackException, NotSupportedException {
        Organizacion organizacion = repositorioDeOrganizaciones.buscar(request.session().attribute("id"));

        organizacion.limpiarMediciones();
        repositorioDeMediciones.eliminarMediciones(organizacion.getId_organizacion());
        repositorioDeOrganizaciones.actualizar(organizacion);
        EntityManagerHelper.closeEntityManager();
        response.redirect("/mediciones");
        return response;
    }
}
