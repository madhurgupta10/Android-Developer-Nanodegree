package com.example.project4.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v4.media.session.IMediaControllerCallback;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.model.Ingredient;
import com.example.project4.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.MyViewHolder> {

    private ArrayList<Ingredient> ingredients;
    private Recipe recipe;
    private Context context;
    private ImageView imageView;

    public RecipeIngredientAdapter(Recipe recipe, Context context, ImageView imageView) {
        this.imageView = imageView;
        this.context = context;
        this.recipe = recipe;
        this.ingredients = (ArrayList<Ingredient>) recipe.getIngredients();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.ingredient_layout;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForList, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        MyViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.ing_tv);
        }

        @SuppressLint("SetTextI18n")
        void bind(int position) {
            textView.setText(ingredients.get(position).getIngredient()+" ("+
                    ingredients.get(position).getQuantity()+ingredients.get(position).getMeasure()+")");

            if (recipe.getImage().length() != 0) {
                Picasso.with(context)
                        .load(recipe.getImage())
                        .fit()
                        .centerCrop()
                        .into(imageView);
            } else {
                textView.setTextColor(Color.parseColor("#000000"));
            }
        }
    }
}