package domain.entities.organizacion;

import domain.entities.trayecto.Tramo;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="trabajo")
public class Trabajo {

    @Id
    @GeneratedValue
    private int id_trabajo;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizacion", referencedColumnName = "id_organizacion")
    private Organizacion organizacion;

    @ManyToOne
    @JoinColumn(name = "id_area", referencedColumnName = "id_area")
    private Area area;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_empleado",referencedColumnName = "id_empleado")
    private Empleado empleado;

    @Enumerated(EnumType.ORDINAL)
    private EstadoTrabajo estadoTrabajo;

    @Column
    private double huellaCarbon;

    public Trabajo(Organizacion organizacion, Empleado empleado, EstadoTrabajo estadoTrabajo) {
        this.organizacion = organizacion;
        this.empleado = empleado;
        this.estadoTrabajo = estadoTrabajo;
    }

    public Trabajo(Organizacion organizacion, Empleado empleado, EstadoTrabajo estadoTrabajo, Area area) {
        this.organizacion = organizacion;
        this.empleado = empleado;
        this.estadoTrabajo = estadoTrabajo;
        this.area = area;
    }

    public Trabajo() {
    }

    public Organizacion getOrganizacion() {return organizacion;}

    public Boolean estaActivo() {
        return this.estadoTrabajo == EstadoTrabajo.ACTIVO;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void cambiarEstado(EstadoTrabajo estadoTrabajo){
        this.estadoTrabajo = estadoTrabajo;
    }

    public int getId_trabajo() {
        return id_trabajo;
    }

    public Area getArea() {
        return area;
    }

    public String getNombreOrganizacion() {
        return organizacion.getRazonSocial();
    }

    public String getRazonSocial() {
        return organizacion.getRazonSocial();
    }

    public String getNombreArea() {
        return area.getNombreDeArea();
    }

    public String getEstadoTrabajo() {

        switch (estadoTrabajo){
            case PENDIENTE:
                return "Pendiente";
            case ACTIVO:
                return "Activo";
            case BAJA:
                return "Baja";
            case RECHAZADA:
                return "Rechazada";
            default:
                return "NSNC";
        }
    }

    public Integer getIdTrabajo() { return this.getId_trabajo(); }

    public Integer getIdEmpleado(){
        return this.empleado.getId_empleado();
    }

    public String getNombreEmpleado(){
        return this.empleado.getNombre();
    }

    public String getApellidoEmpleado(){
        return this.empleado.getApellido();
    }

    public String getDocumentoEmpleado(){
        return this.empleado.getDocumento();
    }

    public Double getHuellaCarbon() {
        this.setHuellaCarbon();
        return this.huellaCarbon;
    }

    public TrabajoDTO convertirADTO(){
        return new TrabajoDTO(this);
    }

    public void setHuellaCarbon() {
        List<Tramo> tramos = this.empleado.getTramos(this);
        this.huellaCarbon = tramos.stream().mapToDouble(t -> t.calcularHC()).sum();
    }

    public class TrabajoDTO{
        public String idTrabajo;
        public String idEmpleado;
        public String nombreEmpleado;
        public String apellidoEmpleado;
        public String documentoEmpleado;

        public TrabajoDTO(Trabajo trabajo){
            this.idTrabajo = Integer.toString(trabajo.getIdTrabajo());
            this.idEmpleado = Integer.toString(trabajo.getIdEmpleado());
            this.nombreEmpleado = trabajo.getNombreEmpleado();
            this.apellidoEmpleado = trabajo.getApellidoEmpleado();
            this.documentoEmpleado = trabajo.getDocumentoEmpleado();
        }

        public String getIdTrabajo() {
            return idTrabajo;
        }

        public String getNombreEmpleado() {
            return nombreEmpleado;
        }

        public String getApellidoEmpleado() {
            return apellidoEmpleado;
        }

        public String getDocumentoEmpleado() {
            return documentoEmpleado;
        }

        public String getIdEmpleado() {
            return idEmpleado;
        }


    }
}
