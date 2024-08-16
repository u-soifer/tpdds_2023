package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.services.geodds.entities.Municipio;

import java.util.List;

public class RepositorioDeMunicipios {

    public Municipio buscar(Integer id){
        Municipio municipio = (Municipio) EntityManagerHelper.createQuery("from Municipio where id = "+id).getSingleResult();
        return municipio;
    }

    public List<Municipio> municipiosDeProvincia(Integer idProvincia){
        List<Municipio> municipios = (List<Municipio>) EntityManagerHelper.createQuery("from Municipio where provincia = "+idProvincia).getResultList();
        return municipios;
    }

    public List<Municipio> buscarTodo() {
        List<Municipio> municipios = (List<Municipio>) EntityManagerHelper.createQuery("from Municipio").getResultList();
        return municipios;
    }
}
