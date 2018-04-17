package com.example.project3.popularmoviesstage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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


public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<JSONArray> {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    String path;
    String title;
    boolean isOnlineQuery;
    JSONArray results;

    String key = BuildConfig.MY_MOVIE_DB_API_KEY;

    public static final int LOADER_ID = 22;
    public static final int LOADER_ID_DB = 44;

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
            boolean isOnlineQuery = savedInstanceState.getBoolean("isOnlineQuery");
            this.title = title;
            this.path = path;
            this.isOnlineQuery = isOnlineQuery;
            String s = savedInstanceState.getString("jsonArray");
            try {
                this.results = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<String> posterUrls = new ArrayList<>();
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
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    JSONObject jsonAtIndex = results.optJSONObject(clickedItemIndex);
                    intent.putExtra("jsonAtIndex", jsonAtIndex.toString());
                    intent.putExtra("apiKey", key);
                    MainActivity.this.startActivity(intent);
                }
            };

            MyAdapter myAdapter = new MyAdapter(results.length(), posterUrls,
                    listener, MainActivity.this, progressBar);

            recyclerView.setAdapter(myAdapter);

        } else {
            this.title = "Popular";
            this.path = "popular";
            this.isOnlineQuery = true;
            Query(this.path, this.title, this.isOnlineQuery);
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
            this.isOnlineQuery = true;
            Query("popular", "Popular",true);
            return true;

        } else if (itemThatWasClickedId == R.id.top_rated) {
            this.title = "Top Rated";
            this.path = "top_rated";
            this.isOnlineQuery = true;
            Query("top_rated", "Top Rated",true);
            return true;

        } else if (itemThatWasClickedId == R.id.favourite) {
            this.title = "Favourites";
            this.path = "favourites";
            this.isOnlineQuery = false;
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
        outState.putBoolean("isOnlineQuery", isOnlineQuery);
        outState.putString("jsonArray", String.valueOf(results));
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<JSONArray> onCreateLoader(final int ID, final Bundle ARGS) {
        return new AsyncTaskLoader<JSONArray>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (ARGS == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public JSONArray loadInBackground() {
                if (ID == LOADER_ID) {
                    String path = ARGS.getString("path");
                    if (path != null) {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://api.themoviedb.org/3/movie/" + path + "?api_key=" + key)
                                .build();
                        try {
                            Response response = client.newCall(request).execute();
                            String data = response.body().string();
                            JSONObject jsonData = new JSONObject(data);
                            return jsonData.optJSONArray("results");

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            return null;
                        }

                    } else {
                        return null;
                    }
                } else if (ID == LOADER_ID_DB){
                    Cursor cursor = MainActivity.this.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);

                    if (cursor != null && cursor.getCount()>0) {

                        cursor.moveToFirst();
                        int results = cursor.getCount();
                        int movieJsonIndex = cursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_JSON);

                        try {
                            JSONArray jsonArray = new JSONArray();
                            for (int i = 0; i < results; i++) {
                                JSONObject jsonData = new JSONObject(cursor.getString(movieJsonIndex));
                                jsonArray.put(jsonData);
                                cursor.moveToNext();
                            }
                            return jsonArray;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Sorry No Favourites", Toast.LENGTH_SHORT).show();
                    }
                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<JSONArray> loader, final JSONArray results) {
        this.results = results;
        if (results != null && results.length() != 0) {
            List<String> posterUrls = new ArrayList<>();
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
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    JSONObject jsonAtIndex = results.optJSONObject(clickedItemIndex);
                    intent.putExtra("jsonAtIndex", jsonAtIndex.toString());
                    intent.putExtra("apiKey", key);
                    MainActivity.this.startActivity(intent);
                }
            };

            MyAdapter myAdapter = new MyAdapter(results.length(), posterUrls,
                    listener, MainActivity.this, progressBar);

            recyclerView.setAdapter(myAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<JSONArray> loader) {
    }

    void Query(String path, String title, boolean isOnlineQuery) {
        if (isOnlineQuery) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("path", path);
            queryBundle.putString("title", title);
            queryBundle.putBoolean("isOnlineQuery", isOnlineQuery);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Object> loader = loaderManager.getLoader(LOADER_ID);

            if (isNetworkConnected(this)) {
                if (loader == null) {
                    loaderManager.initLoader(LOADER_ID, queryBundle,
                            this);
                } else {
                    loaderManager.restartLoader(LOADER_ID, queryBundle, this);
                }
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
            setTitle("Popular Movies - " + title);
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();

        } else {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("title", title);
            queryBundle.putString("path", path);
            queryBundle.putBoolean("isOnlineQuery", isOnlineQuery);

            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<Object> loader = loaderManager.getLoader(LOADER_ID_DB);

            if (loader == null) {
                loaderManager.initLoader(LOADER_ID_DB, queryBundle,
                        this);
            } else {
                loaderManager.restartLoader(LOADER_ID_DB, queryBundle, this);
            }
            setTitle("Popular Movies - " + title);
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
        }
    }
}