package domain.repositories;

import db.EntityManagerHelper;
import domain.entities.usuario.Usuario;

import java.util.List;

public class RepositorioDeUsuarios {

    public List<Usuario> buscarTodos(){
        return (List<Usuario>) EntityManagerHelper.createQuery("from Usuario").getResultList();
    }

    public Boolean existe(String nombreDeUsuario, String contrasenia){
        Integer cantUsuarios = (Integer) EntityManagerHelper.createQuery("select 1 from Usuario where usuario = '" + nombreDeUsuario + "' and contrasenia = '" + contrasenia + "'").getSingleResult();
        return cantUsuarios > 0;
    }

    public Usuario buscar(String nombreDeUsuario, String contrasenia){
        Usuario usuario = (Usuario) EntityManagerHelper.createQuery("from Usuario where usuario = '" + nombreDeUsuario + "' and contrasenia = '" + contrasenia + "'").getSingleResult();
        return usuario;
    }

    public Usuario buscar(Integer id){
        return  (Usuario) EntityManagerHelper.createQuery("from Usuario where id_usuario = " + id).getSingleResult();
    }

    public void guardar(Usuario usuario){
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.persist(usuario);
        EntityManagerHelper.commit();
    }

    public void modificar(Usuario usuario) {
        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.getEntityManager().merge(usuario);
        EntityManagerHelper.commit();
    }
}