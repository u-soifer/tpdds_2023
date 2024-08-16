package domain.entities.services.geodds.retrofit;

import domain.entities.config.Config;
import domain.entities.services.geodds.distancia.ServicioDistanciaAdapter;
import domain.entities.services.geodds.entities.*;
import domain.entities.services.geodds.entities.*;
import domain.entities.services.geodds.ubicacion.ServicioUbicacionAdapter;
import domain.entities.ubicacion.Ubicacion;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.List;

public class ServicioServicioRetrofit implements ServicioDistanciaAdapter, ServicioUbicacionAdapter {

    private static ServicioServicioRetrofit instancia = null;
    private static final String urlAPI = "https://ddstpa.com.ar/api/";
    private Retrofit retrofit;
    private static String bearerAuth;

    private ServicioServicioRetrofit() throws IOException {

        retrofit = new Retrofit.Builder()
                .baseUrl(urlAPI)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bearerAuth = "Bearer "+Config.getProperty("token");
    }

    public static ServicioServicioRetrofit getInstancia() throws IOException {
        if (instancia == null) {
            instancia = new ServicioServicioRetrofit();
        }
        return instancia;
    }

    @Override
    public List<Pais> listadoDePaises(int offset) throws IOException {
        GeoddsService geoddsService = this.retrofit.create(GeoddsService.class);
        Call<List<Pais>> requestPaises = geoddsService.paises(1, bearerAuth);
        Response<List<Pais>> responsePaises = requestPaises.execute();
        return responsePaises.body();
    }

    @Override
    public List<Provincia> listadoDeProvincias(int offset, int paisId) throws IOException {
        GeoddsService geoddsService = this.retrofit.create(GeoddsService.class);
        Call<List<Provincia>> requestProvincias = geoddsService.provincias(offset, paisId, bearerAuth);
        Response<List<Provincia>> responseProvincias = requestProvincias.execute();
        return responseProvincias.body();
    }

    @Override
    public List<Municipio> listadoDeMunicipios(int offset, int provinciaId) throws IOException {
        GeoddsService geoddsService = this.retrofit.create(GeoddsService.class);
        Call<List<Municipio>> requestMunicipios = geoddsService.municipios(offset, provinciaId, bearerAuth);
        Response<List<Municipio>> responseMunicipios = requestMunicipios.execute();
        return responseMunicipios.body();
    }

    @Override
    public List<Localidad> listadoDeLocalidades(int offset, int municipioId) throws IOException {
        GeoddsService geoddsService = this.retrofit.create(GeoddsService.class);
        Call<List<Localidad>> requestLocalidades = geoddsService.localidades(offset, municipioId, bearerAuth);
        Response<List<Localidad>> responseLocalidades = requestLocalidades.execute();
        return responseLocalidades.body();
    }

    private Distancia distancia(int localidadOrigenId, String calleOrigen, int alturaOrigen, int localidadDestinoId, String calleDestino, int alturaDestino) throws IOException{
        GeoddsService geoddsService = this.retrofit.create(GeoddsService.class);
        Call<Distancia> requestDistancia = geoddsService.distancia(localidadOrigenId, calleOrigen, alturaOrigen, localidadDestinoId, calleDestino, alturaDestino, bearerAuth);
        Response<Distancia> responseDistancia = requestDistancia.execute();
        return responseDistancia.body();
    }

    @Override
    public Distancia calcularDistancia(Ubicacion origen, Ubicacion destino){
        try {
            return this.distancia(origen.getLocalidad().id, origen.getCalle(), origen.getAltura(), destino.getLocalidad().id, destino.getCalle(), destino.getAltura());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
