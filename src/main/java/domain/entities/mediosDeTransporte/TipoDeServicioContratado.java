package domain.entities.mediosDeTransporte;

import javax.persistence.*;

@Entity
@Table(name = "tipo_servicio_contratado")
public class TipoDeServicioContratado {

    @Id
    @GeneratedValue
    private Integer id_tipoDeServicioContratado;

    @Column
    private String nombre;

    public Integer getId_tipoDeServicioContratado() {
        return id_tipoDeServicioContratado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDeServicioContratado() {
    }

    public TipoDeServicioContratado(String nombre) {
        this.nombre = nombre;
    }
}
