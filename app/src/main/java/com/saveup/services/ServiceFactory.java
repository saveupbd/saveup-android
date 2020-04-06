package com.saveup.services;


import com.saveup.android.BuildConfig;
import com.saveup.utils.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by AAPBD .
 */
public class ServiceFactory {
    private Retrofit mRetrofit;

    public ServiceFactory(String base_url) {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(base_url)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHttpClient().build())
                .build();
    }

    private OkHttpClient.Builder getHttpClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(150, TimeUnit.SECONDS);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                       // .header("Accept", "application/json")
                        .method(original.method(), original.body())
                        .build();

                return  chain.proceed(request);
            }
        });

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor mLogging = new HttpLoggingInterceptor();
            mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(mLogging);

        }
        return httpClient;
    }

    public UserService getUserService() {
        return mRetrofit.create(UserService.class);
    }


}
