package com.chiriacd.whitbread.injection.api;

import android.content.Context;

import com.chiriacd.whitbread.foursquare.FoursquareService;
import com.chiriacd.whitbread.whitbread.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class FoursquareApiModule {

    private static final String API_URL = "https://api.foursquare.com/v2/";

    private final int cacheSize = 10 * 1024 * 1024; // 10 MB

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
    OkHttpClient httpClient(Interceptor interceptor, Cache cache) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(interceptor);
        httpClient.cache(cache);
        return httpClient.build();
    }

    @Provides Cache cache(Context context) {
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @Provides
    Interceptor interceptor() {
        return chain -> {
            Request original = chain.request();
            HttpUrl originalHttpUrl = original.url();

            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("client_id", BuildConfig.client_id)
                    .addQueryParameter("client_secret", BuildConfig.client_secret)
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
