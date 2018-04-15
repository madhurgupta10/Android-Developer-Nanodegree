package com.example.project3.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project3.popularmoviesstage2.data.TaskContract;
import com.example.project3.popularmoviesstage2.data.TaskDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String path;
    String title;

    String key = BuildConfig.MY_MOVIE_DB_API_KEY;

    public static final int LOADER_ID = 22;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new TaskDbHelper(this);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rv);

        if (savedInstanceState != null) {
            String path = savedInstanceState.getString("path");
            String title = savedInstanceState.getString("Title");
            this.title = path;
            this.path = title;
            Toast.makeText(this, "savedInstanceState True "+this.title, Toast.LENGTH_SHORT).show();
            Query(path, title, true);
        } else {
            this.title = "Popular";
            this.path = "popular";
            Query(path, "Popular", true);
            getLoaderManager().initLoader(LOADER_ID, null, this);
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
            this.title = "Popular";
            this.path = "popular";
            Query("popular", "Popular",true);
            return true;

        } else if (itemThatWasClickedId == R.id.top_rated) {
            this.title = "Top Rated";
            this.path = "top_rated";
            Query("top_rated", "Top Rated",true);
            return true;

        } else if (itemThatWasClickedId == R.id.favourite) {
            this.title = "Favourites";
            this.path = "favourites";
            Query("favourites", "Favourites",false);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", path);
        outState.putString("Title", title);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);
        path = savedInstanceState.getString("path");
        title = savedInstanceState.getString("Title");


    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle ARGS) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (ARGS == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String path = ARGS.getString("path");
                if (path != null) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://api.themoviedb.org/3/movie/" + path + "?api_key=" + key)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        return response.body().string();

                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        if (data != null && data.length() != 0) {
            try {
                JSONObject jsonData = new JSONObject(data);
                final JSONArray results = jsonData.optJSONArray("results");
                List<String> posterUrls = new ArrayList<String>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject result = results.optJSONObject(i);
                    String poster_path = result.optString("poster_path");
                    posterUrls.add(poster_path);
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,
                        2);
                recyclerView.setLayoutManager(gridLayoutManager);

                recyclerView.setHasFixedSize(true);
                MyAdapter.listItemClickListener listener = new MyAdapter.listItemClickListener() {
                    @Override
                    public void onListItemClick(int clickedItemIndex) {
                        Intent intent = new Intent(MainActivity.this, Detail.class);
                        JSONObject jsonAtIndex = results.optJSONObject(clickedItemIndex);
                        intent.putExtra("jsonAtIndex", jsonAtIndex.toString());
                        intent.putExtra("apiKey", key);
                        MainActivity.this.startActivity(intent);
                    }
                };

                MyAdapter myAdapter = new MyAdapter(results.length(), posterUrls,
                        listener, MainActivity.this, progressBar);

                recyclerView.setAdapter(myAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    void Query(String path, String title, boolean isOnlineQuery) {
        if (isOnlineQuery) {
            if (isNetworkConnected(this)) {
                Bundle queryBundle = new Bundle();
                queryBundle.putString("path", path);
                queryBundle.putString("title", title);

                LoaderManager loaderManager = getLoaderManager();
                Loader<Object> loader = loaderManager.getLoader(LOADER_ID);

                if (loader == null) {
                    loaderManager.initLoader(LOADER_ID, queryBundle,
                            this);
                } else {
                    loaderManager.restartLoader(LOADER_ID, queryBundle,
                            this);
                }
                setTitle("Popular Movies - " + title);
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            new DatabaseQueryTask(this, recyclerView, path, progressBar).execute();
            setTitle("Popular Movies - " + title);
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        }
    }
}
