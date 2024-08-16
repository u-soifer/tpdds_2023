package db.converters;

import domain.entities.mediosDeTransporte.TipoCombustible;
import domain.entities.mediosDeTransporte.TipoVehiculo;

public class TipoCombustibleConverter {

    public static TipoCombustible convertToEntityAttribute(Integer i){
        switch (i){
            case 0:
                return TipoCombustible.GNC;
            case 1:
                return TipoCombustible.NAFTA;
            case 2:
                return TipoCombustible.GASOIL;
            case 3:
                return TipoCombustible.ELECTRICO;
            default:
                return null;
        }
    }
}
