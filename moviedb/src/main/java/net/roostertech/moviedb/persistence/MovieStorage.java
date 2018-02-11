package net.roostertech.moviedb.persistence;

import android.database.sqlite.SQLiteConstraintException;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

import net.roostertech.moviedb.model.Movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Database(name = MovieStorage.NAME, version = MovieStorage.VERSION)
public class MovieStorage {
    static Logger LOG = LoggerFactory.getLogger(MovieStorage.class);

    public static final String NAME = "MovieDB";
    public static final int VERSION = 1;


    private static MovieStorage instance = new MovieStorage();

    ModelAdapter<Movie> movieModelAdapter;
    private MovieStorage() {
        movieModelAdapter = FlowManager.getModelAdapter(Movie.class);
    }

    public static MovieStorage getInstance() {
        return instance;
    }

    public void store(Movie movie) {
        try {
            if (movieModelAdapter.exists(movie)) {
                return;
            }
            movieModelAdapter.insert(movie);
        } catch (SQLiteConstraintException e) {
            LOG.warn("Could not store {}", movie.getId());
        }
    }

    public void store(List<Movie> movies) {
        for (Movie movie: movies) {
            store(movie);
        }
    }

    public List<Movie> findAll() {
        return SQLite.select().from(Movie.class).queryList();
    }
}
