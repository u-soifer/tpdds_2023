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
@DiscriminatorValue("BP")
public class BiciPie extends MedioDeTransporte{

    @Transient
    private ServicioDistancia servicioDistancia;

    @ManyToOne
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo")
    private static Consumo consumo;

    public static void setConsumo(Consumo nuevoConsumo){consumo = nuevoConsumo;}

    public BiciPie() throws IOException {
        servicioDistancia = Config.getServicioDistancia();
        sePuedeCompartir = false;
    }

    @Override
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        return servicioDistancia.calcularDistancia(origen, destino);
    }

    @Override
    public Boolean sePuedeCompartir(Integer cantEmpleados) {
        return this.sePuedeCompartir;
    }

    @Override
    public Double getFE() {
        return consumo.getFactorDeEmision();
    }

    @Override
    public String getTipoTransporte(){
        return "Bici/Pie";
    }

    @Override
    public Object getDTO(){
        return new BiciPieDTO(this);
    }

    public class BiciPieDTO{

        public String codMedioDeTransporte;

        public BiciPieDTO(BiciPie biciPie){
            this.codMedioDeTransporte = "bp";
        }

    }
}
