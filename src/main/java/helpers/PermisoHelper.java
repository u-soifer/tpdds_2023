package helpers;

import domain.entities.usuario.TipoUsuario;
import spark.Request;

import java.util.Arrays;

public class PermisoHelper {

    public static Boolean usuarioTienePermisos(Request request, TipoUsuario ... tipoUsuario) {
        return Arrays.stream(tipoUsuario).anyMatch(a -> a.toString().equals(request.session().attribute("tipo_usuario").toString()));

        /*
        return request
                .session()
                .attribute("tipo_usuario")
                .toString()
                .equals(tipoUsuario.toString());
         */
    }

}
