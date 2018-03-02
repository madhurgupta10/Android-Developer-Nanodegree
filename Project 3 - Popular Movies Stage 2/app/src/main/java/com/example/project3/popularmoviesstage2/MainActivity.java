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

            PopulatePosters populatePosters = new PopulatePosters("popular", "Popular", true, this);
            return populatePosters.Query();

        } else if (itemThatWasClickedId == R.id.top_rated) {

            PopulatePosters populatePosters = new PopulatePosters("top_rated", "Top Rated", true, this);
            return populatePosters.Query();

        } else if (itemThatWasClickedId == R.id.favourite) {

            PopulatePosters populatePosters = new PopulatePosters("favourites", "Favourites", this);
            return populatePosters.Query();

        }
        return super.onOptionsItemSelected(item);
    }

    public class PopulatePosters {

        private String path;
        private boolean isOnlineQuery;
        private String title;
        private Context context;

        public PopulatePosters(String path, String title, Context context) {
            this.isOnlineQuery = false;
            this.path = path;
            this.title = title;
            this.context = context;
        }

        public PopulatePosters(String path, String title, boolean isOnlineQuery, Context context) {
            this.isOnlineQuery = isOnlineQuery;
            this.path = path;
            this.title = title;
            this.context = context;
        }

        public boolean Query() {
            if (isNetworkConnected(context)) {
                if (isOnlineQuery) {
                    new QueryTask(context, recyclerView, path, progressBar).execute();
                    setTitle("Popular Movies - " + title);
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    new DatabaseQueryTask(context, recyclerView, path, progressBar).execute();
                    setTitle("Popular Movies - " + title);
                    Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
                    return true;
                }

            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }
}
