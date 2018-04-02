package com.example.project3.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project3.popularmoviesstage2.data.TaskContract;
import com.example.project3.popularmoviesstage2.model.Movie;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Detail extends AppCompatActivity {

    LinearLayout movieTrailers;
    LinearLayout movieReviews;

    ProgressBar progressBarTrailers;
    ProgressBar progressBarReviews;
    ProgressBar progressBarBackdrop;

    JSONObject jsonDataReviews;
    JSONObject jsonDataTrailers;

    String dataReviews;
    String dataTrailers;
    String json;
    String key;

    int movieId;

    boolean isFavourite = false;

    FloatingActionButton floatingActionButton;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_detail);

        //Setting Action Bar to Transparent with no text so that we can add text later if we want
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        progressBarBackdrop = findViewById(R.id.progress_bar_backdrop);
        progressBarBackdrop.setVisibility(View.VISIBLE);

        dataReviews = "";
        dataTrailers = "";

        json = "";
        key = "";

        Gson gson = new Gson();

        if (json.length() == 0 || key.length() == 0) {

            if (isNetworkConnected(this)) {

                json = getIntent().getExtras().getString("jsonAtIndex");
                key = getIntent().getExtras().getString("apiKey");

                final Movie movie = gson.fromJson(json, Movie.class);

                movieId = movie.getId();

                movieTrailers = findViewById(R.id.movie_trailers);
                movieReviews = findViewById(R.id.movie_reviews);

                progressBarTrailers = findViewById(R.id.progress_bar_trailers);
                progressBarReviews = findViewById(R.id.progress_bar_reviews);

                floatingActionButton = findViewById(R.id.fab);
                floatingActionButton.hide();

                TextView title = findViewById(R.id.title_tv);
                TextView desc = findViewById(R.id.desc_tv);
                TextView rating = findViewById(R.id.rating_tv);
                final TextView releaseDate = findViewById(R.id.date_tv);
                ImageView backdropPath = findViewById(R.id.backdrop_iv);
                ImageView poster = findViewById(R.id.poster_iv);

                progressBarTrailers.setVisibility(View.VISIBLE);
                progressBarReviews.setVisibility(View.VISIBLE);


                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/original/"+movie.getBackdropPath())
                        .into(backdropPath);

                Picasso.with(this)
                        .load("http://image.tmdb.org/t/p/w185/"+movie.getPosterPath())
                        .into(poster);

                progressBarBackdrop.setVisibility(View.INVISIBLE);

                setTitle("");
                rating.setText(String.format("Rating - %s", movie.getVoteAverage()));
                releaseDate.setText(String.format("Release Date: %s", movie.getReleaseDate()));
                title.setText(movie.getTitle());
                desc.setText(movie.getOverview());


                //Check if the current movie is favourite or not
                new isFavouriteMovie(movie.getId()).execute();

                floatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("ResourceType")
                    public void onClick(View v) {
                        if (!isFavourite) {
                            Toast.makeText(Detail.this, "Added to Favourites", Toast.LENGTH_SHORT).show();
                            floatingActionButton.setImageResource(R.drawable.btn_star_big_on);

                            addToFavourites(movieId, json);
                            isFavourite = true;

                        } else if (isFavourite){
                            Toast.makeText(Detail.this, "Removed from Favourites", Toast.LENGTH_SHORT).show();
                            floatingActionButton.setImageResource(R.drawable.btn_star_big_off);
                            isFavourite = false;
                            removeFromFavourites(movieId);
                        }

                    }
                });

                if (dataTrailers.length() == 0) {

                    Ion.with(this)
                            .load("http://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=" + key)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String dataTrailers) {
                                    progressBarTrailers.setVisibility(View.INVISIBLE);
                                    Detail.this.dataTrailers = dataTrailers;
                                    populateTrailers();
                                }
                            });
                } else {
                    populateTrailers();
                }

                if (dataReviews.length() == 0) {

                    Ion.with(this)
                            .load("http://api.themoviedb.org/3/movie/" + movie.getId() + "/reviews?api_key=" + key)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String dataReviews) {
                                    Detail.this.dataReviews = dataReviews;
                                    progressBarReviews.setVisibility(View.INVISIBLE);
                                    populateReviews();
                                }
                            });
                } else {
                    populateReviews();
                }
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addToFavourites(Integer mid, String movieJson) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(TaskContract.TaskEntry.COLUMN_MID, mid);

        contentValues.put(TaskContract.TaskEntry.COLUMN_JSON, movieJson);

        getContentResolver().insert(TaskContract.TaskEntry.CONTENT_URI, contentValues);

    }

    private void removeFromFavourites(Integer mid) {

        String stringId = Integer.toString(mid);
        Uri uri = TaskContract.TaskEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        getContentResolver().delete(uri, null, null);
    }

    public class isFavouriteMovie extends AsyncTask<Void, Void, Cursor> {

        String[] id = new String[1];

        public isFavouriteMovie(Integer id) {
            this.id[0] = id.toString();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {

            Cursor cursor = getApplicationContext().getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                    null,
                    TaskContract.TaskEntry.COLUMN_MID + " = ? ",
                    id,
                    null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                isFavourite = true;
            } else {
                isFavourite = false;
            }
            if (isFavourite) {
                floatingActionButton.setImageResource(R.drawable.btn_star_big_on);
                floatingActionButton.show();
            } else {
                floatingActionButton.setImageResource(R.drawable.btn_star_big_off);
                floatingActionButton.show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("dataReviews", dataReviews);
        savedInstanceState.putString("dataTrailers", dataTrailers);
        savedInstanceState.putString("json", json);
        savedInstanceState.putString("key", key);
    }


    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        dataTrailers = savedInstanceState.getString("dataTrailers");
        dataReviews = savedInstanceState.getString("dataReviews");
        key = savedInstanceState.getString("key");
        json = savedInstanceState.getString("json");
    }

    private void populateTrailers() {
        try {
            jsonDataTrailers = new JSONObject(dataTrailers);
            JSONArray results = jsonDataTrailers.optJSONArray("results");

            if (results.length() > 0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.optJSONObject(i);
                    final String youtubeKey = result.optString("key");
                    ImageView imageView = new ImageView(Detail.this);

                    Picasso.with(Detail.this)
                            .load("https://img.youtube.com/vi/" + youtubeKey + "/0.jpg")
                            .into(imageView);
                    movieTrailers.addView(imageView);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent youtubePlay = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + youtubeKey));
                            Intent select = Intent.createChooser(youtubePlay, "Open With");

                            if (youtubePlay.resolveActivity(getPackageManager()) != null) {
                                startActivity(select);
                            }
                        }
                    });
                    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                    marginParams.setMargins(5, 0, 5, 10);
                }
            } else {
                TextView textView = new TextView(Detail.this);
                textView.setText("Sorry No Trailers Found");
                textView.setTextColor(Color.parseColor("#abb4c6"));
                textView.setTextSize(30);
                movieTrailers.addView(textView);
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
                marginParams.setMargins(40, 20, 20, 20);
            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }

    private void populateReviews() {
        try {
            jsonDataReviews = new JSONObject(dataReviews);
            JSONArray results = jsonDataReviews.optJSONArray("results");
            if (results.length() > 0) {
                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.optJSONObject(i);
                    final String author = result.optString("author");
                    final String review = result.optString("content");

                    LinearLayout linearLayout = new LinearLayout(Detail.this);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView textViewAuthor = new TextView(Detail.this);
                    textViewAuthor.setText(author);
                    textViewAuthor.setTextColor(Color.parseColor("#FFFFFF"));

                    linearLayout.addView(textViewAuthor);

                    ViewGroup.LayoutParams params = textViewAuthor.getLayoutParams();
                    params.width = 200;
                    textViewAuthor.setLayoutParams(params);

                    ViewGroup.MarginLayoutParams marginParamsAuthor = (ViewGroup.MarginLayoutParams) textViewAuthor.getLayoutParams();
                    marginParamsAuthor.setMargins(40, 20, 20, 20);

                    TextView textViewReview = new TextView(Detail.this);
                    textViewReview.setText(review);
                    textViewReview.setTextColor(Color.parseColor("#abb4c6"));

                    linearLayout.addView(textViewReview);
                    ViewGroup.MarginLayoutParams marginParamsReview = (ViewGroup.MarginLayoutParams) textViewReview.getLayoutParams();
                    marginParamsReview.setMargins(40, 20, 20, 20);

                    movieReviews.addView(linearLayout);

                }
            } else {
                TextView textView = new TextView(Detail.this);
                textView.setText("Sorry No Reviews Found");
                textView.setTextColor(Color.parseColor("#abb4c6"));
                textView.setTextSize(30);
                movieReviews.addView(textView);
                ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) textView.getLayoutParams();
                marginParams.setMargins(40, 20, 20, 20);
            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        }
    }
}
