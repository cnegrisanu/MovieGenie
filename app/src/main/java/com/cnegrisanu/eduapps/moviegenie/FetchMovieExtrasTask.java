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
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by vollulin on 10/4/2015.
 */
public class FetchMovieExtrasTask extends AsyncTask<String,Void,ArrayList<MovieExtras>> {

    private DetailActivityFragment movieDetailsFragment;
    private final String LOG_TAG = FetchMovieExtrasTask.class.getSimpleName();
    private final String API_KEY = "554026709f5fd02040f0d9060089835b"; //removed per instructions in the guide
    private final String MOVIE_EXTRAS = "append_to_response";
    private final String REVIEWS_AND_TRAILERS = "trailers,reviews";

    public FetchMovieExtrasTask(DetailActivityFragment movieDetailsFragment) {
        this.movieDetailsFragment = movieDetailsFragment;
    }

    /**
     * <p>Runs on the UI thread after {@link #doInBackground}. The
     * specified result is the value returned by {@link #doInBackground}.</p>
     * <p/>
     * <p>This method won't be invoked if the task was cancelled.</p>
     *
     * @param movieExtras The result of the operation computed by {@link #doInBackground}.
     * @see #onPreExecute
     * @see #doInBackground
     * @see #onCancelled(Object)
     */
    @Override
    protected void onPostExecute(ArrayList<MovieExtras> movieExtras) {
        if (movieExtras != null) {
            movieDetailsFragment.mMovieExtrasAdapter.clear();
            for(MovieExtras aMovieExtras : movieExtras) {
                movieDetailsFragment.mMovieExtrasAdapter.add(aMovieExtras);
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
    protected ArrayList<MovieExtras> doInBackground(String... params) {

        // If there's no parameter, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieExtrasJsonStr = null;


        try {
            // Construct the URL for the theMovieDb.org query
            // Possible parameters are available at TMDB's API page, at
            // https://www.themoviedb.org/documentation/api/discover
            final String TMDB_BASE_URL =
                    "https://api.themoviedb.org/3/movie?";
            final String KEY = "api_key";

            Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                    .appendPath(params[0])
                    .appendQueryParameter(KEY, API_KEY)
                    .appendQueryParameter(MOVIE_EXTRAS, REVIEWS_AND_TRAILERS)
                    .build();

            URL url = new URL(builtUri.toString());

//                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to TMDB, and open the connection
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
            movieExtrasJsonStr = buffer.toString();

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
            return getMovieExtrasFromJson(movieExtrasJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        // This will only happen if there was an error getting or parsing the movie info.
        return null;
    }


    private ArrayList<MovieExtras> getMovieExtrasFromJson(String moviesExtrasJsonStr) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMDB_MOVIE_ID = "id";
        final String TMDB_RESULTS = "results";
        final String TMDB_MOVIE_TRAILERS = "trailers";
        final String TMDB_MOVIE_YOUTUBE_TRAILERS = "youtube";
        final String TMDB_MOVIE_REVIEWS = "reviews";
        final String TMDB_VIDEO_NAME = "name";
        final String TMDB_VIDEO_SOURCE = "source";
        final String TMDB_REVIEW_AUTHOR = "author";
        final String TMDB_REVIEW_CONTENT = "content";


//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(movieGridFragment.getActivity());


        JSONObject moviesExtrasJson = new JSONObject(moviesExtrasJsonStr);
        JSONArray movieTrailersArray = moviesExtrasJson.getJSONObject(TMDB_MOVIE_TRAILERS).getJSONArray(TMDB_MOVIE_YOUTUBE_TRAILERS);
        JSONArray movieReviewsArray = moviesExtrasJson.getJSONObject(TMDB_MOVIE_REVIEWS).getJSONArray(TMDB_RESULTS);


//        MovieExtras[] moviesDataArray = new MovieExtras[movieTrailersArray.length() + movieReviewsArray.length()];
        ArrayList<MovieExtras> mDA = new ArrayList<MovieExtras>();

        for (int i = 0; i < movieTrailersArray.length(); i++) {

            String name;
            String source;

            // Get the JSON object representing the movie
            JSONObject movieTrailers = movieTrailersArray.getJSONObject(i);

            name = movieTrailers.getString(TMDB_VIDEO_NAME);
            source = movieTrailers.getString(TMDB_VIDEO_SOURCE);


//            moviesDataArray[i] = new MovieExtras("trailer",source,name);
            mDA.add(new MovieExtras("trailer",source,name));
        }
        for (int i = 0; i < movieReviewsArray.length(); i++) {

            String author;
            String content;

            // Get the JSON object representing the movie
            JSONObject movieReviews = movieReviewsArray.getJSONObject(i);

            author = movieReviews.getString(TMDB_REVIEW_AUTHOR);
            content = movieReviews.getString(TMDB_REVIEW_CONTENT);


            mDA.add(new MovieExtras("review",author,content));
        }

//            for (PopularMovies m : moviesDataArray) {
//                Log.v(LOG_TAG, "Movie entry: " + s);
//            }
        return mDA;
    }
}