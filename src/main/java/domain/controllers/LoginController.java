package domain.controllers;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import config.Config;
import db.EntityManagerHelper;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.TipoDocumento;
import domain.entities.usuario.TipoUsuario;
import domain.entities.usuario.Usuario;
import domain.repositories.RepositorioDeAgentesSectoriales;
import domain.repositories.RepositorioDeEmpleados;
import domain.repositories.RepositorioDeOrganizaciones;
import domain.repositories.RepositorioDeUsuarios;
import org.checkerframework.checker.units.qual.A;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginController {

    RepositorioDeUsuarios repoUsuarios;
    RepositorioDeEmpleados repoEmpleados;
    RepositorioDeOrganizaciones repoOrganizaciones;
    RepositorioDeAgentesSectoriales repoAgentesSectoriales;

    public LoginController() {
        repoUsuarios = new RepositorioDeUsuarios();
        repoEmpleados = new RepositorioDeEmpleados();
        repoOrganizaciones = new RepositorioDeOrganizaciones();
        repoAgentesSectoriales = new RepositorioDeAgentesSectoriales();
    }

    public ModelAndView inicio(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        return new ModelAndView(parametros, "login.hbs");
    }

    public String login(Request request, Response response) {

        Map<String, String> resultado = new HashMap<>();

        try {

            String nombreDeUsuario = request.queryParams("usuario");
            String contrasenia = request.queryParams("contrasenia");
            String hashContrasenia = Hashing.sha256().hashString(contrasenia, StandardCharsets.UTF_8).toString();

            if (repoUsuarios.existe(nombreDeUsuario, hashContrasenia)) {
                Usuario usuario = repoUsuarios.buscar(nombreDeUsuario, hashContrasenia);

                request.session(true);
                request.session().attribute("tipo_usuario", usuario.getTipoUsuario().toString());
                request.session().attribute("idUsuario", usuario.getId());

                if (usuario.estaActivo()){
                    switch (usuario.getTipoUsuario()) {
                        case EMPLEADO:
                            Empleado empleado = repoEmpleados.obtenerEmpleadoDeUsuario(usuario.getId());
                            request.session().attribute("id", empleado.getId_empleado());
                            resultado.put("redirect", "/trabajos");
                            break;
                        case ORGANIZACION:
                            Organizacion organizacion = repoOrganizaciones.obtenerOrganizacionDeUsuario(usuario.getId());
                            request.session().attribute("id", organizacion.getId_organizacion());
                            resultado.put("redirect", "/organizacion");
                            break;
                        case AGENTE_SECTORIAL:
                            AgenteSectorial agenteSectorial = repoAgentesSectoriales.obtenerAgenteDeUsuario(usuario.getId());
                            request.session().attribute("id", agenteSectorial.getId_agenteSectorial());
                            resultado.put("redirect", "/mi-sector-territorial");
                            break;
                        case ADMIN:
                            request.session().attribute("id", usuario.getId());
                            resultado.put("redirect", "/agentes-sectoriales");
                            break;
                    }

                    resultado.put("respuesta", "OK");
                }
                else {
                    Double codigo = Math.random() * (999999 - 100000 + 1) + 100000;
                    String mail = "Su código de verificación para mi impacto ambiental es: "+ String.valueOf(codigo.intValue());
                    Config.getNotificador().notificarPorMail(usuario.getMail(), "Codigo de Verificacion", mail);
                    request.session().attribute("codigo", String.valueOf(codigo.intValue()));

                    resultado.put("respuesta", "MAIL");
                    resultado.put("msjError", "Usted debe validar su mail para iniciar sesion");
                }
            }
            else {
                resultado.put("respuesta", "ERROR");
                resultado.put("msjError", "Usuario y/o contraseña invalido");
            }
        } catch (Exception e) {
            resultado.put("respuesta", "ERROR");
            resultado.put("msjError", "Usuario y/o contraseña invalido");
        } finally {
            Gson gson = new Gson();
            String jsonRespuesta = gson.toJson(resultado);
            response.type("application/json");
            return jsonRespuesta;
        }
    }

    public Response logout(Request request, Response response){
        request.session().invalidate();
        response.redirect("/login");
        return response;
    }


    public String signup(Request request, Response response) throws IOException {

        Map<String, String> resultado = new HashMap<>();

        List<Usuario> usuarios = repoUsuarios.buscarTodos();

        if(usuarios.stream().filter(a -> a.getUsuario().equals(request.queryParams("usuario"))).count() > 0){
            resultado.put("respuesta", "ERROR");
            resultado.put("msjError", "El nombre de usuario ya existe");
        }
        else if(!Config.getValidador().esContraseniaValida(request.queryParams("contrasenia"))){
            resultado.put("respuesta", "ERROR");
            resultado.put("msjError", Config.getValidador().esContraseniaValidaConDetalle(request.queryParams("contrasenia")));
        }
        else {
            Double codigo = Math.random() * (999999 - 100000 + 1) + 100000;
            String mail = "Su código de verificación para mi impacto ambiental es: "+ String.valueOf(codigo.intValue());
            Config.getNotificador().notificarPorMail(request.queryParams("mail"), "Codigo de Verificacion", mail);
            request.session().attribute("codigo", String.valueOf(codigo.intValue()));

            resultado.put("respuesta", "OK");

            String hashContrasenia = Hashing.sha256().hashString(request.queryParams("contrasenia"), StandardCharsets.UTF_8).toString();
            if (request.queryParams("tipoUsuario").equals("Empleado")) {
                Usuario usuario = new Usuario(TipoUsuario.EMPLEADO,
                        request.queryParams("usuario"),
                        hashContrasenia,
                        request.queryParams("mail"));
                Empleado empleado = new Empleado(request.queryParams("nombre"),
                                                 request.queryParams("apellido"),
                                                 TipoDocumento.DNI,
                                                 new Integer(request.queryParams("numDoc")));
                empleado.setUsuario(usuario);
                repoEmpleados.guardar(empleado);
                request.session().attribute("idUsuario", usuario.getId());
                request.session().attribute("id", empleado.getId_empleado());
                request.session().attribute("tipo_usuario", usuario.getTipoUsuario().toString());
            } else if (request.queryParams("tipoUsuario").equals("Agente")) {
                Usuario usuario = new Usuario(TipoUsuario.AGENTE_SECTORIAL,
                                              request.queryParams("usuario"),
                                              hashContrasenia,
                                              request.queryParams("mail"));
                AgenteSectorial agenteSectorial = new AgenteSectorial(request.queryParams("nombre"));
                agenteSectorial.setUsuario(usuario);
                repoAgentesSectoriales.guardar(agenteSectorial);
                request.session().attribute("idUsuario", usuario.getId());
                request.session().attribute("id", agenteSectorial.getId_agenteSectorial());
                request.session().attribute("tipo_usuario", usuario.getTipoUsuario().toString());
            }
        }

        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(resultado);
        response.type("application/json");
        return jsonRespuesta;
    }

    public Object validar(Request request, Response response) {

        Map<String, String> resultado = new HashMap<>();

        if (request.queryParams("codigo").equals(request.session().attribute("codigo").toString())){
            Usuario usuario = repoUsuarios.buscar(request.session().attribute("idUsuario"));
            usuario.validar();
            repoUsuarios.modificar(usuario);
            resultado.put("respuesta", "OK");
            if (request.session().attribute("tipo_usuario").equals("EMPLEADO")){
                resultado.put("redirect", "/trabajos");
            }
            else if (request.session().attribute("tipo_usuario").equals("AGENTE_SECTORIAL")) {
                resultado.put("redirect", "/mi-sector-territorial");
            }
            else if (request.session().attribute("tipo_usuario").equals("ADMIN")) {
                resultado.put("redirect", "/agentes-sectoriales");
            }
            else if (request.session().attribute("tipo_usuario").equals("ORGANIZACION")) {
                resultado.put("redirect", "/organizacion");
            }
        }
        else{
            resultado.put("respuesta", "ERROR");
            resultado.put("msjError", "El codigo es invalido");
        }

        Gson gson = new Gson();
        String jsonRespuesta = gson.toJson(resultado);
        response.type("application/json");
        return jsonRespuesta;
    }
}
