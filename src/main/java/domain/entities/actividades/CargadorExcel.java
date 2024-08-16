package domain.entities.actividades;

import domain.entities.organizacion.Organizacion;
import domain.entities.services.apachepoi.adapters.AdapterCargarExcel;

import java.io.IOException;

public class CargadorExcel {
    AdapterCargarExcel adapterCargarExcel;

    public CargadorExcel(AdapterCargarExcel adapterCargarExcel) {
        this.adapterCargarExcel = adapterCargarExcel;
    }

    public void CargarExcel(String ruta, Organizacion organizacion) throws IOException {
        this.adapterCargarExcel.cargarExcel(ruta, organizacion);
    }
}
