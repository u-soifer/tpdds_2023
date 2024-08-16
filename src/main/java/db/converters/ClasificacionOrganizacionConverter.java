package db.converters;

import domain.entities.organizacion.ClasificacionOrganizacion;
import domain.entities.organizacion.TipoOrganizacion;

public class ClasificacionOrganizacionConverter {

    public static ClasificacionOrganizacion convertToEntityAttribute(Integer i){
        switch (i){
            case 0:
                return ClasificacionOrganizacion.MINISTERIO;
            case 1:
                return ClasificacionOrganizacion.UNIVERSIDAD;
            case 2:
                return ClasificacionOrganizacion.ESCUELA;
            case 3:
                return ClasificacionOrganizacion.EMPRESA_SECTOR_PRIMARIO;
            case 4:
                return ClasificacionOrganizacion.EMPRESA_SECTOR_SECUNDARIO;
            default:
                return null;
        }
    }
}
