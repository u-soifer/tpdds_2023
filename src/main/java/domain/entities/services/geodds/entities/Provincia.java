package domain.entities.services.geodds.entities;

import javax.persistence.*;

@Entity
@Table(name = "provincia")
public class Provincia {

        @Id
        public int id;

        @Column
        public String nombre;

        @ManyToOne
        @JoinColumn(name = "id_pais", referencedColumnName = "id")
        public Pais pais;

        public Provincia(int id, String nombre, Pais pais) {
                this.id = id;
                this.nombre = nombre;
                this.pais = pais;
        }

        public Provincia() {

        }

        public String getNombre() { return this.nombre; }

        public Provincia.ProvinciaDTO convertirADTO() { return new Provincia.ProvinciaDTO(this); }

        public class ProvinciaDTO{
                public String idProvincia;
                public String nombreProvincia;
                public String pais;

                public ProvinciaDTO(Provincia provincia) {
                        this.idProvincia = Integer.toString(provincia.id);
                        this.nombreProvincia = provincia.getNombre();
                        this.pais = String.valueOf(provincia.pais);
                }

                public String getIdProvincia() { return idProvincia; }

                public String getNombreProvincia() { return nombreProvincia; }

                public String getPais() { return pais; }
        }
}
