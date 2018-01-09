package net.roostertech.moviedb.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.roostertech.moviedb.MovieClient;
import net.roostertech.moviedb.R;
import net.roostertech.moviedb.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieRecyclerAdapter.ViewHolder> {
    private List<Movie> movies = new ArrayList<>();
    private Context context;
    private RecyclerViewClickListener clickListener;

    public MovieRecyclerAdapter(List<Movie> movies, Context context, RecyclerViewClickListener clickListener) {
        this.movies = movies;
        this.context = context;
        this.clickListener = clickListener;
    }

    public void updateMovies(List<Movie> movies) {
        this.movies = movies;
        //diff
    }

    private Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        ViewHolder holder = new ViewHolder(movieView, clickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.mvTitle.setText(movie.getTitle());
        holder.mvDesc.setText(movie.getOverview());

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(getContext()).load(MovieClient.POSTER_W154_BASE_URL + movie.getPosterPath()).into(holder.mvImage);
        } else {
            Glide.with(getContext()).load(MovieClient.POSTER_W154_BASE_URL + movie.getBackdropPath()).into(holder.mvImage);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mvImage;
        private TextView mvTitle;
        private TextView mvDesc;

        private RecyclerViewClickListener clickListener;

        public ViewHolder(View itemView, RecyclerViewClickListener clickListener) {
            super(itemView);

            this.clickListener = clickListener;
            mvImage = itemView.findViewById(R.id.mvImage);
            mvTitle = itemView.findViewById(R.id.mvTitle);
            mvDesc = itemView.findViewById(R.id.mvDesc);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getAdapterPosition());
        }
    }
}