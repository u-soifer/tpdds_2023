package db.converters;

import domain.entities.organizacion.TipoOrganizacion;

public class TipoOrganizacionConverter {

    public static TipoOrganizacion convertToEntityAttribute(Integer i){
        switch (i){
            case 0:
                return TipoOrganizacion.GUBERNAMENTAL;
            case 1:
                return TipoOrganizacion.ONG;
            case 2:
                return TipoOrganizacion.INSTITUCION;
            case 3:
                return TipoOrganizacion.EMPRESA;
            default:
                return null;
        }
    }
}
