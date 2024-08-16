package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.trayecto.Trayecto;

import java.util.List;

public class RepositorioDeTrayectos {

    public Trayecto buscar(Integer id){
        Trayecto trayecto = (Trayecto) EntityManagerHelper.createQuery("from Trayecto where id_trayecto = "+id).getSingleResult();
        return trayecto;
    }

    public List<Trayecto> trayectosDeEmpleado(Integer id_empleado){
        List<Trayecto> trayectos = (List<Trayecto>) EntityManagerHelper.createQuery("from Trayecto where empleado = "+id_empleado).getResultList();
        return trayectos;
    }

    public void agregar(Trayecto trayecto){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(trayecto);
        EntityManagerHelper.commit();
    }

    public void modificar(Trayecto trayecto){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(trayecto);
        EntityManagerHelper.commit();
    }

    public void eliminar(Trayecto trayecto) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(trayecto);
        EntityManagerHelper.commit();
    }
}
