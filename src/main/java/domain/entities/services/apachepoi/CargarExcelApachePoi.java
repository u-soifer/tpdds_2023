package domain.entities.services.apachepoi;

import domain.entities.actividades.*;
import domain.entities.actividades.*;
import domain.entities.organizacion.Organizacion;
import domain.entities.services.apachepoi.adapters.AdapterCargarExcel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class CargarExcelApachePoi implements AdapterCargarExcel {

    Consumo consumoFijoGasNatural = new Consumo(TiposConsumo.GAS_NATURAL, 10.0);
    Consumo consumoFijoDieselGasoil = new Consumo(TiposConsumo.DIESEL_GASOIL, 11.0);
    Consumo consumoFijoKerosene = new Consumo(TiposConsumo.KEROSENE, 12.1);
    Consumo consumoFijoFuelOil = new Consumo(TiposConsumo.FUEL_OIL, 13.1);
    Consumo consumoFijoNafta = new Consumo(TiposConsumo.NAFTA, 14.1);
    Consumo consumoFijoCarbon = new Consumo(TiposConsumo.CARBON, 15.1);
    Consumo consumoFijoCarbonDeLenia = new Consumo(TiposConsumo.CARBON_DE_LENIA, 16.1);
    Consumo consumoFijoLenia = new Consumo(TiposConsumo.LENIA, 17.1);
    Consumo combustibleConsumidoGasoil = new Consumo(TiposConsumo.COMBUSTIBLE_CONSUMIDO_GASOIL, 18.1);
    Consumo combustibleConsumidoGNC = new Consumo(TiposConsumo.COMBUSTIBLE_CONSUMIDO_GNC, 19.1);
    Consumo combustibleConsumidoNafta = new Consumo(TiposConsumo.COMBUSTIBLE_CONSUMIDO_NAFTA, 20.1);
    Consumo consumoElectricidad = new Consumo(TiposConsumo.ELECTRICIDAD, 21.1);

    private static CargarExcelApachePoi instancia = null;


    private CargarExcelApachePoi() {

    }
    public static CargarExcelApachePoi getInstancia() throws IOException{
        if(instancia == null){
            instancia = new CargarExcelApachePoi();
        }
        return instancia;
    }

    @Override
    public void cargarExcel(String rutaDeExcel, Organizacion organizacion) throws IOException {
        File excel = new File(rutaDeExcel);
        MedidaSimple medidaSimple;
        MedidaLogistica medidaLogistica;
        Medicion medicion;
        String periodoDeImputacion;
        Periodicidad periodicidad;
        CategoriaProductoTransportado categoriaProductoTransportado;
        MedioDeTransporteLogistica medioDeTransporteLogistica;
        Double distanciaRecorrida;
        Double peso;

        if (excel.exists()) {

            try {
                FileInputStream fileInputStream = new FileInputStream(excel);

                XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet hojaDeExcel = workbook.getSheetAt(0);

                Iterator fila = hojaDeExcel.rowIterator();

                fila.next();
                fila.next();

                List filaActual = new ArrayList<String>();

                while (fila.hasNext()) {
                    filaActual = obtenerSiguienteFila(fila);

                    periodicidad = leerPeriodicidad(filaActual.get(3).toString());
                    periodoDeImputacion = filaActual.get(4).toString();

                    switch (filaActual.get(0).toString().toLowerCase()) {
                        case "combustion fija":
                            medicion = new MedidaSimple(periodicidad,
                                                        periodoDeImputacion,
                                                        leerConsumo(filaActual.get(1).toString()),
                                                        Double.parseDouble(filaActual.get(2).toString()),
                                                        leerCombustionFijaUnidades(filaActual.get(1).toString()),
                                                        organizacion);
                            organizacion.registrarMedicion(medicion);
                            break;
                        case "combustion movil":
                            medicion = new MedidaSimple(periodicidad,
                                                        periodoDeImputacion,
                                                        leerConsumo(filaActual.get(1).toString()),
                                                        Double.parseDouble(filaActual.get(2).toString()),
                                                        leerCombustionMovilUnidades(filaActual.get(1).toString()),
                                                        organizacion);
                            organizacion.registrarMedicion(medicion);
                            break;
                        case "electricidad adquirida y consumida":
                            medicion = new MedidaSimple(periodicidad,
                                                        periodoDeImputacion,
                                                        leerConsumo(filaActual.get(1).toString()),
                                                        Double.parseDouble(filaActual.get(2).toString()),
                                                        leerElectricidadAdquiridaConsumidaUnidades(filaActual.get(1).toString()),
                                                        organizacion);
                            organizacion.registrarMedicion(medicion);
                            break;
                        case "logistica de productos y residuos":

                            categoriaProductoTransportado = leerProductoTransportado(filaActual.get(2).toString());

                            filaActual = obtenerSiguienteFila(fila);
                            medioDeTransporteLogistica = leerMedioDeTransporteLogistica(filaActual.get(2).toString());

                            filaActual = obtenerSiguienteFila(fila);
                            distanciaRecorrida = Double.parseDouble(filaActual.get(2).toString());

                            filaActual = obtenerSiguienteFila(fila);
                            peso = Double.parseDouble(filaActual.get(2).toString());

                            medicion = new MedidaLogistica( periodicidad,
                                                            periodoDeImputacion,
                                                            categoriaProductoTransportado,
                                                            medioDeTransporteLogistica,
                                                            distanciaRecorrida,
                                                            Unidad.KM,
                                                            peso,
                                                            Unidad.KG,
                                                            organizacion);
                            organizacion.registrarMedicion(medicion);
                            break;

                        default:
                            throw new RuntimeException("El formato de archivo no es correcto");
                    }

                    filaActual.clear();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else throw new RuntimeException("No se encuentra el archivo de excel que quiere cargar.");

    }

    private Periodicidad leerPeriodicidad(String periodicidad) {
        switch (periodicidad.toLowerCase()) {
            case "mensual":
                return Periodicidad.MENSUAL;
            case "anual":
                return Periodicidad.ANUAL;
            default:
                throw new RuntimeException("El formato de archivo no es correcto");
        }
    }

    private Consumo leerConsumo(String consumo) {
        switch (consumo.toLowerCase()) {
            case "gas natural":
                return consumoFijoGasNatural;
            case "diesel/gasoil":
                return consumoFijoDieselGasoil;
            case "kerosene":
                return consumoFijoKerosene;
            case "fuel oil":
                return consumoFijoFuelOil;
            case "leña":
                return consumoFijoLenia;
            case "carbon":
                return consumoFijoCarbon;
            case "carbon de leña":
                return consumoFijoCarbonDeLenia;
            case "nafta":
                return consumoFijoNafta;
            case "electricidad":
                return consumoElectricidad;
            case "combustible consumido - gasoil":
                return combustibleConsumidoGasoil;
            case "combustible consumido - gnc":
                return combustibleConsumidoGNC;
            case "combustible consumido - nafta":
                return combustibleConsumidoNafta;
            default:
                throw new RuntimeException("Error en el formato del archivo");
        }
    }

    private Unidad leerCombustionFijaUnidades(String combustion) {
        switch (combustion.toLowerCase()) {
            case "gas natural":
                return Unidad.M3;
            case "diesel/gasoil":
            case "fuel oil":
            case "kerosene":
            case "nafta":
                return Unidad.LT;
            case "leña":
            case "carbon de leña":
            case "carbon":
                return Unidad.KG;
            default:
                throw new RuntimeException("Error en el formato del archivo");
        }
    }

    private Unidad leerCombustionMovilUnidades(String combustion) {
        switch (combustion.toLowerCase()) {
            case "combustible consumido - gasoil":
            case "combustible consumido - gnc":
            case "combustible consumido - nafta":
                return Unidad.LTS;
            default:
                throw new RuntimeException("Error en el formato del archivo");
        }
    }

    private Unidad leerElectricidadAdquiridaConsumidaUnidades(String electricidad) {
        switch (electricidad.toLowerCase()) {
            case "electricidad":
                return Unidad.KWH;
            default:
                throw new RuntimeException("Error en el formato del archivo");
        }
    }

    private CategoriaProductoTransportado leerProductoTransportado(String producto) {
        switch (producto.toLowerCase()) {
            case "productos vendidos":
                return CategoriaProductoTransportado.PRODUCTOS_VENDIDOS;
            case "materia prima":
                return CategoriaProductoTransportado.MATERIA_PRIMA;
            case "insumos":
                return CategoriaProductoTransportado.INSUMOS;
            case "residuos":
                return CategoriaProductoTransportado.RESIDUOS;
            default:
                throw new RuntimeException("Error en el formato del archivoP");
        }
    }

    private MedioDeTransporteLogistica leerMedioDeTransporteLogistica(String medio) {
        switch (medio.toLowerCase()) {
            case "camion de carga":
                return MedioDeTransporteLogistica.CAMION_DE_CARGA;
            case "utilitario liviano":
                return MedioDeTransporteLogistica.UTILITARIO_LIVIANO;
            default:
                throw new RuntimeException("Error en el formato del archivoM");
        }
    }


    private List<String> obtenerSiguienteFila(Iterator fila){
        List filaAuxiliar = new ArrayList<String>();
        XSSFRow filaDeExcel = (XSSFRow) fila.next();
        Iterator columna = filaDeExcel.cellIterator();

        while (columna.hasNext()) {
            XSSFCell celdaDeExcel = (XSSFCell) columna.next();
            if (!celdaDeExcel.toString().equals("")) {
                filaAuxiliar.add(celdaDeExcel.toString());
            }
        }
        return filaAuxiliar;
    }
}
