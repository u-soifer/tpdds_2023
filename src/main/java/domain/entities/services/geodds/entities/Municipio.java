package domain.entities.services.geodds.entities;

import javax.persistence.*;

@Entity
@Table(name = "municipio")
public class Municipio {

        @Id
        public int id;

        @Column
        public String nombre;

        @ManyToOne
        @JoinColumn(name = "id_provincia", referencedColumnName = "id")
        public Provincia provincia;

        public String getNombre() { return nombre; }

        public Municipio(int id, String nombre, Provincia provincia) {
                this.id = id;
                this.nombre = nombre;
                this.provincia = provincia;
        }

        public Municipio(int id, String nombre) {
                this.id = id;
                this.nombre = nombre;
        }

        public Municipio() {

        }

        public Municipio.MunicipioDTO convertirADTO() { return new Municipio.MunicipioDTO(this); }

        public class MunicipioDTO{
                public String id;
                public String nombreMunicipio;
                public String provincia;

                public MunicipioDTO(Municipio municipio) {
                        this.id = Integer.toString(municipio.id);
                        this.nombreMunicipio = municipio.getNombre();
                        this.provincia = String.valueOf(municipio.provincia);
                }

                public String getIdMunicipio() { return id; }

                public String getNombreMunicipio() { return nombreMunicipio; }

                public String getProvincia() { return provincia; }
        }
}
