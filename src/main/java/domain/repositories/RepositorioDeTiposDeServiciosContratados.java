package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.mediosDeTransporte.TipoDeServicioContratado;

import java.util.List;

public class RepositorioDeTiposDeServiciosContratados {

    public TipoDeServicioContratado buscar(Integer id){
        TipoDeServicioContratado tipo = (TipoDeServicioContratado) EntityManagerHelper.getEntityManager().createQuery("from TipoDeServicioContratado where id_tipoDeServicioContratado = "+id).getSingleResult();
        return tipo;
    }

    public List<TipoDeServicioContratado> buscarTodos() {
        List<TipoDeServicioContratado> tipos = (List<TipoDeServicioContratado>) EntityManagerHelper.createQuery("from TipoDeServicioContratado").getResultList();
        return tipos;
    }
}
