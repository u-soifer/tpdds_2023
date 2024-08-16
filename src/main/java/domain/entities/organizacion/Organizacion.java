package domain.entities.organizacion;

import domain.entities.actividades.CargadorExcel;
import domain.entities.actividades.Medicion;
import domain.entities.agentessectoriales.SectorTerritorial;
import domain.entities.usuario.Usuario;
import domain.entities.actividades.*;
import domain.entities.notificaciones.Contacto;
import domain.entities.notificaciones.Via;
import domain.entities.services.apachepoi.factory.FactoryCargaDeExcel;
import domain.entities.services.apachepoi.factory.MetodoCargarExcel;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

@Entity
@Table(name="organizacion")
public class Organizacion {

    @Id
    @GeneratedValue
    private int id_organizacion;

    @Column
    private String razonSocial;

    @Enumerated(EnumType.ORDINAL)
    private TipoOrganizacion tipoOrganizacion;

    @Column
    private double huellaCarbon;

    @ManyToOne
    @JoinColumn(name = "id_ubicacion", referencedColumnName = "id_ubicacion")
    private Ubicacion lugar;

    @OneToMany(mappedBy = "organizacion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Area> areas;

    @Enumerated(EnumType.ORDINAL)
    private ClasificacionOrganizacion clasificacion;

    @OneToMany(mappedBy = "organizacion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Trabajo> empleadosIngresantes;

    @OneToMany(mappedBy = "organizacion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Medicion> mediciones;

    @Transient
    private CargadorExcel cargarExcel;

    @OneToMany(mappedBy = "organizacion", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Contacto> contactos;

    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private Usuario usuario;

    public Organizacion(String razonSocial, TipoOrganizacion tipoOrganizacion, Ubicacion lugar, ClasificacionOrganizacion clasificacion) throws IOException {
        this.razonSocial = razonSocial;
        this.tipoOrganizacion = tipoOrganizacion;
        this.lugar = lugar;
        this.clasificacion = clasificacion;
        this.areas = new ArrayList<>();
        this.empleadosIngresantes = new ArrayList<>();
        this.mediciones = new ArrayList<>();
        this.cargarExcel = new CargadorExcel(FactoryCargaDeExcel.iniciarCargaExcel(MetodoCargarExcel.APACHE_POI));
        this.contactos = new ArrayList<>();
    }

    public Organizacion() {

    }

    //TODO al buscar por entity manager no se crea el CargadorExcel
    public void setCargarExcel() throws IOException {
        this.cargarExcel = new CargadorExcel(FactoryCargaDeExcel.iniciarCargaExcel(MetodoCargarExcel.APACHE_POI));
    }

    public int getId_organizacion() {
        return id_organizacion;
    }

    public Ubicacion getLugar() {
        return lugar;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial){this.razonSocial = razonSocial;}

    public void setLugar(Ubicacion lugar) {
        this.lugar = lugar;
    }

    public List<Empleado> getEmpleados(){
        List<Empleado> empleados = new ArrayList<>();
        for (Area area:areas){
            empleados.addAll(area.getEmpleados());
        }
        return empleados;
    }

    public TipoOrganizacion getTipoOrganizacion() { return this.tipoOrganizacion; }
    public void setTipoOrganizacion(TipoOrganizacion tipoOrg){this.tipoOrganizacion=tipoOrg;}

    public ClasificacionOrganizacion getClasificacion() { return this.clasificacion; }
    public void setClasificacion(ClasificacionOrganizacion clasificacionOrg){this.clasificacion=clasificacionOrg;}

    public void setUsuario (Usuario usuario){
        this.usuario = usuario;
    }

    public Area areaDeEmpleado (Empleado empleado){
        return areas.stream().filter(area-> area.empleadoPertenece(empleado)).collect(Collectors.toList()).get(0);
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void agregarEmpleadoIngresante (Empleado empleado){
        Trabajo trabajo = new Trabajo(this, empleado, EstadoTrabajo.PENDIENTE);
        empleadosIngresantes.add(trabajo);
        empleado.agregarTrabajo(trabajo);
    }

    public void sacarEmpleadoIngresante(Empleado empleado){
        empleadosIngresantes.remove(empleado);
    }

    private Trabajo sacarTrabajoDeIngresante(Empleado empleado){
        for (Trabajo trabajo : this.empleadosIngresantes){
            if(trabajo.getEmpleado() == empleado) {
                this.empleadosIngresantes.remove(trabajo);
                return trabajo;
            }
        }

        return null;
    }

    public void ingresarEmpleado (Empleado empleado, Area area){
        Trabajo trabajo = sacarTrabajoDeIngresante(empleado);
        trabajo.cambiarEstado(EstadoTrabajo.ACTIVO);
        trabajo.setArea(area);
        empleado.agregarUbicaciones(this.lugar);
        area.ingresarEmpleado(trabajo);
    }

    public void rechazarEmpleado (Empleado empleado){
        Trabajo trabajo = sacarTrabajoDeIngresante(empleado);
        trabajo.cambiarEstado(EstadoTrabajo.RECHAZADA);
    }

    public void sacarContacto(Contacto contacto){
        this.contactos.remove(contacto);
    }

    public void eliminarArea(Area area){
        this.areas.remove(area);
    }

    public void quitarEmpleado (Empleado empleado){
        this.areaDeEmpleado(empleado).quitarEmpleado(empleado);
    }

    public void agregarAreas (Area ... areas){
        Collections.addAll(this.areas, areas);
    }

    public List<Empleado> getEmpleadosIngresantes (){
        return empleadosIngresantes.stream().map(a -> a.getEmpleado()).collect(Collectors.toList());
    }

    public void cargarExcel(String rutaDelExcel) throws IOException {
        this.cargarExcel.CargarExcel(rutaDelExcel, this);
    }

    public void registrarMedicion(Medicion medicion){
        this.mediciones.add(medicion);
    }

    public List<Medicion> getMediciones() {
        return mediciones;
    }

    public void limpiarMediciones(){
        this.mediciones = new ArrayList<>();
    }

    public void sumarHC(Double hc) { this.huellaCarbon += hc; }

    public void restarHC(Double hc) { this.huellaCarbon -= hc; }

    public Double calcularHC(){
        this.huellaCarbon = this.calcularHCMediciones() + this.calcularHCEmpleados();
        return huellaCarbon;
    }

    public Double calcularHC(Integer anio){
        return this.calcularHCMediciones(anio) + this.calcularHCEmpleados(anio);
    }

    public Double calcularHC(Integer anio, Integer mes){
        return this.calcularHCMediciones(anio, mes) + this.calcularHCEmpleados(anio, mes);
    }

    public Double calcularHC(Integer anio, Area area){
        if(this.areas.contains(area))
            return area.getEmpleados().stream().mapToDouble(a -> a.calcularHC(anio,a.getTrabajo(this))).sum();
        else return null;
    }

    public Double calcularHC(Integer anio, Integer mes, Area area){
        if(this.areas.contains(area))
        return area.getEmpleados().stream().mapToDouble(a -> a.calcularHC(anio,mes,a.getTrabajo(this))).sum();
        else return 0.0;
    }

    public Double calcularHCMediciones() {
        return this.mediciones.stream()
                .collect(Collectors.toList()).stream()
                .mapToDouble(a -> a.getHuellaCarbon())
                .sum();
    }

    public Double calcularHCMediciones(Integer anio){
        return this.mediciones.stream().filter(a -> a.perteneceAAnio(anio)).mapToDouble(Medicion::getHuellaCarbon).sum();
    }

    public Double calcularHCMediciones(Integer anio, Integer mes) {
        return this.mediciones.stream().filter(a -> a.perteneceAAnioMes(anio, mes))
                .collect(Collectors.toList()).stream()
                .mapToDouble(a -> a.calcularHC())
                .sum();
    }

    public Double calcularHCEmpleados(){
        return this.getEmpleados().stream().mapToDouble(e -> e.calcularHC(e.getTrabajo(this))).sum();
    }

    public Double calcularHCEmpleados(Integer anio){
       return this.getEmpleados().stream().mapToDouble(a -> a.calcularHC(anio,a.getTrabajo(this))).sum();
    }

    public Double calcularHCEmpleados(Integer anio, Integer mes){
        return this.getEmpleados().stream().mapToDouble(a -> a.calcularHC(anio, mes,a.getTrabajo(this))).sum();
    }

    public Double impactoDeEmpleado(Integer anio, Empleado empleado){
        Double impactoEmpleado = empleado.calcularHC(anio, empleado.getTrabajo(this));
        Double impactoEmpresa = this.calcularHC(anio);
        Double porcentajeImpacto = impactoEmpleado * 100.0 / impactoEmpresa;
        return porcentajeImpacto;
    }

    public Double impactoDeEmpleado(Integer anio,Integer mes, Empleado empleado){
        Double impactoEmpleado = empleado.calcularHC(anio, mes, empleado.getTrabajo(this));
        Double impactoEmpresa = this.calcularHC(anio,mes);
        Double porcentajeImpacto = impactoEmpleado * 100.0 / impactoEmpresa;
        return porcentajeImpacto;
    }

    //TODO convendra delegar esto(lo que esta abajo de este comentario) al area?

    public Double indicadorDeArea(Integer anio, Area area){
        Double impactoArea = this.calcularHC(anio, area);
        Integer cantidadDeMiembros = area.getEmpleados().size();
        return impactoArea/cantidadDeMiembros;
    }

    public Double indicadorDeArea(Integer anio, Integer mes,Area area){
        Double impactoArea = this.calcularHC(anio, mes,area);
        Integer cantidadDeMiembros = area.getEmpleados().size();
        return impactoArea/cantidadDeMiembros;
    }


    ////////////////////////////////////////////////////////////////////////////

    //TODO no lo pide esta entrega pero lo dejo porcia

    public Double indicadorDeTodasLasAreas(Integer anio){
        Double total = 0.0;
        for (Area area : this.areas) {
            total += indicadorDeArea(anio, area);
        }
        return total;
    }

    public Double indicadorDeTodasLasAreas(Integer anio, Integer mes){
        Double total = 0.0;
        for (Area area : this.areas) {
            total += indicadorDeArea(anio, mes,area);
        }
        return total;
    }

    ////////////////////////////////////////////////////////////////////////////

    public void agregarContacto (Contacto ... contactos){
        Collections.addAll(this.contactos, contactos);
    }

    public void quitarContacto(Contacto contacto){
        this.contactos.remove(contacto);
    }

    public void enviarRecomendaciones(String link){
        /*
        this.contactos.forEach(a -> {
            try {
                a.serNotificadoMail("Guía de Recomendaciones", link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.contactos.forEach(a-> {
            try {
                a.serNotificadoWpp(link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    */

        this.contactos.forEach(a -> {
            try {
                a.serNotificado(Via.MAIL, "Guia de recomendaciones", link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        this.contactos.forEach(a -> {
            try {
                a.serNotificado(Via.WHATSAPP, "Guia de recomendaciones", link);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public List<Contacto> getContactos(){
        return contactos;
    }

    //TODO: borrar esto
    public void setHC(Double HC){
        this.huellaCarbon = HC;
    }

    public Organizacion.OrganizacionDTO convertirADTO(){
        return new Organizacion.OrganizacionDTO(this);
    }

    public Double getHc() { return this.huellaCarbon; }

    public class OrganizacionDTO{
        public String idOrganizacion;
        public String razonSocial;
        public String tipoOrganizacion;
        public String clasificacion;
        public Ubicacion ubicacionOrganizacion;
        public List<Area> areas;
        public List<Medicion> mediciones;
        public Double hc;

        public OrganizacionDTO(Organizacion organizacion){
            this.idOrganizacion = Integer.toString(organizacion.getId_organizacion());
            this.razonSocial = organizacion.getRazonSocial();
            this.tipoOrganizacion = organizacion.getTipoOrganizacion().name();
            this.clasificacion = organizacion.getClasificacion().getClasificacionOrganizacion();
            this.ubicacionOrganizacion = organizacion.getLugar();
            this.areas = organizacion.getAreas();
            this.mediciones = organizacion.getMediciones();
            this.hc = organizacion.calcularHC();
        }

        public String getIdOrganizacion() {
            return idOrganizacion;
        }

        public String getRazonSocial() {
            return razonSocial;
        }

        public String getTipoOrganizacion() { return tipoOrganizacion; }

        public String getClasificacion() { return clasificacion; }

        public Ubicacion getUbicacionOrganizacion() { return ubicacionOrganizacion; }

        public List<Area> getAreas() {
            return areas;
        }

        public List<Medicion> getMediciones() {
            return mediciones;
        }

        public Double getHC() { return hc; }
    }


}
