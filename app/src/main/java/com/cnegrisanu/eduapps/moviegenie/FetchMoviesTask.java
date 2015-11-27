package com.cnegrisanu.eduapps.moviegenie;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vollulin on 9/27/2015.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, PopularMovies[]> {

    private MovieGridFragment movieGridFragment;
    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private final String API_KEY = "554026709f5fd02040f0d9060089835b"; //removed per instructions in the guide

    public FetchMoviesTask(MovieGridFragment movieGridFragment) {
        this.movieGridFragment = movieGridFragment;
    }


    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param moviesData The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(PopularMovies[] moviesData) {
        if (moviesData != null) {
            movieGridFragment.mMovieAdapter.clear();
            for (PopularMovies aMoviesData : moviesData) {
                movieGridFragment.mMovieAdapter.add(aMoviesData);
            }
        }
    }

    /**
     * Override this method to perform a computation on a background thread. The
     * specified parameters are the parameters passed to {@link #execute}
     * by the caller of this task.
     * <p/>
     * This method can call {@link #publishProgress} to publish updates
     * on the UI thread.
     *
     * @param params The parameters of the task.
     * @return A result, defined by the subclass of this task.
     * @see #onPreExecute()
     * @see #onPostExecute
     * @see #publishProgress
     */
    @Override
    protected PopularMovies[] doInBackground(String... params) {

        // If there's no parameter, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;


        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are available at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String TMDB_BASE_URL =
                    "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String COUNTRY = "certification_country";
            final String KEY = "api_key";

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(COUNTRY, "US")
                    .appendQueryParameter(KEY, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

//                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();

//                Log.v(LOG_TAG, "Movie string: " + movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the movie data, there's no point in attempting
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the movie info.
        return null;
    }

    private PopularMovies[] getMovieDataFromJson(String moviesJsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_RESULTS = "results";
        final String TMDB_ORIGINAL_TITLE = "original_title";
        final String TMDB_SUMMARY = "overview";
        final String TMDB_POSTER_PATH = "poster_path";
        final String TMDB_RELEASE_DATE = "release_date";
        final String TMDB_VOTE_AVERAGE = "vote_average";
        final String TMDB_FAVORITE = "favorite";

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(movieGridFragment.getActivity());


        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);


        PopularMovies[] moviesDataArray = new PopularMovies[moviesArray.length()];
        for (int i = 0; i < moviesArray.length(); i++) {

            String id;
            String title;
            String summary;
            String poster_path;
            String release_date;
            String vote_average;
            Boolean favorite;

            // Get the JSON object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);

            id = movie.getString(TMDB_MOVIE_ID);
            title = movie.getString(TMDB_ORIGINAL_TITLE);
            summary = movie.getString(TMDB_SUMMARY) == null ? "" : movie.getString(TMDB_SUMMARY);
            poster_path = movie.getString(TMDB_POSTER_PATH);
            release_date = movie.getString(TMDB_RELEASE_DATE);
            vote_average = movie.getString(TMDB_VOTE_AVERAGE);
            favorite = prefs.getBoolean(id,false);

            moviesDataArray[i] = new PopularMovies(id,title, summary, poster_path, release_date, vote_average,favorite);
        }

//            for (PopularMovies m : moviesDataArray) {
//                Log.v(LOG_TAG, "Movie entry: " + s);
//            }
        return moviesDataArray;
    }
}