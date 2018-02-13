package com.example.project2.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements MyAdapter.listItemClickListner{

    private static final int numberOfItems = 4;
    private static final List<String> posterUrls = null;
    private Toast mToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.rv);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        //recyclerView.setLayoutManager(gridLayoutManager);

        //recyclerView.setHasFixedSize(true);

        //MyAdapter myAdapter = new MyAdapter(numberOfItems, posterUrls, MainActivity.this, this);

       // recyclerView.setAdapter(myAdapter);

        new QueryTask(this, recyclerView, MainActivity.this).execute();

    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mToast != null) {
            mToast.cancel();
        }

        String toastMessage = "Item #" + clickedItemIndex + " clicked.";
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }

}

