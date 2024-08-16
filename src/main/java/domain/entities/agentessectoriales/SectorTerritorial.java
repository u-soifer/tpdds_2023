package domain.entities.agentessectoriales;

import domain.entities.actividades.Medicion;
import domain.entities.actividades.Periodicidad;
import domain.entities.organizacion.Area;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "sector_territorial")
public class SectorTerritorial {
    @Id
    @GeneratedValue
    private int id_sectorTerritorial;

    @ManyToOne
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    private Provincia provincia;

    @ManyToOne
    @JoinColumn(name = "id_municipio", referencedColumnName = "id")
    private Municipio municipio;

    public SectorTerritorial(Municipio municipio) throws IOException {
        this.provincia = null;
        this.municipio = municipio;
    }

    public SectorTerritorial(Provincia provincia) {
        this.provincia = provincia;
        this.municipio = null;
    }

    public SectorTerritorial() {

    }

    public Provincia getProvincia() { return provincia; }

    public Municipio getMunicipio() { return municipio; }

    public int getId_sectorTerritorial() {
        return id_sectorTerritorial;
    }

    public List<Organizacion> getOrganizaciones(List<Organizacion> organizaciones){
        organizaciones = organizaciones.stream().filter(o -> o.getLugar() != null).collect(Collectors.toList());
        if (this.municipio == null) {
            return organizaciones.stream().filter(a -> this.provincia == a.getLugar().getProvincia()).collect(Collectors.toList());
        }
        else {
            return organizaciones.stream().filter(a -> this.provincia == a.getLugar().getProvincia() && this.municipio == a.getLugar().getMunicipio()).collect(Collectors.toList());
        }
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Double ObtenerHCSector(List<Organizacion> organizaciones){
        return this.getOrganizaciones(organizaciones).stream().mapToDouble(a -> a.calcularHC()).sum();
    }

    public Double ObtenerHCSector(Integer anio, List<Organizacion> organizaciones){
        return this.getOrganizaciones(organizaciones).stream().mapToDouble(a -> a.calcularHC(anio)).sum();
    }

    public Double ObtenerHCSector(Integer anio, Integer mes, List<Organizacion> organizaciones){
        return this.getOrganizaciones(organizaciones).stream().mapToDouble(a -> a.calcularHC(anio, mes)).sum();
    }

    public SectorTerritorial.SectorTerritorialDTO convertirADTO(){
        return new SectorTerritorial.SectorTerritorialDTO(this);
    }

    //TODO
    public Double getHC() {
        return null;
    }

    public class SectorTerritorialDTO{
        public String id_sectorTerritorial;
        public Provincia provincia;
        public Municipio municipio;

        public SectorTerritorialDTO(SectorTerritorial sectorTerritorial){
            this.id_sectorTerritorial = Integer.toString(sectorTerritorial.getId_sectorTerritorial());
            this.provincia = sectorTerritorial.getProvincia();
            this.municipio = sectorTerritorial.getMunicipio();
        }

        public String getId_sectorTerritorial() {
            return id_sectorTerritorial;
        }

        public Provincia getProvincia() {
            return provincia;
        }

        public Municipio getMunicipio() {
            return municipio;
        }
    }


}
