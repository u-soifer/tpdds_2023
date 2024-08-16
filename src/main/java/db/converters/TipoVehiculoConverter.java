package db.converters;

import domain.entities.mediosDeTransporte.TipoVehiculo;

public class TipoVehiculoConverter {

    public static TipoVehiculo convertToEntityAttribute(Integer i){
        switch (i){
            case 0:
                return TipoVehiculo.AUTO;
            case 1:
                return TipoVehiculo.MOTO;
            case 2:
                return TipoVehiculo.CAMIONETA;
            default:
                return null;
        }
    }
}
