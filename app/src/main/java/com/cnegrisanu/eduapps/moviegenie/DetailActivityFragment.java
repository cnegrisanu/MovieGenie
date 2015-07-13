package com.cnegrisanu.eduapps.moviegenie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if(intent.hasExtra("TITLE")) {
                String title = intent.getStringExtra("TITLE");
                ((TextView) rootView.findViewById(R.id.detailsTitleText))
                        .setText(title);
            }
            if(intent.hasExtra("SUMMARY")) {
                String summary = intent.getStringExtra("SUMMARY");
                ((TextView) rootView.findViewById(R.id.detailsSummary))
                        .setText(summary);
            }
            if(intent.hasExtra("POSTER_PATH")) {
                String path = intent.getStringExtra("POSTER_PATH");
                ImageView poster = (ImageView) rootView.findViewById(R.id.detailsPosterView);
                Picasso.with(getActivity()).load(path)
                        .placeholder(R.drawable.ic_photo_black_24dp)
                        .error(R.drawable.ic_broken_image_black_24dp)
                        .into(poster);
            }
        }

        return rootView;
    }
}
