package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.mediosDeTransporte.TipoTransportePublico;
import domain.entities.mediosDeTransporte.TransportePublico;

import java.util.List;

public class RepositorioDeTransportePublico {

    public List<TransportePublico> transportesDelTipo(Integer tipo){
        List<TransportePublico> transportes = (List<TransportePublico>) EntityManagerHelper.createQuery("from TransportePublico where tipo = "+tipo).getResultList();
        return transportes;
    }

    public TransportePublico buscar(Integer id){
        TransportePublico transporte = (TransportePublico) EntityManagerHelper.createQuery("from TransportePublico where id_medioDeTransporte = "+id).getSingleResult();
        return transporte;
    }

    public List<TransportePublico> buscarTodo(){
        List<TransportePublico> transportesPublicos = (List<TransportePublico>) EntityManagerHelper.createQuery("from TransportePublico").getResultList();
        return transportesPublicos;
    }

    public void agregar(TransportePublico tp) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(tp);
        EntityManagerHelper.commit();
    }
}
