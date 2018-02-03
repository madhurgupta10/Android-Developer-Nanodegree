package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.Iterator;
import java.util.List;


public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        //Set Title on Action Bar
        setTitle(sandwich.getMainName());

        //Set Other Views by Calling populateUI function
        populateUI(sandwich);

        //Set Image to ImageView
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich s) {
        //Get Description
        String description =  s.getDescription();

        //Get Place of Origin
        String origin = s.getPlaceOfOrigin();

        //Get Also Known As
        List<String> alsoKnownAs = s.getAlsoKnownAs();
        String alsoKnownAsString = "";

        if (alsoKnownAs != null) {
            Iterator itr = alsoKnownAs.iterator();

            while (itr.hasNext()) {
                alsoKnownAsString = alsoKnownAsString + itr.next() + "\n";
            }
        }

        //Get Ingredients
        List<String> ingredients = s.getIngredients();
        String ingredientsString = "";

        if (ingredients != null) {
            Iterator itr1 = ingredients.iterator();

            while (itr1.hasNext()) {
                ingredientsString = ingredientsString + itr1.next() + "\n";
            }
        }

        //Set Description to TextView
        TextView textView = findViewById(R.id.description_tv);
        textView.setText(description);

        //Set Place of Origin to TextView
        TextView textView1 = findViewById(R.id.origin_tv);
        textView1.setText(origin);

        //Set Also Known As to TextView
        TextView textView2 = findViewById(R.id.also_known_tv);
        textView2.setText(alsoKnownAsString);

        //Set Also Known As to TextView
        TextView textView3 = findViewById(R.id.ingredients_tv);
        textView3.setText(ingredientsString);
    }
}
