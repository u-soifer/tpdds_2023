package domain.entities.mediosDeTransporte;

import domain.entities.actividades.Consumo;
import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Parada;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@DiscriminatorValue("TP")
public class TransportePublico extends MedioDeTransporte {
    @Column(name = "tipoTransportePublico")
    @Enumerated(EnumType.ORDINAL)
    private TipoTransportePublico tipo;

    @Column
    private String linea;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Parada> paradas;

    @ManyToOne
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo")
    private static Consumo consumo;

    public TransportePublico() {
        this.paradas = new ArrayList<>();
        sePuedeCompartir = false;
    }


    public static void setConsumo(Consumo nuevoConsumo){consumo = nuevoConsumo;}

    public List<Parada> getParadas() {
        return paradas;
    }

    public TransportePublico(TipoTransportePublico tipo, String linea) {
        this.tipo = tipo;
        this.linea = linea;
        this.paradas = new ArrayList<>();
        sePuedeCompartir = false;
    }

    public void agregarParadas(Parada ... paradas) {
        Collections.addAll(this.paradas, paradas);
    }

    @Override
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino) {

        Double distanciaRecorrida = 0.0;

        Integer orgIdx = this.indexOfParada(origen);
        Integer desIdx = this.indexOfParada(destino);

        if(orgIdx == -1 || desIdx == -1) {
            throw new IllegalArgumentException("Las paradas no pertenecen al transporte");
        }

        if(orgIdx < desIdx) {
            for (int i = orgIdx; i < desIdx; i++) {
                distanciaRecorrida += this.paradas.get(i).getDistanciaParadaSiguiente().valor;
            }
        }
        else if (orgIdx > desIdx){
            for (int i = orgIdx; i > desIdx; i--) {
                distanciaRecorrida += this.paradas.get(i).getDistanciaParadaAnterior().valor;
            }
        }

        Distancia distancia = new Distancia(distanciaRecorrida, "KM");

        return distancia;
    }

    public Integer getTipoOrdinal() {
        return tipo.ordinal();
    }

    @Override
    public Double getFE() {
        return consumo.getFactorDeEmision();
    }

    @Override
    public Boolean sePuedeCompartir(Integer cantEmpleados) {
        return this.sePuedeCompartir;
    }

    private Integer indexOfParada(Ubicacion ubicacion) {
        for (int i = 0; i < this.paradas.size(); i++) {
            if(this.paradas.get(i).getPais().equals(ubicacion.getPais())      &&
               this.paradas.get(i).getProvincia().equals(ubicacion.getProvincia()) &&
               this.paradas.get(i).getMunicipio().equals(ubicacion.getMunicipio()) &&
               this.paradas.get(i).getLocalidad().equals(ubicacion.getLocalidad()) &&
               this.paradas.get(i).getCalle().equals(ubicacion.getCalle())     &&
               this.paradas.get(i).getAltura().equals(ubicacion.getAltura())) {
                return i;
            }
        }
        return -1;
    }

    public String getLinea() { return this.linea; }

    @Override
    public String getTipoTransporte(){
        return "Transporte público";
    }

    @Override
    public Object getDTO(){
        return new TransportePublicoDTO(this);
    }

    public TipoTransportePublico getTipoTransportePublico() { return this.tipo; }

    public class TransportePublicoDTO{

        public Integer id_tipoTransportePublico;
        public Integer id_linea;
        public String codMedioDeTransporte;

        public TransportePublicoDTO(TransportePublico transportePublico){
            this.id_tipoTransportePublico = transportePublico.getTipoOrdinal();
            this.id_linea = transportePublico.getId_medioDeTransporte();
            this.codMedioDeTransporte = "tp";
        }

    }
}
