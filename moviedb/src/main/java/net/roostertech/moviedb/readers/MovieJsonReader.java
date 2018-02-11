package net.roostertech.moviedb.readers;

import android.util.JsonReader;

import net.roostertech.moviedb.model.Movie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieJsonReader {
    static Logger LOG = LoggerFactory.getLogger(MovieJsonReader.class);


    public static Movie readMovie(JsonReader reader) throws IOException {
        reader.beginObject();
        Movie movie = new Movie();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("overview")) {
                movie.setOverview(reader.nextString());
            } else if (name.equals("original_title")) {
                movie.setOriginalTitle(reader.nextString());
            } else if (name.equals("title")) {
                movie.setTitle(reader.nextString());
            } else if (name.equals("backdrop_path")) {
                movie.setBackdropPath(reader.nextString());
            } else if (name.equals("poster_path")) {
                movie.setPosterPath(reader.nextString());
            } else {
                reader.skipValue();
            }
            LOG.debug(name);
        }
        reader.endObject();
        return movie;
    }

    public static List<Movie> readMovies(JsonReader reader) throws IOException {
        List<Movie> movies = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            Movie movie = readMovie(reader);
            movies.add(movie);
        }
        reader.endArray();
        return movies;
    }
}
