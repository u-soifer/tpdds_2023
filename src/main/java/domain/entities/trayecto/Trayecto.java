package domain.entities.trayecto;

import domain.entities.actividades.Periodicidad;
import domain.entities.organizacion.Empleado;
import domain.entities.organizacion.Trabajo;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "trayecto")
public class Trayecto {
    @Id
    @GeneratedValue
    private int id_trayecto;

    @Column
    private String nombreTrayecto;

    @ManyToOne
    @JoinColumn(name = "id_origen", referencedColumnName = "id_ubicacion")
    private Ubicacion origen;

    @ManyToOne
    @JoinColumn(name = "id_destino", referencedColumnName = "id_ubicacion")
    private Ubicacion destino;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Tramo> tramos;

    @ManyToOne
    @JoinColumn(name = "id_empleado", referencedColumnName = "id_empleado")
    private Empleado empleado;

    @Embedded
    private Distancia distancia;

    @Column
    private Double hc;

    @Transient
    private Periodicidad periodicidad;

    @Transient
    private String fecha;

    public Trayecto(Ubicacion origen, Ubicacion destino, Periodicidad periodicidad, String fecha) {
        this.origen = origen;
        this.destino = destino;
        this.tramos = new ArrayList<>();
        this.periodicidad = periodicidad;
        this.fecha = fecha;
    }

    public Trayecto(String nombre, Ubicacion origen, Ubicacion destino, Empleado empleado) {
        this.nombreTrayecto = nombre;
        this.origen = origen;
        this.destino = destino;
        this.empleado = empleado;
        this.tramos = new ArrayList<>();
    }

    public Trayecto() {
        this.tramos = new ArrayList<>();
    }

    public int getId_trayecto() {
        return id_trayecto;
    }

    public Integer getOrigenId() {
        return origen.getId_ubicacion();
    }

    public String getOrigenNombre() {
        return origen.getNombre();
    }

    public Integer getDestinoId() {
        return destino.getId_ubicacion();
    }

    public String getDestinoNombre() {
        return destino.getNombre();
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public String getNombreTrayecto() {
        return nombreTrayecto;
    }

    public List<Tramo> getTramos() {
        return tramos;
    }

    public List<Tramo> getTramos(Trabajo trabajo) {
        return tramos.stream().filter(a -> a.perteneceA(trabajo.getOrganizacion())).collect(Collectors.toList());
    }

    public void setNombreTrayecto(String nombre) {
        this.nombreTrayecto = nombre;
    }

    public void setOrigen(Ubicacion origen) {
        this.origen = origen;
    }

    public void setDestino(Ubicacion destino) {
        this.destino = destino;
    }

    public void agregarTramos(Tramo ... tramo) {
        Collections.addAll(this.tramos, tramo);
    }

    public void calcularDistancia() throws IOException {
        Double distancia = 0.0;
        for (Tramo tramo : tramos) {
            distancia += tramo.calcularDistancia().valor;
        }

        this.distancia = new Distancia(distancia, "KM");
    }

    public void eliminarTramo(Tramo tramo){
        this.tramos.remove(tramo);
    }

    public void agregarEmpleadoATramos(Empleado empleado){
        tramos.forEach(a -> a.agregarEmpleado(empleado));
    }

    public Boolean perteneceA(Integer anio){
        return periodicidad == Periodicidad.ANUAL && fecha.equals(anio.toString());
    }

    public Boolean perteneceA(Integer anio, Integer mes){
        String cero = String.valueOf(0);
        if(mes.toString().length() == 1){
            cero = cero + mes.toString();
        }
        else{
            cero = mes.toString();
        }
        return periodicidad == Periodicidad.MENSUAL && fecha.equals(cero + '/' + anio);
    }

    public void calcularHC(){
        this.hc = this.tramos.stream().mapToDouble(a -> a.calcularHC()).sum();
    }

    public Distancia getDistancia() {
        return distancia;
    }

    public Double getHc() {
        return hc;
    }

    public TrayectoDTO convertirADTO(){
        return new TrayectoDTO(this);
    }

    public class TrayectoDTO{
        public String nombreTrayecto;
        public String origen;
        public String destino;
        public Double hc;
        public String distancia;

        public TrayectoDTO(Trayecto trayecto){
            this.nombreTrayecto = trayecto.getNombreTrayecto();
            this.origen = trayecto.getOrigenNombre();
            this.destino = trayecto.getDestinoNombre();
            this.hc = trayecto.getHc();
            this.distancia = trayecto.getDistancia().getValor() + trayecto.getDistancia().getUnidad();
        }

        public String getNombreTrayecto() {
            return nombreTrayecto;
        }

        public String getOrigen() {
            return origen;
        }

        public String getDestino() {
            return destino;
        }

        public Double getHc() {
            return hc;
        }

        public String getDistancia() {
            return distancia;
        }
    }
}
