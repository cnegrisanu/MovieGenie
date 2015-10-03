package com.cnegrisanu.eduapps.moviegenie;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment {


    private static final String KEY_MOVIES_LIST = "movies";
    private static String sSortOrder = "";
    private final String LOG_TAG = MovieGridFragment.class.getSimpleName();

    protected PopularMoviesAdapter mMovieAdapter;
    private ArrayList<PopularMovies> mMoviesList;

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG,"onActivityCreated() Called");

    }

    /**
     * Called when a fragment is first attached to its activity.
     * {@link #onCreate(Bundle)} will be called after this.
     *
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     * Called to do initial creation of a fragment.  This is called after
     * {@link #onAttach(Activity)} and before
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * <p>
     * <p>Note that this can be called while the fragment's activity is
     * still in the process of being created.  As such, you can not rely
     * on things like the activity's content view hierarchy being initialized
     * at this point.  If you want to do work once the activity itself is
     * created, see {@link #onActivityCreated(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.v(LOG_TAG, "onCreate() Called");
    }

    public MovieGridFragment() {
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_MOVIES_LIST, mMoviesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(LOG_TAG, "onResume() Called");

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = pref.getString(getString(R.string.preference_sort_order_key),"");
//        Log.v(LOG_TAG, "sortOrder Before: " + sortOrder);
//        Log.v(LOG_TAG, "sSortOrder Before: " + sSortOrder);
        if (mMovieAdapter.isEmpty() || !(sSortOrder.equalsIgnoreCase(sortOrder))) {
//            Log.v(LOG_TAG, "mMovieAdapter.isEmpty(): " + mMovieAdapter.isEmpty());
//            Log.v(LOG_TAG, "sSortOrder.equalsIgnoreCase(sortOrder): " + sSortOrder.equalsIgnoreCase(sortOrder));
            updateMovies();
            sSortOrder = sortOrder;
//            Log.v(LOG_TAG, "MOVIES LIST UPDATED!!!");
//
//            Log.v(LOG_TAG, "sSortOrder: " + sSortOrder);
//            Log.v(LOG_TAG, "sortOrder: " + sortOrder);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "onCreateView() Called");

        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIES_LIST)){
            mMoviesList = savedInstanceState.getParcelableArrayList(KEY_MOVIES_LIST);
            mMovieAdapter =
                    new PopularMoviesAdapter(
                            getActivity(), // The current context (this activity)
                            mMoviesList);
        } else {
            mMoviesList = new ArrayList<PopularMovies>();
            mMovieAdapter =
                    new PopularMoviesAdapter(
                            getActivity(), // The current context (this activity)
                            mMoviesList);
            updateMovies();
        }

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        final GridView listView = (GridView) rootView.findViewById(R.id.movies_gridView);
        listView.setAdapter(mMovieAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopularMovies movieData = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                Bundle movieDetail = new Bundle();
                movieDetail.putParcelable("movieDetails", movieData);

                intent.putExtra("movieDetailsBundle", movieDetail);


//                intent.putExtra("ID", movieData.id);
//                intent.putExtra("TITLE", movieData.title);
//                intent.putExtra("SUMMARY", movieData.summary);
//                intent.putExtra("POSTER_PATH", movieData.poster_path);
//                intent.putExtra("RELEASE_DATE", movieData.release_date);
//                intent.putExtra("VOTE_AVERAGE", movieData.vote_average);
//                intent.putExtra("FAVORITE",movieData.favorite);

                startActivity(intent);
            }
        });
        return rootView;
    }

    /**
     * Called when the Fragment is visible to the user.  This is generally
     * tied to {@link 'Activity #onStart() Activity.onStart} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.v(LOG_TAG, "onStart() Called");
    }

    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask(this);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = pref.getString(getString(R.string.preference_sort_order_key),getString(R.string.defaultSortValue));
        moviesTask.execute(sortOrder);
        Log.v(LOG_TAG,"updateMovies method called");
    }
}
