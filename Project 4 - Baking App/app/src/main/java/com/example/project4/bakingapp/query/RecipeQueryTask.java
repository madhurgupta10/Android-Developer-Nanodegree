package com.example.project4.bakingapp.query;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.project4.bakingapp.RecipeStepListActivity;
import com.example.project4.bakingapp.adapter.RecipeAdapter;
import com.example.project4.bakingapp.model.Recipe;
import com.google.gson.Gson;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecipeQueryTask extends AsyncTask<Void, Void, String>
        implements RecipeAdapter.listItemClickListener{

    private String queryUrl;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<Recipe> recipeArrayList = new ArrayList<Recipe>();
    private RecipeAdapter.listItemClickListener onClickListener;
    private boolean isTablet;

    public RecipeQueryTask(String queryUrl, Context context, RecyclerView recyclerView, boolean isTablet) {
        this.queryUrl = queryUrl;
        this.context = context;
        this.recyclerView = recyclerView;
        this.onClickListener = this;
        this.isTablet = isTablet;
    }

    @Override
    protected String doInBackground(Void... params) {

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
    }

    @Override
    protected void onPostExecute(String data) {
        if (data != null) try {
            JSONArray recipes = new JSONArray(data);
            for (int i = 0; i < recipes.length(); i++) {
                Gson gson = new Gson();
                final Recipe recipe = gson.fromJson(String.valueOf(recipes.optJSONObject(i)),
                        Recipe.class);

                recipeArrayList.add(recipe);
            }

            if (isTablet) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                recyclerView.setLayoutManager(gridLayoutManager);
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                recyclerView.setLayoutManager(gridLayoutManager);
            }

            RecipeAdapter recipeAdapter = new RecipeAdapter(recipes.length(), recipeArrayList,
                    onClickListener, context);
            recyclerView.setAdapter(recipeAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(context, RecipeStepListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("RecipeArrayListBundle", recipeArrayList);
        intent.putExtra("bundle", bundle);
        intent.putExtra("index", clickedItemIndex);
        context.startActivity(intent);
    }
}
