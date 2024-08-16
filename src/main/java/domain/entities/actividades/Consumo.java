package domain.entities.actividades;

import javax.persistence.*;

@Entity
@Table(name = "consumo")
public class Consumo {

    @Id
    @GeneratedValue
    private int id_consumo;

    @Enumerated(EnumType.ORDINAL)
    private TiposConsumo tiposConsumo;

    @Column
    private Double FE;

    public Consumo() {

    }

    public Double getFactorDeEmision() { return FE; }

    public Consumo(TiposConsumo tiposConsumo, Double FE) {
        this.tiposConsumo = tiposConsumo;
        this.FE = FE;
    }

    public TiposConsumo getTiposConsumo() {
        return tiposConsumo;
    }

    public Integer getIdConsumo() { return id_consumo; }

    public void setFE(Double FE) {
        this.FE = FE;
    }

    public void setTipoConsumo(TiposConsumo tipoConsumo) { this.tiposConsumo = tipoConsumo; }

}
