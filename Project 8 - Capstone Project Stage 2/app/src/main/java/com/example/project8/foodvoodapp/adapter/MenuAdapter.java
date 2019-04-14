package com.example.project8.foodvoodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project8.foodvoodapp.R;
import com.example.project8.foodvoodapp.model.Menu;
import com.example.project8.foodvoodapp.model.Restaurant;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private int numberItems;
    private List<Menu> menus;
    private static Context context;
    private TextView amountView;
    private TextView quantView;
    private Restaurant restaurant;

    public MenuAdapter(int numberOfItems, List<Menu> menus,
                       Context context, TextView amountView, TextView quantView, Restaurant restaurant) {
        this.context = context;
        this.restaurant = restaurant;
        this.menus = menus;
        this.numberItems = numberOfItems;
        this.amountView = amountView;
        this.quantView = quantView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.menu_card_layout;
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

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView menuName;
        TextView description;
        TextView price;
        ImageView imageView;
        Button addButton;
        Button minusButton;

        MyViewHolder(View itemView) {
            super(itemView);
            menuName = itemView.findViewById(R.id.menu_name);
            description = itemView.findViewById(R.id.menu_desc);
            price = itemView.findViewById(R.id.menu_price);
            imageView = itemView.findViewById(R.id.cv_img);
            addButton = itemView.findViewById(R.id.plus);
            minusButton = itemView.findViewById(R.id.minus);
        }

        void bind(final int position) {
            if (menus != null) {
                menuName.setText(menus.get(position).getItemName());
                description.setText(menus.get(position).getShortDescription());
                price.setText(String.valueOf(menus.get(position).getAmount()));
                Picasso.with(context)
                        .load(menus.get(position).getImage())
                        .fit()
                        .centerCrop()
                        .into(imageView);

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringQ = (String) quantView.getText();
                        int q = Integer.parseInt(stringQ);
                        String stringAm = (String) amountView.getText();
                        float am = Float.parseFloat(stringAm);
                        if (am >= 0 && q >= 0) {
                            q++;
                            am = menus.get(position).getAmount() + am;
                            amountView.setText(String.valueOf(am));
                            quantView.setText(String.valueOf(q));
                        }
                    }
                });
                minusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stringQ = (String) quantView.getText();
                        int q = Integer.parseInt(stringQ);
                        String stringAm = (String) amountView.getText();
                        float am = Float.parseFloat(stringAm);
                        if (am > 0 && q > 0) {
                            q--;
                            am = am - menus.get(position).getAmount();
                            amountView.setText(String.valueOf(am));
                            quantView.setText(String.valueOf(q));


                        }
                    }
                });
            }
        }
    }
}
