package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.notificaciones.Contacto;


public class RepositorioDeContactos {

    public Contacto buscar(Integer id_contacto){
        Contacto contacto = (Contacto) EntityManagerHelper.createQuery("from Contacto where id_contacto = "+id_contacto).getSingleResult();
        return contacto;
    }

    public void agregar(Contacto contacto){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(contacto);
        EntityManagerHelper.commit();
    }

    public void modificar(Contacto contacto){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(contacto);
        EntityManagerHelper.commit();
    }

    public void eliminar(Contacto contacto){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(contacto);
        EntityManagerHelper.commit();
    }
}
