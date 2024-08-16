package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.EstadoTrabajo;
import domain.entities.organizacion.Trabajo;

import java.util.List;
import java.util.stream.Collectors;

public class RepositorioDeEmpleados{

    public Empleado buscar(Integer id){
        Empleado empleado = (Empleado) EntityManagerHelper.getEntityManager().find(Empleado.class, id);
        return empleado;
    }


    //TODO Borrar luego de arreglar lo de empleados
    public List<Empleado> buscarEmpleados(Integer idArea){
        List<Trabajo> trabajos = EntityManagerHelper.createQuery("from Trabajo where area = "+idArea).getResultList();
        List<Empleado> empleados = trabajos.stream()
                .filter(trabajo -> trabajo.getEstadoTrabajo() == "Activo")
                .map(a->a.getEmpleado()).collect(Collectors.toList());
        return empleados;
    }

    public Empleado obtenerEmpleadoDeUsuario(Integer id){
        Empleado empleado = (Empleado) EntityManagerHelper.createQuery("from Empleado where usuario = "+id).getSingleResult();
        return empleado;
    }

    public void modificar(Empleado empleado){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(empleado);
        EntityManagerHelper.commit();
    }

    public void guardar(Empleado empleado) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.persist(empleado);
        EntityManagerHelper.commit();
    }
}
