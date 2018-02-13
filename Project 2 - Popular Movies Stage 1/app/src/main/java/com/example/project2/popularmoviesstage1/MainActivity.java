package com.example.project2.popularmoviesstage1;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{

    ProgressBar spinner;
    RecyclerView recyclerView;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.progressBar1);

        recyclerView = findViewById(R.id.rv);

        setTitle("Popular Movies - Popular");

        if (isNetworkConnected(this)) {
            spinner.setVisibility(View.VISIBLE);
            new QueryTask(this, recyclerView, "popular").execute();
        } else {
            spinner.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();
        if (itemThatWasClickedId == R.id.popular) {

            if (isNetworkConnected(this)) {
                new QueryTask(this, recyclerView, "popular").execute();

                setTitle("Popular Movies - Popular");

                Context context = MainActivity.this;
                String textToShow = "Popular";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                return true;

            } else {
                spinner.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }


        } else if (itemThatWasClickedId == R.id.top_rated) {

            if (isNetworkConnected(this)) {
                new QueryTask(this, recyclerView, "top_rated").execute();

                setTitle("Popular Movies - Top Rated");

                Context context = MainActivity.this;
                String textToShow = "Top";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                spinner.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}

