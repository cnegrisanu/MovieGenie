package com.cnegrisanu.eduapps.moviegenie;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {


    //    PopularMovies currentMovie = new PopularMovies()
    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    static final String MOVIE_DETAILS = "movieDetails";
    static final String MOVIE_DETAILS_DATA = "movieDetailsBundle";

    String id = "";
    private PopularMovies movie;
    MovieExtrasAdapter mMovieExtrasAdapter;
    private ArrayList<MovieExtras> mMovieExtrasList;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final ToggleButton favoriteToggle = (ToggleButton) rootView.findViewById(R.id.favoriteToggle);
        final ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(getActivity(), "favorites");

        ListView movieExtrasView = (ListView) rootView.findViewById(R.id.movie_extras_view);

        Bundle mBundle = new Bundle();
        if (this.getArguments() != null) {
            mBundle = this.getArguments();
        } else {

            // The detail Activity called via intent.  Inspect the intent for data.
            Intent intent = getActivity().getIntent();

            if (intent != null && intent.hasExtra(MOVIE_DETAILS_DATA)) {
                mBundle = intent.getBundleExtra(MOVIE_DETAILS_DATA);
            }
        }
        movie = mBundle.getParcelable(MOVIE_DETAILS);

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

            if (complexPreferences.getObject(movie.id, PopularMovies.class) != null) {
                favoriteToggle.setChecked(true);
            } else {
                favoriteToggle.setChecked(false);
            }


            FetchMovieExtrasTask extrasTask = new FetchMovieExtrasTask(this);
            extrasTask.execute(movie.id);
        }

        mMovieExtrasList = new ArrayList<>();
        mMovieExtrasAdapter = new MovieExtrasAdapter(getActivity(), mMovieExtrasList);

        mMovieExtrasAdapter.addAll(mMovieExtrasList);


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

//        final SharedPreferences.Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();

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