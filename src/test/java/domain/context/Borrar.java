package domain.context;

import com.google.common.hash.Hashing;
import db.EntityManagerHelper;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.usuario.TipoUsuario;
import domain.entities.usuario.Usuario;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Borrar {
    public static void main(String[] args) throws IOException {
        Usuario agente = new Usuario(TipoUsuario.AGENTE_SECTORIAL,
                "agenteJuan",
                Hashing.sha256().hashString("grupo4", StandardCharsets.UTF_8).toString(),
                "grupo4ddsmama@gmail.com"
        );

        AgenteSectorial agenteSectorial = new AgenteSectorial();
        agenteSectorial.setNombre("Juan");
        agenteSectorial.setUsuario(agente);

        EntityManagerHelper.getEntityManager();
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.persist(agente);
        EntityManagerHelper.persist(agenteSectorial);
        EntityManagerHelper.commit();
        EntityManagerHelper.closeEntityManager();

    }
}
