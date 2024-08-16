package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.actividades.Medicion;
import domain.entities.actividades.Periodicidad;
import domain.entities.organizacion.Organizacion;

import javax.transaction.*;
import java.util.List;

public class RepositorioDeMediciones {

    public List<Medicion> obtenerMedicionesDe(Integer id_org) {
        List<Medicion> mediciones = (List<Medicion>) EntityManagerHelper.createQuery("from Medicion where id_organizacion ="+id_org).getResultList();
        return mediciones;
    }

    //TODO: esta mal hacer la query asi
    public List<String> obtenerPeriodosAnuales() {
        List<String> periodos = EntityManagerHelper.createQuery("select m.periodoDeImputacion from Organizacion o\n" +
                "join Medicion m on m.organizacion = o.id_organizacion\n" +
                "where m.periodicidad = " + Periodicidad.ANUAL.ordinal() + "\n" +
                "group by m.periodoDeImputacion order by m.periodoDeImputacion").getResultList();
        return periodos;
    }

    //TODO: esta mal hacerlo asi
    public void eliminarMediciones(Integer id){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.createQuery("DELETE from Medicion where organizacion = "+id).executeUpdate();
        EntityManagerHelper.commit();
    }
}
