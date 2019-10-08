package com.ilham.myfavoriteapp.activity;

import android.database.Cursor;

public interface LoadMoviesCallback {
    void postExecute(Cursor movies);
}
