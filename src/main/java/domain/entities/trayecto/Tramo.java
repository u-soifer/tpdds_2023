package domain.entities.trayecto;

import domain.entities.mediosDeTransporte.BiciPie;
import domain.entities.mediosDeTransporte.VehiculoParticular;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;
import domain.entities.mediosDeTransporte.MedioDeTransporte;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tramo")
public class Tramo {
    @Id
    @GeneratedValue
    private int id_tramo;

    @ManyToOne
    @JoinColumn(name = "id_origen", referencedColumnName = "id_ubicacion")
    private Ubicacion origen;

    @ManyToOne
    @JoinColumn(name = "id_destino", referencedColumnName = "id_ubicacion")
    private Ubicacion destino;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_medioDeTransporte", referencedColumnName = "id_medioDeTransporte")
    private MedioDeTransporte medioDeTransporte;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Empleado> empleados;

    @ManyToOne
    @JoinColumn(name = "id_organizacion", referencedColumnName = "id_organizacion")
    private Organizacion organizacion;

    @Column
    private double huellaCarbon;

    @Embedded
    private Distancia distancia;

    public Tramo(Ubicacion origen, Ubicacion destino, MedioDeTransporte medioDeTransporte, Organizacion organizacion){
        this.origen = origen;
        this.destino = destino;
        this.medioDeTransporte = medioDeTransporte;
        this.empleados = new ArrayList<>();
        this.organizacion = organizacion;
    }

    public Tramo() {
        this.empleados = new ArrayList<>();
    }

    public void agregarEmpleado(Empleado empleado) {
        if(empleado.perteneceA(this.organizacion))
            this.empleados.add(empleado);
    }

    public void compartirCon(Empleado empleado) {
        if(medioDeTransporte.sePuedeCompartir(this.empleados.size()) && empleado.perteneceA(organizacion))
            this.empleados.add(empleado);
    }

    public void removerEmpleado(Empleado empleado) {
        this.empleados.remove(empleado);
    }

    public Distancia calcularDistancia(){
        this.distancia = this.medioDeTransporte.calcularDistancia(origen, destino);
        return distancia;
    }

    public Double calcularHC(){
        huellaCarbon = distancia.valor * this.medioDeTransporte.getFE() / (double) empleados.size();
        return huellaCarbon;
    }

    public Boolean perteneceA(Organizacion organizacion){
        return this.organizacion == organizacion;
    }

    public void setOrigen(Ubicacion origen) {
        this.origen = origen;
    }

    public void setDestino(Ubicacion destino) {
        this.destino = destino;
    }

    public void setMedioDeTransporte(MedioDeTransporte medioDeTransporte) {
        this.medioDeTransporte = medioDeTransporte;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public int getId_tramo() {
        return id_tramo;
    }

    public Ubicacion getOrigen() {
        return origen;
    }

    public Ubicacion getDestino() {
        return destino;
    }

    public MedioDeTransporte getMedioDeTransporte() {
        return medioDeTransporte;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public double getHuellaCarbon() {
        return huellaCarbon;
    }

    public Distancia getDistancia() {
        return distancia;
    }

    public Boolean esCompartible(){
        return this.medioDeTransporte.sePuedeCompartir(this.empleados.size());
    }

    public TramoDTO convertirADTO(){
        return new TramoDTO(this);
    }

    public class TramoDTO{
        public String id_tramo;
        public Integer id_organizacionResponsable;
        public Integer id_origen;
        public String origen;
        public Integer id_destino;
        public String destino;
        public String medioDeTransporte;
        public String distancia;
        public String acompaniantes;
        public Object medioDeTransporteDet;

        public TramoDTO(Tramo tramo){
            this.id_tramo = String.valueOf(tramo.getId_tramo());
            this.id_organizacionResponsable = tramo.getOrganizacion().getId_organizacion();
            this.id_origen = tramo.getOrigen().getId_ubicacion();
            this.origen = tramo.getOrigen().getNombre();
            this.id_destino = tramo.getDestino().getId_ubicacion();
            this.destino = tramo.getDestino().getNombre();
            this.medioDeTransporte = tramo.getMedioDeTransporte().getTipoTransporte();
            this.medioDeTransporteDet = tramo.medioDeTransporte.getDTO();

            this.acompaniantes = "";
            for (Empleado empleado : tramo.getEmpleados()){
                this.acompaniantes += empleado.getNombre() + " " + empleado.getApellido() + "\n";
            }
            
            try {
                this.distancia = tramo.distancia.valor.toString() + tramo.distancia.unidad;
            }
            catch (Exception e){
                tramo.calcularDistancia();
                this.distancia = tramo.distancia.valor.toString() + tramo.distancia.unidad;
            }
        }
    }
}
