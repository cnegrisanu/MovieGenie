package com.cnegrisanu.eduapps.moviegenie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Cristian on 7/12/2015.
 */
public class PopularMovies implements Parcelable{
    String id;
    String title;
    String summary;
    String poster_path;
    String release_date;
    String vote_average;
    Boolean favorite;

    public PopularMovies(String id, String title, String summary, String poster_path, String release_date, String vote_average, Boolean favorite) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.poster_path = "http://image.tmdb.org/t/p/w342/" + poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.favorite = favorite;
    }

    protected PopularMovies(Parcel in) {
        id = in.readString();
        title = in.readString();
        summary = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
        favorite = (in.readInt() == 0) ? false : true;
    }

    public static final Creator<PopularMovies> CREATOR = new Creator<PopularMovies>() {
        @Override
        public PopularMovies createFromParcel(Parcel in) {
            return new PopularMovies(in);
        }

        @Override
        public PopularMovies[] newArray(int size) {
            return new PopularMovies[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable's
     * marshalled representation.
     *
     * @return a bitmask indicating the set of special object types marshalled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(summary);
        dest.writeString(poster_path);
        dest.writeString(release_date);
        dest.writeString(vote_average);
        dest.writeInt(favorite ? 1 : 0);
    }
}
