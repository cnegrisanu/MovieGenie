package com.cnegrisanu.eduapps.moviegenie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


//    PopularMovies currentMovie = new PopularMovies()
    String id = "";
    PopularMovies movie;
    protected MovieExtrasAdapter mMovieExtrasAdapter;
    private ArrayList<MovieExtras> mMovieExtrasList;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final ToggleButton favoriteToggle = (ToggleButton) rootView.findViewById(R.id.favoriteToggle);
        final ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(),"favorites", Context.MODE_PRIVATE);

        ListView movieExtrasView = (ListView)rootView.findViewById(R.id.movie_extras_view);

        // The detail Activity called via intent.  Inspect the intent for forecast data.
        Intent intent = getActivity().getIntent();

        if (intent != null) {
            if (intent.hasExtra("movieDetailsBundle")) {
                Bundle mBundle = intent.getBundleExtra("movieDetailsBundle");
                movie = mBundle.getParcelable("movieDetails");

                if (movie != null) {
                    ((TextView) rootView.findViewById(R.id.detailsTitleText))
                            .setText(movie.title);
                    ((TextView) rootView.findViewById(R.id.detailsSummary))
                            .setText(movie.summary);
                    ((TextView) rootView.findViewById(R.id.detailsRating))
                            .setText(movie.vote_average);
                    ((TextView) rootView.findViewById(R.id.detailsReleaseDate))
                            .setText(movie.release_date);


                    ImageView poster = (ImageView) rootView.findViewById(R.id.detailsPosterView);
                    Picasso.with(getActivity()).load(movie.poster_path)
                            .placeholder(R.drawable.ic_photo_black_24dp)
                            .error(R.drawable.ic_broken_image_black_24dp)
                            .into(poster);
                }
                if(complexPreferences.getObject(movie.id,PopularMovies.class) != null) {
                    favoriteToggle.setChecked(true);
                } else {
                    favoriteToggle.setChecked(false);
                }
            }

//            if(intent.hasExtra("TITLE")) {
//                String title = intent.getStringExtra("TITLE");
//                ((TextView) rootView.findViewById(R.id.detailsTitleText))
//                        .setText(title);
//            }
//            if(intent.hasExtra("SUMMARY")) {
//                String summary = intent.getStringExtra("SUMMARY");
//                ((TextView) rootView.findViewById(R.id.detailsSummary))
//                        .setText(summary);
//            }
//            if(intent.hasExtra("VOTE_AVERAGE")) {
//                String vote_average = intent.getStringExtra("VOTE_AVERAGE");
//                ((TextView) rootView.findViewById(R.id.detailsRating))
//                        .setText(vote_average);
//            }
//            if(intent.hasExtra("RELEASE_DATE")) {
//                String release_date = intent.getStringExtra("RELEASE_DATE");
//                ((TextView) rootView.findViewById(R.id.detailsReleaseDate))
//                        .setText(release_date);
//            }
//            if(intent.hasExtra("ID")) {
//                id = intent.getStringExtra("ID");
//                Boolean favorite = prefs.contains(id);
//                favoriteToggle.setChecked(favorite);
//
//            }
//            if(intent.hasExtra("POSTER_PATH")) {
//                String path = intent.getStringExtra("POSTER_PATH");
//                ImageView poster = (ImageView) rootView.findViewById(R.id.detailsPosterView);
//                Picasso.with(getActivity()).load(path)
//                        .placeholder(R.drawable.ic_photo_black_24dp)
//                        .error(R.drawable.ic_broken_image_black_24dp)
//                        .into(poster);
//            }

            FetchMovieExtrasTask extrasTask = new FetchMovieExtrasTask(this);
            extrasTask.execute(movie.id);

            mMovieExtrasList = new ArrayList<MovieExtras>();
            mMovieExtrasAdapter = new MovieExtrasAdapter(getActivity(),mMovieExtrasList);

            mMovieExtrasAdapter.addAll(mMovieExtrasList);

        }
        movieExtrasView.setAdapter(mMovieExtrasAdapter);

        movieExtrasView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieExtras movieData = mMovieExtrasAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/v/"
//                        Uri.parse("vnd.youtube:"
                                + movieData.key));

                startActivity(intent);
            }
        });

        final SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

        favoriteToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
//                    Toast.makeText(getActivity().getApplicationContext(),"Button checked!",Toast.LENGTH_SHORT).show();
                    favoriteToggle.setChecked(true);
                    complexPreferences.putObject(movie.id, movie);
//                    prefsEditor.putBoolean(id,true);
                } else {
                    // The toggle is disabled
//                    Toast.makeText(getActivity(),"Button un-checked!",Toast.LENGTH_SHORT).show();
                    favoriteToggle.setChecked(false);
                    complexPreferences.remove(movie.id);
//                    prefsEditor.remove(id);
                }
                complexPreferences.commit();
//                prefsEditor.commit();
            }
        });

        return rootView;
    }
}
