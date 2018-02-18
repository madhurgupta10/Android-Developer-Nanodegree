package com.example.project3.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class QueryTask extends AsyncTask<Void, Void, String> implements MyAdapter.listItemClickListener{

    private List<String> posterUrls = new ArrayList<String>();
    JSONArray results;

    private ProgressBar spinner;
    private String path;
    private String key;
    private Context context;
    private RecyclerView recyclerView;
    private MyAdapter.listItemClickListener listener;

    QueryTask(Context context, RecyclerView recyclerView, String path, ProgressBar progressBar) {
        this.spinner = progressBar;
        this.path = path;
        this.context = context;
        this.recyclerView = recyclerView;
        this.listener = this;
        this.key = "your api key";
    }


    @Override
    protected String doInBackground(Void... params) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.themoviedb.org/3/movie/"+path+"?api_key="+key)
                .build();

        String data = null;

        try {
            Response response = client.newCall(request).execute();

            data = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        if (data != null) {

            try {
                JSONObject jsonData = new JSONObject(data);
                results = jsonData.optJSONArray("results");


                for(int i = 0; i < results.length(); i++) {
                    JSONObject result = results.optJSONObject(i);
                    String poster_path = result.optString("poster_path");
                    posterUrls.add(poster_path);
                }

                final int orientation = context.getResources().getConfiguration().orientation;

                if (orientation == 1) {
                    // If Portrait set to 2 columns
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    MyAdapter myAdapter = new MyAdapter(results.length(), posterUrls, listener, context, spinner);

                    recyclerView.setAdapter(myAdapter);

                } else {
                    // Else set to 3 columns
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    MyAdapter myAdapter = new MyAdapter(results.length(), posterUrls, listener, context, spinner);

                    recyclerView.setAdapter(myAdapter);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        Intent intent = new Intent(context, Detail.class);
        JSONObject jsonAtIndex = results.optJSONObject(clickedItemIndex);
        intent.putExtra("jsonAtIndex", jsonAtIndex.toString());
        intent.putExtra("apiKey", key);
        context.startActivity(intent);

    }
}
