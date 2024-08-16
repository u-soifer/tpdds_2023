package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.services.geodds.entities.Provincia;

import java.util.List;

public class RepositorioDeProvincias {

    public Provincia buscar(Integer id){
        Provincia provincia = (Provincia) EntityManagerHelper.getEntityManager().createQuery("from Provincia where id = "+id).getSingleResult();
        return provincia;
    }

    public List<Provincia> provinciasDelPais(Integer idPais){
        List<Provincia> provincias = EntityManagerHelper.getEntityManager().createQuery("from Provincia where pais = "+idPais).getResultList();
        return provincias;
    }

    public List<Provincia> buscarTodo() {
        List<Provincia> provincias = (List<Provincia>) EntityManagerHelper.createQuery("from Provincia").getResultList();
        return provincias;
    }
}
