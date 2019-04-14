package com.example.project8.foodvoodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.project8.foodvoodapp.adapter.RestaurantAdapter;
import com.example.project8.foodvoodapp.model.Restaurant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {

    private String queryUrl;
    private static final int LOADER_ID = 22;
    private String data;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.restaurant_recyclerview);

        if (isNetworkConnected(this)) {

            AdView mAdView = (AdView) findViewById(R.id.adView);
            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device. e.g.
            // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mAdView.loadAd(adRequest);

            if (savedInstanceState == null) {

                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]
                            {android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                } else {
                    Location location = null;
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    }

                    if (location != null) {

                        queryUrl = getString(R.string.url_base) + location.getLongitude() + getString(R.string.url_extra) + location.getLatitude();

                        Bundle queryBundle = new Bundle();
                        queryBundle.putString(getString(R.string.query_url), queryUrl);

                        LoaderManager loaderManager = getSupportLoaderManager();
                        Loader<Object> loader = loaderManager.getLoader(LOADER_ID);

                        if (loader == null) {
                            loaderManager.initLoader(LOADER_ID, queryBundle,
                                    this);
                        } else {
                            loaderManager.restartLoader(LOADER_ID, queryBundle, this);
                        }
                    } else {
                        Toast.makeText(this, R.string.gps, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                String data = savedInstanceState.getString(getString(R.string.data));
                this.data = data;
                try {
                    setRecyclerView(data, recyclerView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(getString(R.string.data), data);
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle ARGS) {
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
                if (ARGS != null) {
                    queryUrl = ARGS.getString(getString(R.string.query_url));

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(queryUrl)
                            .build();

                    String data = null;

                    try {
                        Response response = client.newCall(request).execute();
                        assert response.body() != null;
                        data = response.body().string();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return data;
                } else {
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        this.data = data;
        if (data != null) {
            try {
                setRecyclerView(data, recyclerView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        try {
            setRecyclerView(data, recyclerView);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setRecyclerView(String data, RecyclerView recyclerView)
            throws JSONException {

        if (data != null) {

            final ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();

            JSONArray restaurantsArray = new JSONArray(data);

            if (restaurantsArray.length() > 0) {

                for (int i = 0; i < restaurantsArray.length(); i++) {
                    Gson gson = new Gson();
                    Restaurant restaurant = gson.fromJson(String.valueOf(restaurantsArray.optJSONObject(i)),
                            Restaurant.class);
                    restaurants.add(restaurant);
                }

                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
                recyclerView.setLayoutManager(gridLayoutManager);

                RestaurantAdapter.listItemClickListener onClickListener =
                        new RestaurantAdapter.listItemClickListener() {
                            @Override
                            public void onListItemClick(int clickedItemIndex) {
                                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(getString(R.string.restbundle), restaurants);
                                intent.putExtra(getString(R.string.bundle), bundle);
                                intent.putExtra(getString(R.string.index), clickedItemIndex);
                                MainActivity.this.startActivity(intent);
                            }
                        };

                RestaurantAdapter restaurantAdapter = new RestaurantAdapter(restaurantsArray.length(),
                        restaurants,
                        onClickListener, this);
                recyclerView.setAdapter(restaurantAdapter);
            }
        }
    }
}
