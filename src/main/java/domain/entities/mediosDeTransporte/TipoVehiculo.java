package domain.entities.mediosDeTransporte;

public enum TipoVehiculo {
    AUTO,MOTO,CAMIONETA;

    public String getVehiculo(){
        return toString();
    }

    public String getId(){
        return String.valueOf(ordinal());
    }

}
