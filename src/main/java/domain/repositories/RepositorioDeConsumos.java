package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.actividades.Consumo;

import java.util.List;

public class RepositorioDeConsumos {

    public List<Consumo> buscarTodo(){
        List<Consumo> tiposDeConsumo = (List<Consumo>) EntityManagerHelper.createQuery("from Consumo").getResultList();
        return tiposDeConsumo;
    }

    public void actualizar(Consumo consumo){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(consumo);
        EntityManagerHelper.commit();
    }

    public Consumo buscar(Integer id_consumo) {
        Consumo consumo = (Consumo) EntityManagerHelper.createQuery("from Consumo where id_consumo="+id_consumo).getSingleResult();
        return consumo;
    }
}
