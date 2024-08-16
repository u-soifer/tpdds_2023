package domain.entities.actividades;


import domain.entities.organizacion.Organizacion;

import javax.persistence.*;

@Entity
@Table(name = "medicion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_medicion")
public abstract class Medicion {
    @Id
    @GeneratedValue
    private int id_medicion;

    @Enumerated(EnumType.ORDINAL)
    private Periodicidad periodicidad;

    @Column
    private String periodoDeImputacion;

    @Column
    double huellaCarbon;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizacion", referencedColumnName = "id_organizacion")
    private Organizacion organizacion;

    @ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_consumo", referencedColumnName = "id_consumo")
    private Consumo consumo;


    public Medicion(Periodicidad periodicidad, String periodoDeImputacion, Organizacion organizacion) {
        this.periodicidad = periodicidad;
        this.periodoDeImputacion = periodoDeImputacion;
        this.organizacion = organizacion;
    }

    public Medicion() {

    }

    public Double calcularHC(){
        return 0.0;
    }

    public Boolean perteneceAAnio(Integer anio){
        return periodicidad == Periodicidad.ANUAL && periodoDeImputacion.equals(anio.toString());
    }

    public Boolean perteneceAAnioMes(Integer anio, Integer mes){
        String cero = String.valueOf(0);
        if(mes.toString().length() == 1){
            cero = cero + mes.toString();
        }
        else{
            cero = mes.toString();
        }
        return periodicidad == Periodicidad.MENSUAL && periodoDeImputacion.equals(cero + '/' + anio);
    }

    public String getPeriodoDeImputacion() {
        return periodoDeImputacion;
    }

    public int getIdConsumo() { return consumo.getIdConsumo(); }

    public Consumo getConsumo() { return consumo; }

    public int getId_medicion() {
        return id_medicion;
    }

    public Periodicidad getPeriodicidad() {
        return periodicidad;
    }

    public double getHuellaCarbon() {
        return huellaCarbon;
    }

    public MedicionDTO convertirADTO(){
        return new Medicion.MedicionDTO(this,null,null,null,null,null,null,null,null,null);
    }

    public class MedicionDTO{
        public String idMedicion;
        public String periodicidadMedicion;
        public String periodoDeImputacionMedicion;
        public String huellaCarbonMedicion;
        public String consumoMedicion;
        public String productoTransportado;
        public String medioTransporte;
        public String valorSimple;
        public String distanciaRecorrida;
        public String peso;

        public MedicionDTO(Medicion medicion,Consumo consumo, Double valor,Double distancia, Double peso,Unidad unidad,Unidad unidadDistancia, Unidad unidadPeso,CategoriaProductoTransportado categoriaProductoTransportado, MedioDeTransporteLogistica medioDeTransporteLogistica){
            this.idMedicion = Integer.toString(medicion.getId_medicion());
            this.periodicidadMedicion = String.valueOf(medicion.getPeriodicidad());
            this.periodoDeImputacionMedicion = medicion.getPeriodoDeImputacion();
            this.huellaCarbonMedicion = String.valueOf(medicion.getHuellaCarbon());
            this.consumoMedicion = String.valueOf(consumo.getTiposConsumo());
            this.productoTransportado = retornaEspacios(String.valueOf(categoriaProductoTransportado));
            this.medioTransporte = retornaEspacios(String.valueOf(medioDeTransporteLogistica));
            this.valorSimple = retornaEspacios(String.valueOf(valor)) + " " + retornaEspacios(String.valueOf(unidad));
            this.distanciaRecorrida = retornaEspacios(String.valueOf(distancia)) + " " + retornaEspacios(String.valueOf(unidadDistancia));
            this.peso = retornaEspacios(String.valueOf(peso)) + " " + retornaEspacios(String.valueOf(unidadPeso));

        }

        public String retornaEspacios(String string){
            if(string != "null")
                return string;
            else return String.valueOf(" ");
        }

        public String getIdMedicion() {
            return idMedicion;
        }

        public String getPeriodicidadMedicion() {
            return periodicidadMedicion;
        }

        public String getPeriodoDeImputacionMedicion() {
            return periodoDeImputacionMedicion;
        }

        public String getHuellaCarbonMedicion() {
            return huellaCarbonMedicion;
        }

        public String getConsumoMedicion() {
            return consumoMedicion;
        }

        public String getProductoTransportado() {
            return productoTransportado;
        }

        public String getMedioTransporte() {
            return medioTransporte;
        }

        public String getValorSimple() {
            return valorSimple;
        }

        public String getDistanciaRecorrida() {
            return distanciaRecorrida;
        }

        public String getPeso() {
            return peso;
        }
    }


}
