package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.actividades.Medicion;
import domain.entities.notificaciones.Contacto;
import domain.entities.organizacion.*;
import domain.entities.trayecto.Trayecto;

import java.io.IOException;
import java.util.List;

public class RepositorioDeOrganizaciones {

    public Organizacion buscar(Integer id){
        Organizacion organizacion = (Organizacion) EntityManagerHelper.createQuery("from Organizacion where id_organizacion = "+id).getSingleResult();
        return organizacion;
    }

    public List<Organizacion> buscarTodo(){
        List<Organizacion> organizaciones = (List<Organizacion>) EntityManagerHelper.createQuery("from Organizacion").getResultList();
        return organizaciones;
    }


    //TODO: deberia agarrar una organizacion y despues las areas pero o a lo sumo hacerlo en un repositorio de areas -> Se sigue utilizando en trayectosController, no lo toco
    public List<Area> obtenerAreas(Integer id_organizacion){
        List<Area> areas = (List<Area>) EntityManagerHelper.createQuery("from Area where id_organizacion = "+id_organizacion).getResultList();
        return areas;
    }

    public void actualizar(Organizacion organizacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(organizacion);
        EntityManagerHelper.commit();
    }

    public Organizacion obtenerOrganizacionDeUsuario(Integer id){
        Organizacion organizacion = (Organizacion) EntityManagerHelper.createQuery("from Organizacion where usuario = "+id).getSingleResult();
        return organizacion;
    }

    public void guardar(Organizacion organizacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(organizacion);
        EntityManagerHelper.commit();
    }

    public void modificar(Organizacion organizacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(organizacion);
        EntityManagerHelper.commit();
    }

    public void eliminar(Organizacion organizacion){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(organizacion);
        EntityManagerHelper.commit();
    }
}
