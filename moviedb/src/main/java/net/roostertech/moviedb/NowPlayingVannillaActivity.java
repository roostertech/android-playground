package net.roostertech.moviedb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.widget.ListView;

import net.roostertech.moviedb.adapters.MovieArrayAdapter;
import net.roostertech.moviedb.model.Movie;
import net.roostertech.moviedb.persistence.MovieStorage;
import net.roostertech.moviedb.readers.MovieJsonReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Use ListView and built in Android utilities
 */
public class NowPlayingVannillaActivity extends AppCompatActivity {
    Logger LOG = LoggerFactory.getLogger(NowPlayingActivity.class);
    ArrayList<Movie> movies = new ArrayList<>();
    MovieArrayAdapter movieArrayAdapter;

    ListView lvMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing_lv);
        lvMovies = findViewById(R.id.lvMovies);
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvMovies.setAdapter(movieArrayAdapter);

    }

    List<Movie> readResults(InputStream inputStream) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        List<Movie> movies = new ArrayList<>();
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("results")) {
                movies = MovieJsonReader.readMovies(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return movies;
    }

    static AsyncTask<Void, Void, List<Movie>> fetchTask;

    @Override
    protected void onStart() {
        super.onStart();
        List<Movie> movies = MovieStorage.getInstance().findAll();
        if (movies.size() > 0) {
            LOG.debug("Restored {} movies", movies.size());
            this.movies.addAll(movies);
            movieArrayAdapter.notifyDataSetChanged();
        } else {
            LOG.debug("No persisted data");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // This does not handle the case where activity is gone when task is completed
        fetchTask = new AsyncTask<Void, Void, List<Movie>>() {
            @Override
            protected List<Movie> doInBackground(Void... voids) {
                try {
                    URL nowPlayingUrl = new URL(MovieClient.BASE_URL + "now_playing" + "?api_key=" + MovieClient.API_KEY);
                    HttpURLConnection urlConnection = (HttpURLConnection) nowPlayingUrl.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    List<Movie> movies = readResults(in);
                    MovieStorage.getInstance().store(movies);
                    return movies;
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                NowPlayingVannillaActivity.this.movies.clear();
                NowPlayingVannillaActivity.this.movies.addAll(movies);
                NowPlayingVannillaActivity.this.movieArrayAdapter.notifyDataSetChanged();
            }
        };

        fetchTask.execute(null, null);
    }
}
