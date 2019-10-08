package com.ilham.myfavoriteapp.activity.activity;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ilham.myfavoriteapp.R;
import com.ilham.myfavoriteapp.activity.LoadMoviesCallback;
import com.ilham.myfavoriteapp.activity.adapter.FavoriteAdapter;
import com.ilham.myfavoriteapp.activity.item.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;

import static com.ilham.myfavoriteapp.activity.MappingHelper.mapCursorToArrayList;
import static com.ilham.myfavoriteapp.activity.database.DatabaseContract.MovieColumns.CONTENT_URI;


public class MainActivity extends AppCompatActivity implements LoadMoviesCallback {

    private long backPressedTime;
    private Toast backToast;

    private FavoriteAdapter favAdapter;
    private DataObserver myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbarMovie = findViewById(R.id.toolbar_movie);

        RecyclerView rvMovies = findViewById(R.id.rv_movie_favorite);

        favAdapter = new FavoriteAdapter(this);

        // Layout Manager
        rvMovies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Divider between item list
        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        itemDecorator.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.divider)));
        rvMovies.addItemDecoration(itemDecorator);
        rvMovies.setHasFixedSize(true);

        rvMovies.setAdapter(favAdapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);
        new getData(this, this).execute();

        // Intent to Detail Activity
        favAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movieItems) {
                // Define and Start Intent
                Intent moveWithObjectIntent = new Intent(MainActivity.this, DetailActivity.class);
                moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MOVIE, movieItems);
                MainActivity.this.startActivity(moveWithObjectIntent);

                // Intent Transition Animation
                MainActivity.this.overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            return;
        } else {
            backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void postExecute(Cursor movies) {
        ArrayList<Movie> listMovies = mapCursorToArrayList(movies);
        if (listMovies.size() > 0) {
            favAdapter.setListMovies(listMovies);
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
            favAdapter.setListMovies(new ArrayList<Movie>());
        }
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {

        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private getData(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return weakContext.get().getContentResolver().query(CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            super.onPostExecute(data);
            weakCallback.get().postExecute(data);
        }
    }

    static class DataObserver extends ContentObserver {

        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }
}
