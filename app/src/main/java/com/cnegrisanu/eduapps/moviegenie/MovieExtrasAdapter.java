package com.cnegrisanu.eduapps.moviegenie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vollulin on 10/4/2015.
 */
public class MovieExtrasAdapter extends ArrayAdapter<MovieExtras> {
    private final String LOG_TAG = PopularMoviesAdapter.class.getSimpleName();

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param movieExtras            The objects to represent in the ListView.
     */
    public MovieExtrasAdapter(Context context, List<MovieExtras> movieExtras) {
        super(context, 0, movieExtras);
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
            convertView = inflater.inflate(R.layout.review_trailer_item, parent, false);
            holder = new ViewHolder();
            holder.key = (TextView) convertView.findViewById(R.id.review_key);
            holder.value = (TextView) convertView.findViewById(R.id.review_value);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        MovieExtras movieExtras = getItem(position);
        if(movieExtras != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            if(movieExtras.type.equalsIgnoreCase("review")) {
                holder.key.setText(movieExtras.key);
                holder.value.setText(movieExtras.value);
            } else {
//                holder.value.setText("");
                holder.key.setText(movieExtras.value);
            }

        }



        return convertView;
    }

    static class ViewHolder {
        TextView key;
        TextView value;
    }
}
