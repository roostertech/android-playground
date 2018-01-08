package net.roostertech.moviedb.adapters;

import android.content.Context;
import android.content.res.Configuration;
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
    private static class ViewHolder {
        ImageView mvImage;
        TextView mvTitle;
        TextView mvDesc;
    }

    public MovieArrayAdapter(@NonNull Context context, ArrayList<Movie> movies) {
        super(context, android.R.layout.simple_expandable_list_item_1, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder.mvImage = convertView.findViewById(R.id.mvImage);
            viewHolder.mvTitle = convertView.findViewById(R.id.mvTitle);
            viewHolder.mvDesc = convertView.findViewById(R.id.mvDesc);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mvTitle.setText(movie.getTitle());
        viewHolder.mvDesc.setText(movie.getOverview());

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(getContext()).load(MovieClient.POSTER_W154_BASE_URL + movie.getPosterPath()).into(viewHolder.mvImage);
        } else {
            Glide.with(getContext()).load(MovieClient.POSTER_W154_BASE_URL + movie.getBackdropPath()).into(viewHolder.mvImage);
        }
        return convertView;
    }
}
