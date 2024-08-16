package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.actividades.Consumo;
import domain.entities.organizacion.Organizacion;
import domain.entities.ubicacion.Parada;

import java.util.List;

public class RepositorioDeParadas {

    public Parada buscar (Integer id){
        Parada parada = (Parada) EntityManagerHelper.createQuery("from Parada where id_ubicacion = "+id).getSingleResult();
        return parada;
    }

    public List<Parada> buscarPorLinea(String linea){
        List<Parada> paradas = (List<Parada>) EntityManagerHelper.createQuery("from Parada p join Ubicacion u on u.id_ubicacion = p.id_ubicacion\n" +
                "join medio_de_transporte_parada m on m.paradas_id_ubicacion = p.id_ubicacion\n" +
                "join MedioDeTransporte t on t.id_medioDeTransporte = m.TransportePublico_id_medioDeTransporte\n" +
                "where linea = "+linea).getResultList();
        return paradas;
    }

    public void actualizar(Parada parada){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(parada);
        EntityManagerHelper.commit();
    }
}
