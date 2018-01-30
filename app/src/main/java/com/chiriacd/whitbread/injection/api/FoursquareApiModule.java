package com.chiriacd.whitbread.injection.api;

import com.chiriacd.whitbread.foursquare.FoursquareService;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class FoursquareApiModule {

    private static final String API_URL = "https://api.foursquare.com/v2/";
    private static final String CLIENT_ID = "Y3FWP0BNWLQE2JENWRIELO0GPAPIFPHUJPZTSOZVOKLIHV0G";
    private static final String CLEINT_SECRET = "KJLLOL2K1A2DM4J1GJVUE00CQHJZLQT11CEWH5HVNY2B1JUI";//todo should be obfuscated

    @Provides
    public Retrofit provideRetrofit(OkHttpClient httpClient) {
        return new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClient)
                .build();
    }

    @Provides
    OkHttpClient httpClient(Interceptor interceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        return httpClient.build();
    }

    @Provides
    Interceptor interceptor() {
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", CLIENT_ID)
                    .addQueryParameter("client_secret", CLEINT_SECRET)
                    .addQueryParameter("v", "20180130")
                    .build();

            Request request = original.newBuilder()
                    .url(url)
                    .build();

            return chain.proceed(request);
        };
    }

    @Provides
    public FoursquareService provideFoursquareService (Retrofit retrofit) {
        return retrofit.create(FoursquareService.class);
    }
}
