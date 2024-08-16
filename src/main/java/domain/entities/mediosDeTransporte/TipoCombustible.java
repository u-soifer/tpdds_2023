package domain.entities.mediosDeTransporte;

public enum TipoCombustible {
    GNC,NAFTA,GASOIL,ELECTRICO;

    public String getCombustible(){
        return toString();
    }

    public String getId(){
        return String.valueOf(ordinal());
    }

    public TipoCombustible converter(Integer i){
        switch (i){
            case 0: return GNC;
            case 1: return NAFTA;
            case 2: return GASOIL;
            case 3: return ELECTRICO;
            default: return null;
        }
    }
}
