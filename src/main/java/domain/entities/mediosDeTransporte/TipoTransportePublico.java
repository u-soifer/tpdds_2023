package domain.entities.mediosDeTransporte;

public enum TipoTransportePublico {
    TREN,SUBTE,COLECTIVO;

    public String getTransporte(){
        return toString();
    }

    public String getId(){
        return String.valueOf(ordinal());
    }
}
