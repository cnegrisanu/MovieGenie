package com.cnegrisanu.eduapps.moviegenie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    String id = "";

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final ToggleButton favoriteToggle = (ToggleButton) rootView.findViewById(R.id.favoriteToggle);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());


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
            if(intent.hasExtra("VOTE_AVERAGE")) {
                String vote_average = intent.getStringExtra("VOTE_AVERAGE");
                ((TextView) rootView.findViewById(R.id.detailsRating))
                        .setText(vote_average);
            }
            if(intent.hasExtra("RELEASE_DATE")) {
                String release_date = intent.getStringExtra("RELEASE_DATE");
                ((TextView) rootView.findViewById(R.id.detailsReleaseDate))
                        .setText(release_date);
            }
            if(intent.hasExtra("ID")) {
                id = intent.getStringExtra("ID");
                Boolean favorite = prefs.getBoolean(id,false);
                favoriteToggle.setChecked(favorite);

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

        final SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
//                    Toast.makeText(getActivity().getApplicationContext(),"Button checked!",Toast.LENGTH_SHORT).show();
                    favoriteToggle.setChecked(true);
                    prefsEditor.putBoolean(id,true);
                } else {
                    // The toggle is disabled
//                    Toast.makeText(getActivity(),"Button un-checked!",Toast.LENGTH_SHORT).show();
                    favoriteToggle.setChecked(false);
                    prefsEditor.remove(id);
                }
                prefsEditor.commit();
            }
        });

        return rootView;
    }
}
