package domain.entities.services.apachepoi.factory;

import domain.entities.services.apachepoi.CargarExcelApachePoi;
import domain.entities.services.apachepoi.adapters.AdapterCargarExcel;

import java.io.IOException;

public class FactoryCargaDeExcel {
    public static AdapterCargarExcel iniciarCargaExcel(MetodoCargarExcel metodo) throws IOException {
        AdapterCargarExcel instancia = null;

        switch (metodo){
            case APACHE_POI: instancia = CargarExcelApachePoi.getInstancia();
        }

        return instancia;
    }
}
