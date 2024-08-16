package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.services.geodds.entities.Pais;

import java.util.List;

public class RepositorioDePaises {

    public Pais buscar(Integer id){
        Pais pais = (Pais) EntityManagerHelper.createQuery("from Pais where id = "+id).getSingleResult();
        return pais;
    }

    public List<Pais> buscarTodo(){
        List<Pais> paises = (List<Pais>) EntityManagerHelper.createQuery("from Pais").getResultList();
        return paises;
    }
}
