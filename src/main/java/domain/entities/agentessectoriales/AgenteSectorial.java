package domain.entities.agentessectoriales;

import domain.entities.organizacion.EstadoTrabajo;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Provincia;
import domain.entities.usuario.Usuario;
import domain.entities.organizacion.Organizacion;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "agente_sectorial")
public class AgenteSectorial {

    @Id
    @GeneratedValue
    private int id_agenteSectorial;

    @ManyToOne
    @JoinColumn(name = "id_sectorTerritorial", referencedColumnName = "id_sectorTerritorial")
    private SectorTerritorial sectorTerritorial;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    @Column
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column
    private EstadoTrabajo estadoSolicitud;

    public AgenteSectorial() {
    }

    public AgenteSectorial(String nombre) {
        this.nombre = nombre;
        this.estadoSolicitud = EstadoTrabajo.SIN_SOLICITUD;
    }

    public AgenteSectorial(SectorTerritorial sectorTerritorial) {
        this.estadoSolicitud = EstadoTrabajo.ACTIVO;
        this.sectorTerritorial = sectorTerritorial;
    }

    public void setUsuario (Usuario usuario){
        this.usuario = usuario;
    }

    public Double calcularHC(Integer anio, List<Organizacion> organizaciones){
        return sectorTerritorial.getOrganizaciones(organizaciones)
                                .stream()
                                .mapToDouble(a -> a.calcularHC(anio))
                                .sum();
    }

    public Double calcularHC(Integer anio, Integer mes, List<Organizacion> organizaciones){
        return sectorTerritorial.getOrganizaciones(organizaciones)
                                .stream()
                                .mapToDouble(a -> a.calcularHC(anio, mes))
                                .sum();
    }

    public Integer getIdSectorTerritorial() {
        return this.sectorTerritorial.getId_sectorTerritorial();
    }

    public SectorTerritorial getSectorTerritorial() { return this.sectorTerritorial; }

    public void setSectorTerritorial(SectorTerritorial sector){
        this.estadoSolicitud = EstadoTrabajo.ACTIVO;
        this.sectorTerritorial = sector;
    }

    public void setSolicitudVinculacion(SectorTerritorial sector){
        this.estadoSolicitud = EstadoTrabajo.PENDIENTE;
        this.sectorTerritorial = sector;
    }


    public EstadoTrabajo getEstadoSolicitud() { return this.estadoSolicitud; }

    public String getNombre() {
        return nombre;
    }

    public int getId_agenteSectorial() {
        return id_agenteSectorial;
    }

    public void setNombre(String nombre){this.nombre = nombre;}

    public void setEstadoSolicitud(EstadoTrabajo estado){
        this.estadoSolicitud = estado;
    }

    public AgenteSectorial.AgenteSectorialDTO convertirADTO(){
        return new AgenteSectorial.AgenteSectorialDTO(this);
    }

    public class AgenteSectorialDTO{
        public String id_agenteSectorial;
        public String nombre;
        public SectorTerritorial sectorTerritorial;
        public String estadoTrabajo;

        public AgenteSectorialDTO(AgenteSectorial agenteSectorial){
            this.id_agenteSectorial = Integer.toString(agenteSectorial.getId_agenteSectorial());
            this.nombre = agenteSectorial.getNombre();
            this.sectorTerritorial = agenteSectorial.getSectorTerritorial();
            if(agenteSectorial.getEstadoSolicitud() != null) {
                this.estadoTrabajo = agenteSectorial.getEstadoSolicitud().toString();
            }
            else this.estadoTrabajo = null;
        }

        public String getId_agenteSectorial() {
            return id_agenteSectorial;
        }

        public String getNombre() {
            return nombre;
        }

        public SectorTerritorial getSectorTerritorial() {
            return sectorTerritorial;
        }

        public String getEstadoTrabajo(){
            return estadoTrabajo;
        }

    }
}
