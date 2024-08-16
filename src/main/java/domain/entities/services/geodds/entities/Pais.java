package domain.entities.services.geodds.entities;

import javax.persistence.*;

@Entity
@Table(name = "pais")
public class Pais {

        @Id
        public int id;

        @Column
        public String nombre;

        public Pais(int id, String nombre) {
                this.id = id;
                this.nombre = nombre;
        }

        public int getId() {
                return id;
        }

        public String getNombre() {
                return nombre;
        }

        public Pais() {

        }
}
