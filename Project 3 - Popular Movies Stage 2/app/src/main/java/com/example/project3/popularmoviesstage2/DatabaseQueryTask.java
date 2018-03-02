package com.example.project3.popularmoviesstage2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.project3.popularmoviesstage2.data.TaskContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class DatabaseQueryTask extends AsyncTask<Void, Void, Cursor> implements MyAdapter.listItemClickListener {

    private List<String> posterUrls = new ArrayList<String>();

    Cursor mData;

    private ProgressBar progressBar;
    private String path;
    private Context context;
    private RecyclerView recyclerView;
    private MyAdapter.listItemClickListener listener;
    private String key;

    DatabaseQueryTask(Context context, RecyclerView recyclerView, String path, ProgressBar progressBar) {
        this.progressBar = progressBar;
        this.path = path;
        this.context = context;
        this.recyclerView = recyclerView;
        this.listener = this;
        this.key = BuildConfig.MY_MOVIE_DB_API_KEY;
    }

    @Override
    protected Cursor doInBackground(Void... params) {
        Cursor cursor = context.getContentResolver().query(TaskContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        return cursor;
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);
        if (cursor!=null && cursor.getCount()>0) {

            mData = cursor;
            mData.moveToFirst();

            int results = mData.getCount();

            int movieJsonIndex = mData.getColumnIndex(TaskContract.TaskEntry.COLUMN_JSON);

            try {
                for (int i = 0; i < results; i++) {
                    JSONObject jsonData = new JSONObject(mData.getString(movieJsonIndex));
                    String poster_path = jsonData.optString("poster_path");
                    posterUrls.add(poster_path);
                    mData.moveToNext();
                }

                final int orientation = context.getResources().getConfiguration().orientation;

                if (orientation == 1) {
                    // If Portrait set to 2 columns
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    MyAdapter myAdapter = new MyAdapter(results, posterUrls, listener, context, progressBar);

                    recyclerView.setAdapter(myAdapter);

                } else {
                    // Else set to 3 columns
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
                    recyclerView.setLayoutManager(gridLayoutManager);

                    recyclerView.setHasFixedSize(true);

                    MyAdapter myAdapter = new MyAdapter(results, posterUrls, listener, context, progressBar);

                    recyclerView.setAdapter(myAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "Sorry No Favourites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(context, Detail.class);
        mData.moveToPosition(clickedItemIndex);
        int movieJsonIndex = mData.getColumnIndex(TaskContract.TaskEntry.COLUMN_JSON);

        intent.putExtra("jsonAtIndex", mData.getString(movieJsonIndex));
        intent.putExtra("apiKey", key);
        context.startActivity(intent);
    }
}
