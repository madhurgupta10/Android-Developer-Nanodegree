package com.example.project2.popularmoviesstage1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;


public class MainActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar spinner;
        spinner = findViewById(R.id.progressBar1);

        spinner.setVisibility(View.VISIBLE);

        RecyclerView recyclerView = findViewById(R.id.rv);

        new QueryTask(this, recyclerView).execute();

    }

}

