package net.roostertech.moviedb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieClient {
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p";
    public static final String POSTER_W154_BASE_URL = POSTER_BASE_URL + "/w154";
    public static final String POSTER_W500_BASE_URL = POSTER_BASE_URL + "/w500";

    static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    private MovieDbApi movieDbApi;
    private Retrofit retrofit;
    static private MovieClient instance;

    private MovieClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        movieDbApi = retrofit.create(MovieDbApi.class);
    }

    public static MovieDbApi getApi() {
        if (instance == null) {
            instance = new MovieClient();
        }

        return instance.movieDbApi;
    }
}
