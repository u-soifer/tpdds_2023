package server;

import config.Config;
import domain.controllers.*;
import domain.entities.usuario.TipoUsuario;
import helpers.PermisoHelper;
import middlewares.AuthMiddleware;
import spark.Spark;
import spark.template.handlebars.HandlebarsTemplateEngine;
import spark.utils.BooleanHelper;
import spark.utils.HandlebarsTemplateEngineBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipOutputStream;

public class Router {

    private static HandlebarsTemplateEngine engine;

    private static void initEngine() {
        Router.engine = HandlebarsTemplateEngineBuilder
                .create()
                .withDefaultHelpers()
                .withHelper("isTrue", BooleanHelper.isTrue)
                .build();

    }

    public static void init() throws IOException {
        Router.initEngine();
        Spark.staticFileLocation("/public");
        Router.configure();
    }

    private static void configure() throws IOException {
        Config config = new Config();

        //Controllers públicos
        LoginController loginController = new LoginController();
        RecomendacionesController recomendacionesController = new RecomendacionesController();

        //Controllers de empleado
        TrabajoController trabajoController = new TrabajoController();
        UbicacionController ubicacionController = new UbicacionController();
        TrayectosController trayectosController = new TrayectosController();
        CalculadoraHC calculadoraHC = new CalculadoraHC();

        //Controllers de organización
        MiOrganizacionController miOrganizacionController = new MiOrganizacionController();
        MedicionesController medicionesController = new MedicionesController();
        ContactosController contactosController = new ContactosController();
        VinculacionController vinculacionController = new VinculacionController();

        //Controllers de agente sectorial
        ReportesController reportesController = new ReportesController();
        AgentesSectorialesController agentesController = new AgentesSectorialesController();

        //Controllers del administrador
        OrganizacionesController organizacionesController = new OrganizacionesController();
        ConsumosController consumosController = new ConsumosController();
        TransportePublicoController transportePublicoController = new TransportePublicoController();

        //Rutas publicas
        Spark.get("/recomendaciones", recomendacionesController::recomendaciones, Router.engine);

        Spark.path("/login", () -> {
            Spark.get("", loginController::inicio, Router.engine);
            Spark.post("", loginController::login);
            Spark.get("/logout", loginController::logout);
            Spark.post("/signup", loginController::signup);
            Spark.post("/validar", loginController::validar);
        });

        Spark.get("/excel", ((request, response) -> {
            URI filePath = URI.create("/js/EjemploExcel.xls");
            byte[] bytes = Files.readAllBytes(Paths.get(filePath));
            HttpServletResponse raw = response.raw();

            raw.getOutputStream().write(bytes);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();

            response.type("application/vnd.ms-excel");
            return response.raw();
        }));

        //Rutas de empleado
        Spark.path("/trabajos", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.EMPLEADO)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", trabajoController::mostrar, engine);
            Spark.post("", trabajoController::guardar);
        });

        Spark.path("/ubicaciones", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.EMPLEADO)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("/provincias", ubicacionController::provincias);
            Spark.get("/municipios", ubicacionController::municipios);
            Spark.get("/localidades", ubicacionController::localidades);

            Spark.get("", ubicacionController::mostrar, Router.engine);
            Spark.post("/crear", ubicacionController::guardar);
            Spark.post("/modificar/:id_ubicacion", ubicacionController::modificar);
            Spark.post("/eliminar/:id_ubicacion", ubicacionController::eliminar);
        });

        Spark.path("/trayectos", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.EMPLEADO)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("/lineas", trayectosController::lineas);
            Spark.get("/paradas", trayectosController::paradas);

            Spark.get("", trayectosController::mostrar, Router.engine);
            Spark.post("/crear", trayectosController::guardar);
            Spark.post("/modificar/:id_trayecto", trayectosController::modificar);
            Spark.post("/eliminar/:id_trayecto", trayectosController::eliminar);

            Spark.path("/tramos", () -> {
                Spark.get("", trayectosController::tramosDeTrayecto);
                Spark.get("/compartidos", trayectosController::tramosCompartidos);
                Spark.get("/detalle", trayectosController::detalleTramo);

                Spark.post("/compartir", trayectosController::compartirTramo);
                Spark.post("/crear", trayectosController::guardarTramo);
                Spark.post("/modificar", trayectosController::modificarTramo);
                Spark.post("/eliminar", trayectosController::eliminarTramo);
            });
        });

        Spark.path("/calculadora-hc", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.EMPLEADO, TipoUsuario.ORGANIZACION)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", calculadoraHC::mostrar, Router.engine);
        });

        //Rutas de organizacion
        Spark.path("/organizacion", ()-> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ORGANIZACION)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("/provincias", miOrganizacionController::provincias);
            Spark.get("/municipios", miOrganizacionController::municipios);
            Spark.get("/localidades", miOrganizacionController::localidades);

            Spark.post("/modificarUbicacion", miOrganizacionController::modificarUbi);

            Spark.get("", miOrganizacionController::mostrar, Router.engine);
            Spark.post("/crear", miOrganizacionController::crear);
            Spark.post("/modificar/:id_area", miOrganizacionController::modificarArea);
            Spark.post("/eliminar/:id_area", miOrganizacionController::eliminar);

            Spark.path("/empleados", () -> {
                Spark.get("", miOrganizacionController::empleadosDeArea);
                Spark.get("/detalle", miOrganizacionController::detalleEmpleado);
                Spark.post("/eliminar", miOrganizacionController::eliminarEmpleado);
            });

        });

        Spark.path("/mediciones", ()-> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ORGANIZACION)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", medicionesController::mostrar, Router.engine);
            Spark.post("/cargar", medicionesController::cargar);
            Spark.post("/cargar2", medicionesController::cargarDos);
            Spark.post("/limpiar", medicionesController::limpiar);

        });

        Spark.path("/vinculacion", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ORGANIZACION)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", vinculacionController::mostrar, Router.engine);
            Spark.post("/aceptar/:id_trabajo", vinculacionController::aceptar);
            Spark.post("/rechazar/:id_trabajo", vinculacionController::rechazar);
        });

        Spark.path("/contactos", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ORGANIZACION)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", contactosController::mostrar, Router.engine);
            Spark.post("/crear", contactosController::guardar);
            Spark.post("/modificar/:id_contacto", contactosController::modificar);
            Spark.post("/eliminar/:id_contacto", contactosController::eliminar);

        });

        //Rutas de agente sectorial
        Spark.path("/reportes", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("/", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.AGENTE_SECTORIAL)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", reportesController::mostrar, Router.engine);
            Spark.post("", reportesController::obtenerReporte);
        });

        Spark.path("/mi-sector-territorial", ()-> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("/", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.AGENTE_SECTORIAL)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", agentesController::mostrar, Router.engine);
            Spark.post("", agentesController::solicitarVinculacion);
        });


        //Rutas de administrador
        Spark.path("/organizaciones", () -> {

            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if (!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ADMIN)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", organizacionesController::mostrar, Router.engine);
            //Spark.get("/:id_organizacion", organizacionesController::mostrar, Router.engine);
            Spark.post("/modificar/:id_organizacion", organizacionesController::modificar);
            Spark.post("/eliminar/:id_organizacion", organizacionesController::eliminar);
            Spark.post("/crear", organizacionesController::guardar);

        });

        Spark.path("/agentes-sectoriales", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("/", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ADMIN)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", agentesController::mostrarTodos, Router.engine);
            Spark.post("/crear", agentesController::crear);
            Spark.post("/rechazar-solicitud/agente/:id_agenteSectorial", agentesController::rechazarSolicitud);
            Spark.post("/aceptar-solicitud/agente/:id_agenteSectorial", agentesController::aceptarSolicitud);
            Spark.post("/modificar/:id_sectorTerritorial", agentesController::modificarSectorTerritorial);
            Spark.post("/eliminar/sector/:id_sectorTerritorial", agentesController::eliminarSectorTerritorial);
        });

        Spark.path("/consumos", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("/", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ADMIN)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", consumosController::mostrar, Router.engine);
            Spark.post("", consumosController::guardar);
        });

        Spark.path("/medios-de-transporte", () -> {
            Spark.before("/*", AuthMiddleware::verificarSesion);
            Spark.before("/", AuthMiddleware::verificarSesion);

            Spark.before("/*", ((request, response) -> {
                if(!PermisoHelper.usuarioTienePermisos(request, TipoUsuario.ADMIN)){
                    response.redirect("/login/logout");
                    Spark.halt();
                }
            }));

            Spark.get("", transportePublicoController::mostrar, Router.engine);
            Spark.post("", transportePublicoController::obtenerParadas);
            Spark.post("/editar", transportePublicoController::editar);
            Spark.post("/crear", transportePublicoController::guardar);
        });
    }
}


