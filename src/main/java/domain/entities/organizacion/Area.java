package domain.entities.organizacion;

import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "area")
public class Area {

    @Id
    @GeneratedValue
    private int id_area;

    @Column
    private String nombreDeArea;

    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Trabajo> empleados;

    @Column
    private double huellaCarbon;

    @ManyToOne
    @JoinColumn(name="id_organizacion", referencedColumnName = "id_organizacion")
    private Organizacion organizacion;

    public Area(String nombreDeArea) {
        this.nombreDeArea = nombreDeArea;
        this.empleados = new ArrayList<>();
    }

    public Area(String nombreDeArea, Organizacion organizacion) {
        this.nombreDeArea = nombreDeArea;
        this.organizacion = organizacion;
        this.empleados = new ArrayList<>();
    }

    public Area() {

    }

    public void setNombreDeArea(String nombreDeArea) {
        this.nombreDeArea = nombreDeArea;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Boolean empleadoPertenece(Empleado empleado){
        return empleados.contains(empleado);
    }

    public List<Empleado> getEmpleados(){
        return empleados.stream().map(a -> a.getEmpleado()).collect(Collectors.toList());
    }

    public void ingresarEmpleado(Trabajo trabajo){
        empleados.add(trabajo);
    }

    public void quitarEmpleado(Empleado empleado){
        empleados.remove(empleado);
    }

    public String getNombreDeArea() {
        return nombreDeArea;
    }

    public int getId_area() { return id_area; }

    public double getHuellaCarbon() {
        return huellaCarbon;
    }

    public void setHuellaCarbon(List<Trabajo> trabajos) {
        this.huellaCarbon = trabajos.stream().mapToDouble(t -> t.getHuellaCarbon()).sum();
    }

    public Organizacion getOrganizacion() { return organizacion; }

    public Area.AreaDTO convertirADTO(){
        return new Area.AreaDTO(this);
    }

    public class AreaDTO{
        public String idArea;
        public String nombreArea;
        public List<Empleado> empleadosArea;
        public Double huellaCarbon;
        public Organizacion organizacion;

        public AreaDTO(Area area){
            this.idArea = Integer.toString(area.getId_area());
            this.nombreArea = area.getNombreDeArea();
            this.empleadosArea = area.getEmpleados();
            this.huellaCarbon = area.getHuellaCarbon();
            this.organizacion = area.getOrganizacion();
        }

        public String getNombreArea() {
            return nombreArea;
        }

        public String getIdArea() {
            return idArea;
        }

        public List<Empleado> getEmpleadosArea() {
            return empleadosArea;
        }

        public String getHuellaCarbon() {
            return this.huellaCarbon.toString();
        }
    }


}
