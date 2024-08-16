package domain.entities.services.geodds.retrofit;

import domain.entities.services.geodds.entities.*;
import domain.entities.services.geodds.entities.*;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

import java.util.List;

public interface GeoddsService {

    @GET("paises")
    Call<List<Pais>> paises(@Query("offset") int offset, @Header("Authorization") String auth);

    @GET("provincias")
    Call<List<Provincia>> provincias(@Query("offset") int offset, @Query("paisId") int paisId, @Header("Authorization") String auth);

    @GET("municipios")
    Call<List<Municipio>> municipios(@Query("offset") int offset, @Query("provinciaId") int provinciaId, @Header("Authorization") String auth);

    @GET("localidades")
    Call<List<Localidad>> localidades(@Query("offset") int offset, @Query("municipioId") int municipioId, @Header("Authorization") String auth);

    @GET("distancia")
    Call<Distancia> distancia(@Query("localidadOrigenId") int localidadOrigenId, @Query("calleOrigen") String calleOrigen, @Query("alturaOrigen") int alturaOrigen,
                              @Query("localidadDestinoId") int localidadDestinoId, @Query("calleDestino") String calleDestino, @Query("alturaDestino") int alturaDestino,
                              @Header("Authorization") String auth);
}
