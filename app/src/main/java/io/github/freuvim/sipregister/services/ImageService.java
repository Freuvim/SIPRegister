package io.github.freuvim.sipregister.services;

import io.github.freuvim.sipregister.model.ImageModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ImageService {

    @GET("registrar")
    Call<ImageModel> registrar();

}
