package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.EstadoTrabajo;
import domain.entities.organizacion.Organizacion;
import domain.entities.organizacion.Trabajo;

import java.util.List;

public class RepositorioDeTrabajos {

    public void agregar(Trabajo trabajo){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(trabajo);
        EntityManagerHelper.commit();
    }

    public List<Trabajo> obtenerTrabajosDe(Integer id_empleado){
        List<Trabajo> trabajos = EntityManagerHelper.createQuery("from Trabajo where empleado = "+id_empleado+" and area is not null and estadoTrabajo ="+ EstadoTrabajo.ACTIVO.ordinal()).getResultList();
        return trabajos;
    }

    public List<Trabajo> obtenerSolicitudesDe(Integer id_empleado){
        List<Trabajo> trabajos = EntityManagerHelper.createQuery("from Trabajo where empleado = "+id_empleado+" and area is null and estadoTrabajo ="+ EstadoTrabajo.PENDIENTE.ordinal()).getResultList();
        return trabajos;
    }

    public List<Trabajo> solicitudesOrganizacion(Integer id_organizacion){
        List<Trabajo> trabajos = EntityManagerHelper.createQuery("from Trabajo where organizacion = "+id_organizacion+" and estadoTrabajo ="+ EstadoTrabajo.PENDIENTE.ordinal()).getResultList();
        return trabajos;
    }

    public Trabajo buscar(Integer id){
        Trabajo trabajo = EntityManagerHelper.getEntityManager().find(Trabajo.class, id);
        return trabajo;
    }

    public void actualizar(Trabajo trabajo){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(trabajo);
        EntityManagerHelper.commit();
    }
}
