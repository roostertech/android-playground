package net.roostertech.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import net.roostertech.moviedb.adapters.MovieArrayAdapter;
import net.roostertech.moviedb.model.Movie;
import net.roostertech.moviedb.model.NowPlaying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class NowPlayingActivity extends AppCompatActivity {
    Logger LOG = LoggerFactory.getLogger(NowPlayingActivity.class);
    ListView lvMovies;
    MovieArrayAdapter movieArrayAdapter;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        lvMovies = findViewById(R.id.lvMovies);
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvMovies.setAdapter(movieArrayAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Observable<NowPlaying> nowPlayingFetch = MovieClient.getApi().getNowPlaying();
        nowPlayingFetch.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nowPlaying -> {
                    LOG.debug("Movie {}", nowPlaying);
                    movies.addAll(nowPlaying.getMovies());
                    movieArrayAdapter.notifyDataSetChanged();
                }, error -> {
                    LOG.error("Error", error);
                });
    }
}
