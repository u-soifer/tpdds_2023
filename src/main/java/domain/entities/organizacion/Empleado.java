package domain.entities.organizacion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import domain.entities.ubicacion.Ubicacion;
import domain.entities.usuario.Usuario;
import domain.entities.trayecto.Tramo;
import domain.entities.trayecto.Trayecto;

import javax.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue
    private int id_empleado;

    @Column(name = "nombre", columnDefinition = "VARCHAR(255)")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "tipoDocumento")
    @Enumerated(EnumType.ORDINAL)
    private TipoDocumento tipoDocumento;

    @Column(name = "numDoc")
    private Integer numDoc;

    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Trabajo> trabajos;

    @OneToMany(mappedBy = "empleado", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Trayecto> recorrido;

    @Column
    private double huellaCarbon;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Ubicacion> ubicaciones;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    public List<Ubicacion> getUbicaciones() {
        return ubicaciones;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public Empleado(String nombre, String apellido, TipoDocumento tipoDocumento, Integer numDoc) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numDoc = numDoc;
        this.trabajos = new ArrayList<>();
        this.recorrido = new ArrayList<>();
        this.ubicaciones = new ArrayList<>();
    }

    public Empleado() {
        this.trabajos = new ArrayList<>();
        this.recorrido = new ArrayList<>();
        this.ubicaciones = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Area getArea(Organizacion organizacion){
        return organizacion.areaDeEmpleado(this);
    }

    public Trabajo getTrabajo(Organizacion organizacion){return trabajos.stream().filter(a->a.getOrganizacion()==organizacion).findFirst().get();}

    public void solicitarVinculacion(Organizacion organizacion){
        organizacion.agregarEmpleadoIngresante(this);
    }

    public void setUsuario (Usuario usuario){
        this.usuario = usuario;
    }

    public void agregarTrabajo(Trabajo trabajo) {
        this.trabajos.add(trabajo);
    }

    public List<Trabajo> getTrabajos() {
        return trabajos;
    }

    public void agregarTrayecto(Trayecto trayecto) {
        recorrido.add(trayecto);
        trayecto.agregarEmpleadoATramos(this);
    }

    public List<Trayecto> getTrayectos(){
        return this.recorrido;
    }

    public String getDocumento(){
        return this.tipoDocumento.toString() + " " + this.numDoc;
    }

    public List<Tramo> getTramos(){
        return this.recorrido.stream().flatMap(a-> a.getTramos().stream()).collect(Collectors.toList());
    }

    public List<Tramo> getTramos(Trabajo trabajo){
        return this.recorrido.stream().flatMap(a-> a.getTramos(trabajo).stream()).collect(Collectors.toList());
    }

    public List<Tramo> getTramos(Integer anio, Trabajo trabajo){
        return this.recorrido.stream().filter(a->a.perteneceA(anio)).flatMap(a-> a.getTramos(trabajo).stream()).collect(Collectors.toList());
    }

    public List<Tramo> getTramos(Integer anio, Integer mes, Trabajo trabajo){
        return this.recorrido.stream().filter(a->a.perteneceA(anio, mes)).flatMap(a-> a.getTramos(trabajo).stream()).collect(Collectors.toList());
    }

    public List<Organizacion> getOrganizaciones(){

        return this.trabajos
                .stream().filter(a -> a.estaActivo())
                .map(a -> a.getOrganizacion())
                .collect(Collectors.toList());
    }

    public void agregarUbicaciones(Ubicacion ... ubicaciones){
        Collections.addAll(this.ubicaciones, ubicaciones);
    }

    public void sacarUbicacion(Ubicacion ubicacion){
        this.ubicaciones.remove(ubicacion);
    }

    public Boolean perteneceA(Organizacion organizacion){
        return trabajos.stream().map(a->a.getOrganizacion()).collect(Collectors.toList()).contains(organizacion);
    }


    public Double calcularHC(){
        huellaCarbon = this.getTramos()
                .stream()
                .mapToDouble(Tramo::calcularHC)
                .sum();
        return huellaCarbon;
    }

    public Double calcularHC(Integer anio, Trabajo trabajo){
         return this.getTramos(anio,trabajo).stream().mapToDouble(Tramo::calcularHC).sum();
    }

    public Double calcularHC(Integer anio, Integer mes, Trabajo trabajo){
        return this.getTramos(anio,mes,trabajo)
                .stream()
                .mapToDouble(Tramo::calcularHC)
                .sum();
    }

    public Double calcularHC(Trabajo trabajo){
        return this.getTramos(trabajo)
                .stream()
                .mapToDouble(Tramo::calcularHC)
                .sum();
    }

    public Empleado.EmpleadoDTO convertirADTO(){
        return new Empleado.EmpleadoDTO(this);
    }

    public class EmpleadoDTO{
        public String idEmpleado;
        public String nombreEmpleado;
        public String apellidoEmpleado;
        public String documentoEmpleado;

        public EmpleadoDTO(Empleado empleado){
            this.idEmpleado = Integer.toString(empleado.getId_empleado());
            this.nombreEmpleado = empleado.getNombre();
            this.apellidoEmpleado = empleado.getApellido();
            this.documentoEmpleado = empleado.getDocumento();
        }

        public String getIdEmpleado() {
            return idEmpleado;
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
    }
}
