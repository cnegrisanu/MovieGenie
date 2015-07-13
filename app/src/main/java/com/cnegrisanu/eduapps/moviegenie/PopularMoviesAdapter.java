package com.cnegrisanu.eduapps.moviegenie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Cristian on 7/12/2015.
 */
public class PopularMoviesAdapter extends ArrayAdapter<PopularMovies> {

    private final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param popularMovies  The objects to represent in the ListView.
     */
    public PopularMoviesAdapter(Activity context, List<PopularMovies> popularMovies) {
        super(context, 0, popularMovies);
    }

    /**
     * {@inheritDoc}
     *
     * @param position
     * @param convertView
     * @param parent
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PopularMovies popularMovies = getItem(position);

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.imageview_item, parent, false);

        ImageView posterView = (ImageView) rootView.findViewById(R.id.poster_imageView);

        Picasso.with(getContext())
                .load(popularMovies.poster_path)
                .noFade()
//                .resize(250,250)

                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(posterView);

        return rootView;
//        return super.getView(position, convertView, parent);
    }
}
