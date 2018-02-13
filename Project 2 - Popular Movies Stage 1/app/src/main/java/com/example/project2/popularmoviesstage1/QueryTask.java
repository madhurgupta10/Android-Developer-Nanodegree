package com.example.project2.popularmoviesstage1;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QueryTask extends AsyncTask<Void, Void, String> {

    private Context c;
    private RecyclerView r;
    private MyAdapter.listItemClickListner listner;

    QueryTask(Context context, RecyclerView recyclerView, MyAdapter.listItemClickListner l) {
        c = context;
        r = recyclerView;
        listner = l;
    }


    @Override
    protected String doInBackground(Void... params) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.themoviedb.org/3/movie/popular?api_key={your api key}")
                .build();

        String data = null;

        try {
            Response response = client.newCall(request).execute();

            data = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("ASYNC", "doInBackground() called with: " + "params = [" + data + "]");
        return data;
    }

    @Override
    protected void onPostExecute(String data) {
        if (data != null) {

            try {
                JSONObject jsonData = new JSONObject(data);
                JSONArray results = jsonData.optJSONArray("results");

                List<String> posterUrls = new ArrayList<String>();

                for(int i = 0; i < results.length(); i++) {
                    JSONObject result = results.optJSONObject(i);
                    String poster_path = result.optString("poster_path");
                    posterUrls.add(poster_path);
                }

                //Toast.makeText(c, ""+posterUrls, Toast.LENGTH_LONG).show();

                GridLayoutManager gridLayoutManager = new GridLayoutManager(c, 2);
                r.setLayoutManager(gridLayoutManager);

                r.setHasFixedSize(true);

                MyAdapter myAdapter = new MyAdapter(10, posterUrls, listner, c);

                r.setAdapter(myAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
