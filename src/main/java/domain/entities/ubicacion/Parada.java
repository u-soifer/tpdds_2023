package domain.entities.ubicacion;
import domain.entities.services.geodds.entities.*;

import javax.persistence.*;

@Entity
@Table(name = "parada")
public class Parada extends Ubicacion {

    @AttributeOverrides({
            @AttributeOverride(name="valor", column = @Column(name = "valorParadaSiguiente")),
            @AttributeOverride(name="unidad", column = @Column(name = "unidadParadaSiguiente"))
    })
    private Distancia distanciaParadaSiguiente;

    @AttributeOverrides({
            @AttributeOverride(name="valor", column = @Column(name = "valorParadaAnterior")),
            @AttributeOverride(name="unidad", column = @Column(name = "unidadParadaAnterior"))
    })
    private Distancia distanciaParadaAnterior;

    public Parada(Pais pais, Provincia provincia, Municipio municipio, Localidad localidad, String calle, Integer altura, Distancia distanciaParadaSiguiente, Distancia distanciaParadaAnterior) {
        super(pais, provincia, municipio, localidad, calle, altura);
        this.distanciaParadaAnterior = distanciaParadaAnterior;
        this.distanciaParadaSiguiente = distanciaParadaSiguiente;
    }

    public Parada() {

    }

    public Distancia getDistanciaParadaSiguiente() {
        return this.distanciaParadaSiguiente;
    }

    public void setDistanciaParadaSiguiente(Distancia distanciaParadaSiguiente) {
        this.distanciaParadaSiguiente = distanciaParadaSiguiente;
    }

    public Distancia getDistanciaParadaAnterior() {
        return this.distanciaParadaAnterior;
    }

    public void setDistanciaParadaAnterior(Distancia distanciaParadaAnterior) {
        this.distanciaParadaAnterior = distanciaParadaAnterior;
    }

    public static Parada parseParada (Ubicacion ubicacion, Distancia distanciaParadaSiguiente, Distancia distanciaParadaAnterior) {
        Municipio muni = new Municipio(ubicacion.getMunicipio().id, ubicacion.getMunicipio().nombre, ubicacion.getProvincia());
        return new Parada(ubicacion.getPais(), ubicacion.getProvincia(), ubicacion.getMunicipio(), ubicacion.getLocalidad(), ubicacion.getCalle(), ubicacion.getAltura(), distanciaParadaSiguiente, distanciaParadaAnterior);
    }
}
