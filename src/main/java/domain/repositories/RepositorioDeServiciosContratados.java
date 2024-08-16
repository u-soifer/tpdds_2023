package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.mediosDeTransporte.MedioDeTransporte;
import domain.entities.mediosDeTransporte.ServicioContratado;

import java.util.List;

public class RepositorioDeServiciosContratados {

    public List<ServicioContratado> buscarTodo(){
        List<ServicioContratado> servicios = (List<ServicioContratado>)  EntityManagerHelper.getEntityManager().createQuery("from ServicioContratado").getResultList();
        return servicios;
    }

    public ServicioContratado buscar(Integer id) {
        ServicioContratado servicio = (ServicioContratado) EntityManagerHelper.createQuery("from ServicioContratado where id_medioDeTransporte = "+id).getSingleResult();
        return servicio;
    }
}
