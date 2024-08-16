package domain.entities.usuario;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue
    private int id_usuario;

    @Enumerated(EnumType.ORDINAL)
    private TipoUsuario tipoUsuario;

    @Column
    private String usuario;

    @Column
    private String contrasenia;

    @Column
    private String mail;

    @Column
    private LocalDate fAlta;

    @Column
    private LocalDate fBaja;

    public Usuario() {

    }

    public Usuario(TipoUsuario tipoUsuario, String usuario, String contrasenia, String mail) {
        this.tipoUsuario = tipoUsuario;
        this.usuario = usuario;
        this.contrasenia = contrasenia;
        this.mail = mail;
    }

    public int getId() {
        return id_usuario;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public String getMail() {
        return mail;
    }

    public void validar() {
        this.fAlta = LocalDate.now();
    }

    public Boolean estaActivo(){
        return this.fAlta != null && this.fBaja == null;
    }
}
