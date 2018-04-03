package com.example.project4.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.model.Ingredient;

import java.util.ArrayList;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.MyViewHolder> {

    private ArrayList<Ingredient> ingredients;
    private Context context;

    public RecipeIngredientAdapter(ArrayList<Ingredient> ingredients, Context context) {
        this.context = context;
        this.ingredients = ingredients;
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
        }
    }
}