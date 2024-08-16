package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Organizacion;

public class RepositorioDeAreas {

    public Area buscar(Integer id){
        Area area = (Area) EntityManagerHelper.createQuery("from Area where id = "+id).getSingleResult();
        return area;
    }

    public void actualizar(Area area){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(area);
        EntityManagerHelper.commit();
    }
}
