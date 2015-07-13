package com.cnegrisanu.eduapps.moviegenie;

/**
 * Created by vollulin on 7/12/2015.
 */
public class PopularMovies {
    String title;
    String summary;
    String poster_path;

    public PopularMovies(String title, String summary, String poster_path) {
        this.title = title;
        this.summary = summary;
        this.poster_path = "http://image.tmdb.org/t/p/w342/" + poster_path;
    }
}
