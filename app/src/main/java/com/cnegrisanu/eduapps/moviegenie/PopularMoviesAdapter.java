package com.cnegrisanu.eduapps.moviegenie;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.imageview_item, parent, false);
            holder = new ViewHolder();
            holder.poster = (ImageView) convertView.findViewById(R.id.poster_imageView);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        PopularMovies popularMovies = getItem(position);

        Picasso.with(getContext())
                .load(popularMovies.poster_path)
                .noFade()
//                .resize(250,250)
                .placeholder(R.drawable.ic_photo_black_24dp)
                .error(R.drawable.ic_broken_image_black_24dp)
                .into(holder.poster);

        return convertView;
    }

    static class ViewHolder {
        ImageView poster;
    }
}
