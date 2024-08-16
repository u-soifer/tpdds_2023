package domain.entities.services.geodds.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distancia {
    @Column(name = "valor")
    public Double valor;

    @Column(name = "unidad")
    public String unidad;

    public Distancia(Double valor, String unidad) {
        this.valor = valor;
        this.unidad = unidad;
    }

    public Distancia() {
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

}
