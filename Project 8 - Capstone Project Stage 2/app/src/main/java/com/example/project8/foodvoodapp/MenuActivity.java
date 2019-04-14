package com.example.project8.foodvoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project8.foodvoodapp.adapter.MenuAdapter;
import com.example.project8.foodvoodapp.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {

    Restaurant restaurant;
    ArrayList<Restaurant> restaurants;
    int clickedItemIndex;
    TextView amountView;
    TextView quantView;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.menu_recyclerview);
        amountView = findViewById(R.id.amount);
        quantView = findViewById(R.id.quantity);
        imageButton = findViewById(R.id.checkout);

        ActionBar actionBar = getSupportActionBar();


        if (savedInstanceState != null) {

            clickedItemIndex = savedInstanceState.getInt(getString(R.string.index), 0);
            restaurants = (ArrayList<Restaurant>) savedInstanceState.getSerializable("restaurants");
            restaurant = restaurants.get(clickedItemIndex);
            amountView.setText(savedInstanceState.getString(getString(R.string.amount)));
            quantView.setText(savedInstanceState.getString(getString(R.string.quantity)));

        } else {
            final Intent intent = this.getIntent();
            Bundle bundle = intent.getBundleExtra(getString(R.string.bundle));
            clickedItemIndex = intent.getIntExtra(getString(R.string.index), 0);
            restaurants = (ArrayList<Restaurant>) bundle.getSerializable(getString(R.string.restauransBundle));
            restaurant = restaurants.get(clickedItemIndex);
        }

        setTitle(restaurant.getRestaurantName());

        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
            ImageView imageView = findViewById(R.id.up_iv);
            Picasso.with(this)
                    .load(restaurant.getImage())
                    .fit()
                    .centerCrop()
                    .into(imageView);
        }

        if (restaurants != null) {
            setupRecyclerView(recyclerView, restaurants, restaurant);
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String androidId = Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                final DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference();
//                final SimpleDateFormat formatter = new SimpleDateFormat("dd:MM:yyyy");
//                final Date date = new Date();

                mDatabase.child(androidId)
                        .child(getString(R.string.item)).setValue(restaurant.getMenu().get(0).getItemName());
                mDatabase.child(androidId)
                        .child(getString(R.string.quantity)).setValue(String.valueOf(quantView.getText()));
                mDatabase.child(androidId)
                        .child(getString(R.string.name)).setValue(restaurant.getRestaurantName());
                mDatabase.child(androidId)
                        .child(getString(R.string.amount)).setValue(String.valueOf(amountView.getText()));
                Intent intent = new Intent(MenuActivity.this, OrderActivity.class);
                MenuActivity.this.startActivity(intent);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView,
                                   ArrayList<Restaurant> restaurants, Restaurant restaurant) {

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(
                new MenuAdapter(restaurants.size(), restaurant.getMenu(), getApplicationContext(),
                        amountView, quantView, restaurant));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(getString(R.string.index), clickedItemIndex);
        outState.putSerializable(getString(R.string.restaurants), restaurants);
        outState.putString(getString(R.string.amount), (String) amountView.getText());
        outState.putString(getString(R.string.quantity), (String) quantView.getText());
    }
}
