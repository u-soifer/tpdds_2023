package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.agentessectoriales.AgenteSectorial;
import domain.entities.agentessectoriales.SectorTerritorial;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;

import java.util.List;

public class RepositorioDeSectoresTerritoriales {

    public SectorTerritorial buscar(Integer id){
        SectorTerritorial sectorTerritorial = (SectorTerritorial) EntityManagerHelper.createQuery("from SectorTerritorial where id_sectorTerritorial = "+id).getSingleResult();
        return sectorTerritorial;
        //return (SectorTerritorial) EntityManagerHelper.getEntityManager().find(SectorTerritorial.class, id);
        //return (SectorTerritorial) EntityManagerHelper.getEntityManager().createQuery("from SectorTerritorial where id_sectorTerritorial =" +id).getSingleResult();
    }

    public List<SectorTerritorial> buscarTodo(){
        List<SectorTerritorial> sectores = (List<SectorTerritorial>) EntityManagerHelper.createQuery("from SectorTerritorial").getResultList();
        return sectores;
    }

    public void modificar(SectorTerritorial sectorTerritorial) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(sectorTerritorial);
        EntityManagerHelper.commit();
    }

    public void eliminar(SectorTerritorial sectorTerritorial){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().remove(sectorTerritorial);
        EntityManagerHelper.commit();
    }

    public void guardar(SectorTerritorial sectorTerritorial){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().persist(sectorTerritorial);
        EntityManagerHelper.commit();
    }
}
