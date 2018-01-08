package net.roostertech.moviedb;

import net.roostertech.moviedb.model.NowPlaying;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MovieDbApi {
    @GET("now_playing")
    Observable<NowPlaying> getNowPlaying();
}
