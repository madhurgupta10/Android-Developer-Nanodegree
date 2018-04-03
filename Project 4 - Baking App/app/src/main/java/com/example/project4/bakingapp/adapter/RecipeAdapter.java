package com.example.project4.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {

    final private listItemClickListener onClickListener;
    private int numberItems;
    private List<Recipe> recipeArrayList;
    private static Context context;

    public interface listItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(int numberOfItems, List<Recipe> recipeArrayList, listItemClickListener onClickListener,
                         Context context) {
        this.context = context;
        this.recipeArrayList = recipeArrayList;
        this.onClickListener = onClickListener;
        this.numberItems = numberOfItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForList = R.layout.recipe_card_layout;
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
        return numberItems;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewRecipeName;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewRecipeName = itemView.findViewById(R.id.content);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        void bind(int position) {
            if (recipeArrayList != null) {
                textViewRecipeName.setText(recipeArrayList.get(position).getName());
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}