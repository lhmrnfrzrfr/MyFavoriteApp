package com.ilham.myfavoriteapp.activity;

import android.database.Cursor;

import com.ilham.myfavoriteapp.activity.item.Movie;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.BACKDROP;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.OVERVIEW;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.POSTER;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.RELEASED;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.SCORE;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.TITLE;


public class MappingHelper {
    public static ArrayList<Movie> mapCursorToArrayList(Cursor moviesCursor) {

        ArrayList<Movie> moviesList = new ArrayList<>();

        while (moviesCursor.moveToNext()) {
            int id = moviesCursor.getInt(moviesCursor.getColumnIndexOrThrow(_ID));
            String poster = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(POSTER));
            double score = moviesCursor.getDouble(moviesCursor.getColumnIndexOrThrow(SCORE));
            String title = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(TITLE));
            String overview = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(OVERVIEW));
            String backdrop = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(BACKDROP));
            String released = moviesCursor.getString(moviesCursor.getColumnIndexOrThrow(RELEASED));
            moviesList.add(new Movie(id, poster, score, title, overview, backdrop, released));
        }

        return moviesList;
    }
}
