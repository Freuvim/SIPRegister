package io.github.freuvim.sipregister.retrofit;


import io.github.freuvim.sipregister.services.ImageService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitBuilder {

    private final Retrofit retrofit;

    public RetrofitBuilder() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        this.retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("http://192.168.1.101:3000/api/")
                .client(client.build())
                .build();
    }

    public ImageService getImageService() {
        return this.retrofit.create(ImageService.class);
    }

}
