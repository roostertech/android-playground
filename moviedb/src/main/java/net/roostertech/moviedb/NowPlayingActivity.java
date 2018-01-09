package net.roostertech.moviedb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import net.roostertech.moviedb.adapters.MovieArrayAdapter;
import net.roostertech.moviedb.adapters.MovieRecyclerAdapter;
import net.roostertech.moviedb.adapters.RecyclerViewClickListener;
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
    RecyclerView lvMovies;
    MovieRecyclerAdapter movieArrayAdapter;
    ArrayList<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        lvMovies = findViewById(R.id.lvMovies);
        movieArrayAdapter = new MovieRecyclerAdapter(movies, this, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                LOG.debug("Clicked on item {}", position);
                Movie movie = movies.get(position);
                Intent movieDetailIntent = new Intent(NowPlayingActivity.this, MovieDetailActivity.class);
                movieDetailIntent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
                startActivity(movieDetailIntent);
            }
        });
        lvMovies.setAdapter(movieArrayAdapter);
        lvMovies.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();

        Observable<NowPlaying> nowPlayingFetch = MovieClient.getApi().getNowPlaying();
        nowPlayingFetch.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(nowPlaying -> {
                    LOG.debug("Loaded {} movies", nowPlaying.getMovies().size());
                    movies.addAll(nowPlaying.getMovies());
                    movieArrayAdapter.notifyDataSetChanged();
                }, error -> {
                    LOG.error("Error", error);
                });
    }
}
