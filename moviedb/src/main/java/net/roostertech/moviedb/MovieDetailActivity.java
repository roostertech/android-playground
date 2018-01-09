package net.roostertech.moviedb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import net.roostertech.moviedb.model.Movie;
import net.roostertech.moviedb.model.MovieTrailers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {
    private static Logger LOG = LoggerFactory.getLogger(MovieDetailActivity.class);

    public static final String EXTRA_MOVIE = "extra_movie";
    private Movie movie;


    @BindView(R.id.ivMoviePoster)
    ImageView moviePoster;
    @BindView(R.id.tvMovieTitle)
    TextView movieTitle;
    @BindView(R.id.tvMovieDesc)
    TextView movieDesc;
    @BindView(R.id.movieRatingBar)
    RatingBar ratingBar;
//    @BindView(R.id.youtube_player)
    YouTubePlayerFragment youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        youTubePlayer = (YouTubePlayerFragment)
                getFragmentManager().findFragmentById(R.id.youtube_player);

        movie = (Movie) getIntent().getExtras().get(EXTRA_MOVIE);
    }


    @Override
    protected void onResume() {
        super.onResume();

        movieTitle.setText(movie.getTitle());
        movieDesc.setText(movie.getOverview());
        Glide.with(this).load(MovieClient.POSTER_W500_BASE_URL + movie.getPosterPath()).into(moviePoster);

        ratingBar.setMax(5);
        ratingBar.setRating((float) (movie.getVoteAverage() / 2));

        Observable<MovieTrailers> trailersFetch = MovieClient.getApi().getMovieTrailers(movie.getId());
        trailersFetch.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieTrailers -> {
                    if (movieTrailers.getResults().size() == 0) {
                        return;
                    }
                    youTubePlayer.initialize("AIzaSyCVf8no2wPKnXHdpE-Jy3GuVpW_aAekJ7Y", new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                            youTubePlayer.cueVideo(movieTrailers.getResults().get(0).getKey());
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        }
                    });
                }, error -> {
                    LOG.debug("Could not fetch trailer", error);
                });
    }
}
