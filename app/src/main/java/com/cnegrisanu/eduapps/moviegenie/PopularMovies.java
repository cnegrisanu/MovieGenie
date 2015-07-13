package com.cnegrisanu.eduapps.moviegenie;

/**
 * Created by Cristian on 7/12/2015.
 */
public class PopularMovies {
    String title;
    String summary;
    String poster_path;
    String release_date;
    String vote_average;

    public PopularMovies(String title, String summary, String poster_path, String release_date, String vote_average) {
        this.title = title;
        this.summary = summary;
        this.poster_path = "http://image.tmdb.org/t/p/w342/" + poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
    }
}
