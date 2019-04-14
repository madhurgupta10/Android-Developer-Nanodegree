package com.example.project8.foodvoodapp.adapter;

import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project8.foodvoodapp.R;
import com.example.project8.foodvoodapp.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.MyViewHolder> {

    private listItemClickListener onClickListener;
    private int numberItems;
    private ArrayList<Restaurant> restaurants;
    private static Context context;

    public interface listItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RestaurantAdapter(int numberOfItems, ArrayList<Restaurant> restaurants,
                             listItemClickListener onClickListener,
                         Context context) {
        this.context = context;
        this.restaurants = restaurants;
        this.onClickListener = onClickListener;
        this.numberItems = numberOfItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.restaurant_card_layout;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForList, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView restaurantName;
        TextView rating;
        TextView budget;
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            restaurantName = itemView.findViewById(R.id.restaurant_name);
            rating = itemView.findViewById(R.id.restaurant_rating);
            budget = itemView.findViewById(R.id.restaurant_budget);
            imageView = itemView.findViewById(R.id.cv_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }

        void bind(int position) {
            if (restaurants != null) {
                restaurantName.setText(restaurants.get(position).getRestaurantName());
                rating.setText(restaurants.get(position).getRating());
                budget.setText(restaurants.get(position).getBudget());
                Picasso.with(context)
                        .load(restaurants.get(position).getImage())
                        .fit()
                        .centerCrop()
                        .into(imageView);
            }
        }
    }
}
