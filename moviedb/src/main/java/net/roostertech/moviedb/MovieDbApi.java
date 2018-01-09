package net.roostertech.moviedb;

import net.roostertech.moviedb.model.MovieTrailers;
import net.roostertech.moviedb.model.NowPlaying;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface MovieDbApi {
    @GET("now_playing")
    Observable<NowPlaying> getNowPlaying();

    @GET("{movieid}/videos")
    Observable<MovieTrailers> getMovieTrailers(@Path("movieid") int movieId);
}
