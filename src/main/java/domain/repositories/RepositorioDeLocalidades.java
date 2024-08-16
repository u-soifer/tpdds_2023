package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.services.geodds.entities.Localidad;

import java.util.List;

public class RepositorioDeLocalidades {

    public Localidad buscar(Integer id){
        Localidad localidad = (Localidad) EntityManagerHelper.createQuery("from Localidad where id = "+id).getSingleResult();
        return localidad;
    }

    public List<Localidad> localidadesDeMunicipio(Integer idLocalidad){
        List<Localidad> localidades = (List<Localidad>) EntityManagerHelper.createQuery("from Localidad where municipio = "+idLocalidad).getResultList();
        return localidades;
    }
}
