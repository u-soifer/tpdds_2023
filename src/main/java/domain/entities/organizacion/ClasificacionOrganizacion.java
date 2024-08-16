package domain.entities.organizacion;

public enum ClasificacionOrganizacion {
        MINISTERIO,
        UNIVERSIDAD,
        ESCUELA,
        EMPRESA_SECTOR_PRIMARIO,
        EMPRESA_SECTOR_SECUNDARIO;

        public String getClasificacionOrganizacion(){ return toString();}
        public String getId(){
                return String.valueOf(ordinal());
        }

        public ClasificacionOrganizacion converter(Integer j){
                switch (j){
                        case 0: return MINISTERIO;
                        case 1: return UNIVERSIDAD;
                        case 2: return ESCUELA;
                        case 3: return EMPRESA_SECTOR_PRIMARIO;
                        case 4: return EMPRESA_SECTOR_SECUNDARIO;
                        default: return null;
                }
        }
}
