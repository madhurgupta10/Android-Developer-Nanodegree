package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        Sandwich s = new Sandwich();


        try {
            // Get JSON Object
            JSONObject jsonData = new JSONObject(json);

            // Get Name JSON Object inside JSON Object
            JSONObject name = jsonData.getJSONObject("name");

            // Set Main Name
            s.setMainName(name.getString("mainName"));

            // Set Also Known As
            JSONArray arrJson = name.getJSONArray("alsoKnownAs");
            List<String> arr = new ArrayList<>();
            for(int i = 0; i < arrJson.length(); i++)
                arr.add(arrJson.getString(i));
            s.setAlsoKnownAs(arr);

            // Set Place of Origin
            s.setPlaceOfOrigin(jsonData.getString("placeOfOrigin"));

            // Set Description
            s.setDescription(jsonData.getString("description"));

            // Set Image
            s.setImage(jsonData.getString("image"));

            // Set Ingredients
            JSONArray arrJson1 = jsonData.getJSONArray("ingredients");

            List<String> arr1 = new ArrayList<>();
            for(int i = 0; i < arrJson1.length(); i++)
                arr1.add(arrJson1.getString(i));
            s.setIngredients(arr1);

            return s;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
