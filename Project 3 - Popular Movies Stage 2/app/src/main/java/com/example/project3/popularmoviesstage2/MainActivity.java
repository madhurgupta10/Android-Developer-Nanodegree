package com.example.project3.popularmoviesstage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
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

import com.example.project3.popularmoviesstage2.data.TaskDbHelper;


public class MainActivity extends AppCompatActivity{

    public ProgressBar progressBar;
    private RecyclerView recyclerView;

    private SQLiteDatabase sqLiteDatabase;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TaskDbHelper taskDbHelper = new TaskDbHelper(this);

        Toast.makeText(this, "Database Created", Toast.LENGTH_SHORT).show();

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv);

        setTitle("Popular Movies - Popular");

        if (isNetworkConnected(this)) {
            progressBar.setVisibility(View.VISIBLE);
            new QueryTask(this, recyclerView, "popular", progressBar).execute();
        } else {
            progressBar.setVisibility(View.INVISIBLE);
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
                new QueryTask(this, recyclerView, "popular", progressBar).execute();

                setTitle("Popular Movies - Popular");

                Context context = MainActivity.this;
                String textToShow = "Popular";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                return true;

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }


        } else if (itemThatWasClickedId == R.id.top_rated) {

            if (isNetworkConnected(this)) {
                new QueryTask(this, recyclerView, "top_rated", progressBar).execute();

                setTitle("Popular Movies - Top Rated");

                Context context = MainActivity.this;
                String textToShow = "Top Rated";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }

        } else if (itemThatWasClickedId == R.id.favourite) {

            if (isNetworkConnected(this)) {
                new DatabaseQueryTask(this, recyclerView, "favourites", progressBar).execute();

                setTitle("Popular Movies - Favourites");

                Context context = MainActivity.this;
                String textToShow = "Favourites";
                Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
