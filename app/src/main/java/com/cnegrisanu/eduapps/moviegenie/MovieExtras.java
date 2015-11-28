package com.cnegrisanu.eduapps.moviegenie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by vollulin on 10/4/2015.
 * Movie Genie App for the Udacity Android Nanodegree Course
 */
public class MovieExtras implements Parcelable {

    String type;
    String key;
    String value;

    public MovieExtras(String type, String key,String value) {
        this.type = type;
        this.key = key;
        this.value = value;
    }

    private MovieExtras(Parcel in) {
        type = in.readString();
        key = in.readString();

    }

    public static final Creator<MovieExtras> CREATOR = new Creator<MovieExtras>() {
        @Override
        public MovieExtras createFromParcel(Parcel in) {
            return new MovieExtras(in);
        }

        @Override
        public MovieExtras[] newArray(int size) {
            return new MovieExtras[size];
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
        dest.writeString(type);
        dest.writeString(key);
        dest.writeString(value);

    }
}
