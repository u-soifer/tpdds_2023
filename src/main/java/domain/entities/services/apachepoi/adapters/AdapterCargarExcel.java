package domain.entities.services.apachepoi.adapters;

import domain.entities.organizacion.Organizacion;

import java.io.IOException;

public interface AdapterCargarExcel {
    public void cargarExcel(String rutaDeExcel, Organizacion organizacion) throws IOException;
}
