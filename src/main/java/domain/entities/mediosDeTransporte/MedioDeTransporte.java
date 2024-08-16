package domain.entities.mediosDeTransporte;

import domain.entities.services.geodds.entities.Distancia;
import domain.entities.ubicacion.Ubicacion;

import javax.persistence.*;

@Entity
@Table(name = "medio_de_transporte")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_transporte")
public abstract class MedioDeTransporte {

    @Id
    @GeneratedValue
    private int id_medioDeTransporte;

    @Column
    protected Boolean sePuedeCompartir;

    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        return null;
    }

    public Boolean sePuedeCompartir(Integer cantEmpleados){
        return sePuedeCompartir;
    }

    public Double getFE(){
        return null;
    }

    public int getId_medioDeTransporte() {
        return id_medioDeTransporte;
    }

    public Boolean getSePuedeCompartir() {
        return sePuedeCompartir;
    }

    public String getTipoTransporte(){
        return null;
    }

    public Object getDTO() {return null;}
}
