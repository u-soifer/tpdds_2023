package domain.entities.actividades;

import domain.entities.organizacion.Organizacion;

import javax.persistence.*;

@Entity
@DiscriminatorValue("SIM")
public class MedidaSimple extends Medicion {

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo", insertable = false, updatable = false)
    private Consumo consumo;

    @Column
    private Double valor;

    @Column
    private Unidad unidad;

    public MedidaSimple(Periodicidad periodicidad, String periodoDeImputacion, Consumo consumo, Double valor, Unidad unidad, Organizacion organizacion) {
        super(periodicidad, periodoDeImputacion, organizacion);
        this.consumo = consumo;
        this.valor = valor;
        this.unidad = unidad;
        this.huellaCarbon = this.calcularHC();
    }

    public MedidaSimple() {

    }

    @Override
    public Double calcularHC(){
        return valor * consumo.getFactorDeEmision();
    }

    @Override
    public Consumo getConsumo() {
        return consumo;
    }

    @Override
    public Medicion.MedicionDTO convertirADTO(){
        return new Medicion.MedicionDTO(this, this.consumo, this.valor, null, null, this.unidad,null,null,null, null);
    }

}
