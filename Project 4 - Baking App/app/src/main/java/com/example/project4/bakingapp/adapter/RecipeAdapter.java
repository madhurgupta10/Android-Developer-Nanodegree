package com.example.project4.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.model.Recipe;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
        ImageView imageView;

        MyViewHolder(View itemView) {
            super(itemView);
            textViewRecipeName = itemView.findViewById(R.id.content);
            imageView = itemView.findViewById(R.id.cv_img);
            itemView.setOnClickListener(this);
        }

        @SuppressLint("ResourceAsColor")
        void bind(final int position) {
            if (recipeArrayList != null) {
                textViewRecipeName.setText(recipeArrayList.get(position).getName());

                if (recipeArrayList.get(position).getImage().length() == 0) {
                    String imgQuery = recipeArrayList.get(position).getName();
                    imgQuery = imgQuery.replaceAll("\\s+", "%20");
                    Ion.with(context)
                            .load("https://api.qwant.com/api/search/images?count=1&q=" +
                                    imgQuery)
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String jsonData) {
                                    try {
                                        Log.d("JSON", "onCompleted: " + jsonData);
                                        JSONObject json = new JSONObject(jsonData);
                                        if (json.optString("status").equals("success")) {
                                            JSONArray data = json.optJSONObject("data")
                                                    .optJSONObject("result")
                                                    .optJSONArray("items");
                                            JSONObject item = (JSONObject) data.opt(0);
                                            String imgUrl = item.optString("thumbnail");
                                            imgUrl = imgUrl.replaceFirst("//", "https://");
                                            recipeArrayList.get(position).setImage(imgUrl);
                                            Picasso.with(context)
                                                    .load(imgUrl)
                                                    .fit()
                                                    .centerCrop()
                                                    .into(imageView);
                                        } else {
                                            textViewRecipeName.setTextColor(Color.parseColor("#000000"));
                                        }

                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }
                                }
                            });
                }
            }
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            onClickListener.onListItemClick(clickedPosition);
        }
    }
}