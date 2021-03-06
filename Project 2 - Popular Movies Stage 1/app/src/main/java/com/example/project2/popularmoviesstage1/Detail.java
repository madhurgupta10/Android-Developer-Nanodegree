package com.example.project2.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2.popularmoviesstage1.model.Movie;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String json = getIntent().getExtras().getString("jsonAtIndex");

        Gson gson = new Gson();

        Movie movie = gson.fromJson(json, Movie.class);


        TextView title = findViewById(R.id.title_tv);
        TextView desc = findViewById(R.id.desc_tv);
        TextView rating = findViewById(R.id.rating_tv);
        TextView releaseDate = findViewById(R.id.date_tv);
        ImageView imageView = findViewById(R.id.main_iv);
        ImageView poster = findViewById(R.id.poster_iv);


        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/original/"+movie.getBackdropPath())
                .into(imageView);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                .into(poster);

        setTitle(movie.getTitle());
        rating.setText("Rating - "+movie.getVoteAverage());
        releaseDate.setText("Release Date: "+movie.getReleaseDate());
        title.setText(movie.getTitle());
        desc.setText(movie.getOverview());

    }
}
