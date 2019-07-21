package com.codesense.driverapp.di.module;

import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.net.ApiCallInterface;
import com.codesense.driverapp.net.RequestHandler;
import com.codesense.driverapp.net.WebserviceUrls;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class NetworkModule {

    /*private static final NetworkModule networkModule = new NetworkModule();

    private NetworkModule() {

    }*/

    /*protected static NetworkModule GetInstance() {
        return networkModule;
    }*/

    @Provides
    @Singleton
    protected Gson provideGson() {
        GsonBuilder builder =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return builder.setLenient().create();
    }

    @Provides
    @Singleton
    protected Retrofit provideRetrofit(Gson gson, OkHttpClient okHttpClient) {

        return new Retrofit.Builder()
                .baseUrl(WebserviceUrls.BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    @Provides
    @Singleton
    protected OkHttpClient getRequestHeader() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .build();
            return chain.proceed(request);
        })
                .connectTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(100, TimeUnit.SECONDS)
                .readTimeout(300, TimeUnit.SECONDS);

        return httpClient.build();
    }

    @Provides
    @Singleton
    protected ApiCallInterface getApiCallInterface() {
        OkHttpClient okHttpClient = getRequestHeader();
        Retrofit retrofit = provideRetrofit(provideGson(), okHttpClient);
        return retrofit.create(ApiCallInterface.class);
    }

    @Provides
    @Singleton
    protected RequestHandler provideRequestHandler(ApiCallInterface apiCallInterface) {
         return new RequestHandler(apiCallInterface);
    }

    @Provides
    @Singleton
    protected Utility provideUtility() {
         return new Utility();
    }
}
