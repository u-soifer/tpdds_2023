package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.ubicacion.Ubicacion;

public class RepositorioDeUbicaciones {

    public Ubicacion buscar(Integer id_ubicacion){
        Ubicacion ubicacion = (Ubicacion) EntityManagerHelper.createQuery("from Ubicacion where id_ubicacion = "+id_ubicacion).getSingleResult();
        return ubicacion;
    }

    public void agregar(Ubicacion ubicacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(ubicacion);
        EntityManagerHelper.commit();
    }

    public void modificar (Ubicacion ubicacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(ubicacion);
        EntityManagerHelper.commit();
    }

    public void eliminar (Ubicacion ubicacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(ubicacion);
        EntityManagerHelper.commit();
    }
}
