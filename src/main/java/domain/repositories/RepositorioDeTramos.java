package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.trayecto.Tramo;

import java.util.List;

public class RepositorioDeTramos {

    public Tramo buscar(Integer id){
        Tramo tramo = (Tramo) EntityManagerHelper.createQuery("from Tramo where id_tramo = "+id).getSingleResult();
        return tramo;
    }

    public List<Tramo> buscarTramosDeOrganizacion(Integer id) {
        List<Tramo> tramos = (List<Tramo>) EntityManagerHelper.createQuery("from Tramo where organizacion = "+id).getResultList();
        return tramos;
    }

    public void agregar(Tramo tramo){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(tramo);
        EntityManagerHelper.commit();
    }

    public void eliminar(Tramo tramo) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(tramo);
        EntityManagerHelper.commit();
    }

    public void modificar(Tramo tramo) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(tramo);
        EntityManagerHelper.commit();
    }
}
