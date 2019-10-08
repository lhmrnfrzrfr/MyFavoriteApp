package com.ilham.myfavoriteapp.activity.activity;

import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ilham.myfavoriteapp.R;
import com.ilham.myfavoriteapp.activity.item.Movie;

public class DetailActivity extends AppCompatActivity {

    /// Position Variable
    private int position;

    // Default Values
    public static final String EXTRA_MOVIE = "extra_movie";
    public static final String EXTRA_POSITION = "extra_position";

    // Instance Movie Items
    private Movie movie;

    // Widget Variables Declaration
    TextView txtTitleDetail;
    TextView txtOverviewDetail;
    TextView txtReleasedDetail;
    ImageView posterBanner;
    ImageButton btnBack;
    ProgressBar progressBar;
    RatingBar scoreDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Translucent Status Bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Casting Data Variables
        txtTitleDetail = findViewById(R.id.txt_title_detail);
        txtOverviewDetail = findViewById(R.id.txt_overviewDetail);
        txtReleasedDetail = findViewById(R.id.txt_released_detail);
        posterBanner = findViewById(R.id.poster_banner);
        scoreDetail = findViewById(R.id.score_detail_movie);

        // Casting Button Variables
        btnBack = findViewById(R.id.btn_back);

        // Progress Bar Declaration
        progressBar = findViewById(R.id.progressBar_detailMovie);
        progressBar.bringToFront();

        // Menerima Intent Movie dan Positon
        movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
        if (movie != null) {
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
        } else {
            movie = new Movie();
        }

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) movie = new Movie(cursor);
                cursor.close();
            }
        }

        // Mengisi data String
        txtTitleDetail.setText(movie.getTitle());
        txtOverviewDetail.setText(movie.getOverview());
        double score = movie.getScore() * 10;
        scoreDetail.setRating((float) ((score * 5) / 100));
        String released = (movie.getReleased()).substring(0, 4);
        txtReleasedDetail.setText(released);

        // Mengisi data image
        String url = "https://image.tmdb.org/t/p/original" + movie.getBackdrop();
        Glide.with(DetailActivity.this)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(posterBanner);

        // setOnClickListener untuk Button Back
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Animation onBackPressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailActivity.this.overridePendingTransition(R.anim.no_animation, R.anim.slide_down);
    }
}
