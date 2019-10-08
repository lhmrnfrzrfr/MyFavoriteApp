package com.ilham.myfavoriteapp.activity.item;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.ilham.myfavoriteapp.activity.database.DatabaseContract;

public class Movie implements Parcelable {

    private int id;
    private String poster;
    private double score;
    private String title;
    private String overview;
    private String backdrop;
    private String released;

    public String getReleased() {
        return released;
    }

    public String getPoster() {
        return poster;
    }

    public double getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getBackdrop() {
        return backdrop;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.poster);
        dest.writeDouble(this.score);
        dest.writeString(this.title);
        dest.writeString(this.overview);
        dest.writeString(this.backdrop);
        dest.writeString(this.released);
    }

    public Movie() {
    }

    public Movie(int id, String poster, double score, String title, String overview, String backdrop, String released) {
        this.id = id;
        this.poster = poster;
        this.score = score;
        this.title = title;
        this.overview = overview;
        this.backdrop = backdrop;
        this.released = released;
    }

    public Movie(Cursor cursor) {
        this.id = DatabaseContract.getColumnInt(cursor, DatabaseContract.MovieColumns._ID);
        this.poster = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.POSTER);
        this.score = DatabaseContract.getColumnDouble(cursor, DatabaseContract.MovieColumns.SCORE);
        this.title = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.TITLE);
        this.overview = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.OVERVIEW);
        this.backdrop = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.BACKDROP);
        this.released = DatabaseContract.getColumnString(cursor, DatabaseContract.MovieColumns.RELEASED);
    }

    protected Movie(Parcel in) {
        this.id = in.readInt();
        this.poster = in.readString();
        this.score = in.readDouble();
        this.title = in.readString();
        this.overview = in.readString();
        this.backdrop = in.readString();
        this.released = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
