package domain.entities.actividades;

import domain.entities.organizacion.Organizacion;

import javax.persistence.*;

@Entity
@DiscriminatorValue("LOG")
public class MedidaLogistica extends Medicion {

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo", insertable = false, updatable = false)
    private Consumo consumo;

    @Enumerated(EnumType.ORDINAL)
    private CategoriaProductoTransportado categoriaProductoTransportado;

    @Enumerated(EnumType.ORDINAL)
    private MedioDeTransporteLogistica medioDeTransporte;

    @Column
    private Double distanciaRecorrida;

    @Enumerated(EnumType.ORDINAL)
    private Unidad unidad_distancia;

    @Column
    private Double pesoTotal;

    @Enumerated(EnumType.ORDINAL)
    private Unidad unidad_peso;

    @Column
    private static Double constanteK;

    public MedidaLogistica(Periodicidad periodicidad, String periodoDeImputacion, CategoriaProductoTransportado categoriaProductoTransportado, MedioDeTransporteLogistica medioDeTransporte, Double distanciaRecorrida, Unidad unidad_distancia, Double pesoTotal, Unidad unidad_peso, Organizacion organizacion) {
        super(periodicidad, periodoDeImputacion, organizacion);
        this.categoriaProductoTransportado = categoriaProductoTransportado;
        this.medioDeTransporte = medioDeTransporte;
        this.distanciaRecorrida = distanciaRecorrida;
        this.unidad_distancia = unidad_distancia;
        this.pesoTotal = pesoTotal;
        this.unidad_peso = unidad_peso;
        this.constanteK = 2.0;
        this.consumo = new Consumo(TiposConsumo.LOGISTICA, 2.0);
        this.huellaCarbon = this.calcularHC();
    }

    public MedidaLogistica() {

    }

    @Override
    public Double calcularHC(){
        //HC = DISTANCIA x PESO x FE X K
        return distanciaRecorrida * pesoTotal * consumo.getFactorDeEmision() * constanteK;
    }

    @Override
    public Consumo getConsumo() {
        return consumo;
    }

    public CategoriaProductoTransportado getCategoriaProductoTransportado() {
        return categoriaProductoTransportado;
    }

    public MedioDeTransporteLogistica getMedioDeTransporte() {
        return medioDeTransporte;
    }

    public Double getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    @Override
    public Medicion.MedicionDTO convertirADTO(){
        return new Medicion.MedicionDTO(this, this.consumo, null ,this.distanciaRecorrida, this.pesoTotal,null,this.unidad_distancia, this.unidad_peso,this.categoriaProductoTransportado, this.medioDeTransporte);
    }

}
