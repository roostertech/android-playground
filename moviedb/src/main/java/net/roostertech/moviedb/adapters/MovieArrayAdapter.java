package net.roostertech.moviedb.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.roostertech.moviedb.MovieClient;
import net.roostertech.moviedb.R;
import net.roostertech.moviedb.model.Movie;

import java.util.ArrayList;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(@NonNull Context context, ArrayList<Movie> movies) {
        super(context, android.R.layout.simple_expandable_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
        }

        ImageView mvImage = convertView.findViewById(R.id.mvImage);
        TextView mvTitle = convertView.findViewById(R.id.mvTitle);
        TextView mvDesc = convertView.findViewById(R.id.mvDesc);

        mvTitle.setText(movie.getTitle());
        mvDesc.setText(movie.getOverview());

        Glide.with(getContext()).load(MovieClient.POSTER_W185_BASE_URL + movie.getPosterPath()).into(mvImage);

        return convertView;
    }
}
