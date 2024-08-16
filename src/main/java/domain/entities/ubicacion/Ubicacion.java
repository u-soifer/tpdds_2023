package domain.entities.ubicacion;

import domain.entities.services.geodds.entities.Localidad;
import domain.entities.services.geodds.entities.Pais;
import domain.entities.services.geodds.entities.Municipio;
import domain.entities.services.geodds.entities.Provincia;

import javax.persistence.*;

@Entity
@Table(name = "ubicacion")
@Inheritance(strategy = InheritanceType.JOINED)
public class Ubicacion {

    @Id
    @GeneratedValue
    private int id_ubicacion;

    @Column
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_pais", referencedColumnName = "id")
    private Pais pais;

    @ManyToOne
    @JoinColumn(name = "id_provincia", referencedColumnName = "id")
    private Provincia provincia;

    @ManyToOne
    @JoinColumn(name = "id_municipio", referencedColumnName = "id")
    private Municipio municipio;

    @ManyToOne
    @JoinColumn(name = "id_localidad", referencedColumnName = "id")
    private Localidad localidad;

    @Column
    private String calle;

    @Column
    private Integer altura;

    public Ubicacion(Pais pais, Provincia provincia, Municipio municipio, Localidad localidad, String calle, Integer altura) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.altura = altura;
        this.municipio = municipio;
    }

    public Ubicacion(String nombre, Pais pais, Provincia provincia, Municipio municipio, Localidad localidad, String calle, Integer altura) {
        this.pais = pais;
        this.provincia = provincia;
        this.localidad = localidad;
        this.calle = calle;
        this.altura = altura;
        this.municipio = municipio;
        this.nombre = nombre;
    }

    public Ubicacion() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Pais getPais() {
        return pais;
    }

    public String getPaisDet() {
        return pais.nombre;
    }

    public Integer getIdPais() {
        return pais.id;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public String getProvinciaDet() {
        return provincia.nombre;
    }

    public Integer getIdProvincia() {
        return provincia.id;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public String getMunicipioDet() {
        return municipio.nombre;
    }

    public Integer getIdMunicipio() {
        return municipio.id;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    public String getLocalidadDet() {
        return localidad.nombre;
    }

    public Integer getIdLocalidad() {
        return localidad.id;
    }

    public void setLocalidad(Localidad localidad) {
        this.localidad = localidad;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public Integer getAltura() {
        return altura;
    }

    public int getId_ubicacion() {
        return id_ubicacion;
    }

    public void setAltura(Integer altura) {
        this.altura = altura;
    }

    public static Boolean perteneceAlMismoPais(Ubicacion ubicacionA, Ubicacion ubicacionB){
        return ubicacionA.getPais() == ubicacionB.getPais();
    }

    public static Boolean perteneceAMismaProvincia(Ubicacion ubicacionA, Ubicacion ubicacionB){
        return ubicacionA.getPais() == ubicacionB.getPais() &&
                ubicacionA.getProvincia() == ubicacionB.getProvincia();
    }

    public static Boolean perteneceAMismoMunicipio(Ubicacion ubicacionA, Ubicacion ubicacionB){
        return ubicacionA.getPais() == ubicacionB.getPais() &&
                ubicacionA.getProvincia() == ubicacionB.getProvincia() &&
                ubicacionA.getMunicipio() == ubicacionB.getMunicipio();
    }

    public static Boolean perteneceAMismaLocalidad(Ubicacion ubicacionA, Ubicacion ubicacionB){
        return ubicacionA.getPais() == ubicacionB.getPais() &&
                ubicacionA.getProvincia() == ubicacionB.getProvincia() &&
                ubicacionA.getMunicipio() == ubicacionB.getMunicipio() &&
                ubicacionA.getLocalidad() == ubicacionB.getLocalidad();
    }
}
