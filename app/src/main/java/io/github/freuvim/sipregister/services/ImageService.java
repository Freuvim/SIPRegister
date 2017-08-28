package io.github.freuvim.sipregister.services;

import io.github.freuvim.sipregister.model.ImageModel;
import io.github.freuvim.sipregister.model.Imsi;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ImageService {
    @POST("registrar")
    Call<ImageModel> enviarRegistro(@Body Imsi imsi);
}
