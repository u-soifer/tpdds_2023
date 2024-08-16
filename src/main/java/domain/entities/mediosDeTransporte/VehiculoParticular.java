package domain.entities.mediosDeTransporte;

import config.Config;
import domain.entities.actividades.Consumo;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;
import domain.entities.services.geodds.distancia.ServicioDistancia;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.io.IOException;

@Entity
@DiscriminatorValue("VP")
public class VehiculoParticular extends MedioDeTransporte{
    @Column(name = "tipoVehiculo")
    @Enumerated(EnumType.ORDINAL)
    private TipoVehiculo tipo;

    @Column(name = "tipoCombustible")
    @Enumerated(EnumType.ORDINAL)
    private TipoCombustible combustible;

    @Transient
    private ServicioDistancia servicioDistancia;

    @ManyToOne
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo")
    private static Consumo consumo;

    @Column
    Integer capacidad;

    public VehiculoParticular() throws IOException {
        sePuedeCompartir = true;
        this.servicioDistancia = Config.getServicioDistancia();
    }

    public static void setConsumo(Consumo nuevoConsumo){consumo = nuevoConsumo;}

    public VehiculoParticular(TipoVehiculo tipo, TipoCombustible combustible, Integer capacidad) throws IOException {
        this.tipo = tipo;
        this.combustible = combustible;
        this.capacidad = capacidad;
        this.sePuedeCompartir = true;
        this.servicioDistancia = Config.getServicioDistancia();
    }

    public Boolean sePuedeCompartir(Integer cantEmpleados){
        return sePuedeCompartir && cantEmpleados < this.capacidad;
    }

    @Override
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        return servicioDistancia.calcularDistancia(origen, destino);
    }

    @Override
    public Double getFE() {
        return consumo.getFactorDeEmision();
    }

    @Override
    public String getTipoTransporte(){
        return "Vehiculo particular";
    }

    public TipoVehiculo getTipo() {
        return tipo;
    }

    public TipoCombustible getCombustible() {
        return combustible;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    @Override
    public Object getDTO(){
        return new VehiculoParticularDTO(this);
    }

    public class VehiculoParticularDTO{

        public Integer tipoVehiculo;
        public Integer tipoCombustible;
        public Integer capacidadVehiculoParticular;
        public String codMedioDeTransporte;

        public VehiculoParticularDTO(VehiculoParticular vehiculoParticular){
            this.tipoVehiculo = vehiculoParticular.getTipo().ordinal();
            this.tipoCombustible = vehiculoParticular.getTipo().ordinal();
            this.capacidadVehiculoParticular = vehiculoParticular.getCapacidad();
            this.codMedioDeTransporte = "vp";
        }

    }

}
