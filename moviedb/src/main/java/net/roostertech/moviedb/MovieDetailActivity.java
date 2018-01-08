package net.roostertech.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.roostertech.moviedb.model.Movie;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    private Movie movie;


    @BindView(R.id.ivMoviePoster)  ImageView moviePoster;
    @BindView(R.id.tvMovieTitle)  TextView movieTitle;
    @BindView(R.id.tvMovieDesc)  TextView movieDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        movie = (Movie) getIntent().getExtras().get(EXTRA_MOVIE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        movieTitle.setText(movie.getTitle());
        movieDesc.setText(movie.getOverview());
        Glide.with(this).load(MovieClient.POSTER_W500_BASE_URL + movie.getPosterPath()).into(moviePoster);

    }
}
