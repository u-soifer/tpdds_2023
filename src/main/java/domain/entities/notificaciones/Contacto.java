package domain.entities.notificaciones;

import domain.entities.organizacion.Organizacion;

import javax.persistence.*;

import java.io.IOException;

@Entity
@Table(name = "contacto")
public class Contacto {
    @Id
    @GeneratedValue
    private int id_contacto;

    @Column
    private String nombre;

    @Column
    private String mail;

    @Column
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_organizacion", referencedColumnName = "id_organizacion")
    private Organizacion organizacion;

    @Transient
    private Notificador notificador;

    public Contacto(String nombre, String mail, String telefono, Notificador notificador) {
        this.nombre = nombre;
        this.mail = mail;
        this.telefono = telefono;
        this.notificador = notificador;
    }

    public Contacto(String nombre, String mail, String telefono) {
        this.nombre = nombre;
        this.mail = mail;
        this.telefono = telefono;
        this.notificador = notificador;
    }

    public Contacto() {

    }

    public int getId_contacto() {
        return id_contacto;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void serNotificado(Via via, String titulo, String mensaje) throws IOException {
        switch (via){
            case WHATSAPP: this.serNotificadoWpp("*"+titulo+"*\n\n"+mensaje);
            case MAIL: this.serNotificadoMail(titulo, mensaje);
        }
    }

    public void serNotificadoMail(String asunto, String cuerpo) throws IOException {
        this.notificador.notificarPorMail(this.mail, asunto, cuerpo);
    }

    public void serNotificadoWpp(String mensaje) throws IOException {
        notificador.notificarPorWpp(this.telefono, mensaje);
    }

}
