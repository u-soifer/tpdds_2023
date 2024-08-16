package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.organizacion.EstadoTrabajo;

import java.util.List;

public class RepositorioDeAgentesSectoriales {

    public AgenteSectorial buscar(Integer id){
        AgenteSectorial agenteSectorial = (AgenteSectorial) EntityManagerHelper.createQuery("from AgenteSectorial where id_agenteSectorial = "+id).getSingleResult();
        return agenteSectorial;
    }

    public List<AgenteSectorial> buscarTodo(){
        List<AgenteSectorial> agentes = (List<AgenteSectorial>) EntityManagerHelper.createQuery("from AgenteSectorial ").getResultList();
        return agentes;
    }

    public List<AgenteSectorial> buscarPendientes(){
        List<AgenteSectorial> pendientes = (List<AgenteSectorial>) EntityManagerHelper.createQuery("from AgenteSectorial where estadoSolicitud = 'PENDIENTE'").getResultList();
        return pendientes;
    }

    public void modificar(AgenteSectorial agente) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(agente);
        EntityManagerHelper.commit();
    }

    public void eliminar(AgenteSectorial agenteSectorial){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(agenteSectorial);
        EntityManagerHelper.commit();
    }

    public AgenteSectorial obtenerAgenteDeUsuario(Integer id) {
        AgenteSectorial agenteSectorial = (AgenteSectorial) EntityManagerHelper.createQuery("from AgenteSectorial where usuario = "+id).getSingleResult();
        return agenteSectorial;
    }

    public void guardar(AgenteSectorial agenteSectorial) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.persist(agenteSectorial);
        EntityManagerHelper.commit();
    }

    public void actualizar(AgenteSectorial agenteSectorial){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(agenteSectorial);
        EntityManagerHelper.commit();
    }
}
