package domain.entities.organizacion;

import domain.entities.mediosDeTransporte.TipoCombustible;

public enum TipoOrganizacion {
        GUBERNAMENTAL,ONG,INSTITUCION,EMPRESA;

        public String getTipoOrganizacion(){return toString();}
        public String getId(){
                return String.valueOf(ordinal());
        }

        public TipoOrganizacion converter(Integer j){
                switch (j){
                        case 0: return GUBERNAMENTAL;
                        case 1: return ONG;
                        case 2: return INSTITUCION;
                        case 3: return EMPRESA;
                        default: return null;
                }
        }

}
