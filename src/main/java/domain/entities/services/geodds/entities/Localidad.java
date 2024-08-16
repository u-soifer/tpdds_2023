package domain.entities.services.geodds.entities;

import javax.persistence.*;

@Entity
@Table(name = "localidad")
public class Localidad {

    @Id
    public int id;

    @Column
    public String nombre;

    @Column
    public int codPostal;

    @ManyToOne
    @JoinColumn(name = "id_municipio", referencedColumnName = "id")
    public Municipio municipio;

    public Localidad(int id, String nombre, int codPostal, Municipio municipio) {
        this.id = id;
        this.nombre = nombre;
        this.codPostal = codPostal;
        this.municipio = municipio;
    }

    public Localidad() {

    }
}
