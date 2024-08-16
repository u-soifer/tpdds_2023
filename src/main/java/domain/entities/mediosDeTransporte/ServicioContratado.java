package domain.entities.mediosDeTransporte;

import config.Config;
import domain.entities.actividades.Consumo;
import domain.entities.organizacion.Empleado;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;
import domain.entities.services.geodds.distancia.ServicioDistancia;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.trayecto.Tramo;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.io.IOException;

@Entity
@DiscriminatorValue("SC")
public class ServicioContratado extends MedioDeTransporte {

    @Transient
    private ServicioDistancia servicioDistancia;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_tipoDeServicioContratado", referencedColumnName = "id_tipoDeServicioContratado")
    private TipoDeServicioContratado tipo;

    @ManyToOne
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo")
    private static Consumo consumo;

    @Column
    private Integer capacidad;

    public ServicioContratado() throws IOException {
        this.sePuedeCompartir = true;
        this.servicioDistancia = Config.getServicioDistancia();
    }


    public ServicioContratado(TipoDeServicioContratado tipo, Integer capacidad, ServicioDistancia servicioDistancia) throws IOException {
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.sePuedeCompartir = true;
        this.servicioDistancia = servicioDistancia;
        this.servicioDistancia = Config.getServicioDistancia();
    }

    public ServicioContratado(TipoDeServicioContratado tipo, Integer capacidad) throws IOException {
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.sePuedeCompartir = true;
        this.servicioDistancia = Config.getServicioDistancia();
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public static void setConsumo(Consumo nuevoConsumo){consumo = nuevoConsumo;}

    @Override
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        return servicioDistancia.calcularDistancia(origen, destino);
    }

    @Override
    public Boolean sePuedeCompartir(Integer cantEmpleados) {
        return this.sePuedeCompartir && cantEmpleados < this.capacidad;
    }

    @Override
    public Double getFE() {
        return consumo.getFactorDeEmision();
    }

    public TipoDeServicioContratado getTipo() {
        return this.tipo;
    }

    @Override
    public String getTipoTransporte(){
        return "Servicio contratado";
    }

    @Override
    public Object getDTO(){
        return new ServicioContratadoDTO(this);
    }

    public class ServicioContratadoDTO{

        public Integer id_servicioContratado;
        public Integer capacidadServicioContratado;
        public String codMedioDeTransporte;

        public ServicioContratadoDTO(ServicioContratado servicioContratado){
            this.id_servicioContratado = servicioContratado.getTipo().getId_tipoDeServicioContratado();
            this.capacidadServicioContratado = servicioContratado.getCapacidad();
            this.codMedioDeTransporte = "sc";
        }

    }
}

