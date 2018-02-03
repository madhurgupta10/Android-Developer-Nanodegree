package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
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
            String[] arr = new String[arrJson.length()];
            for(int i = 0; i < arrJson.length(); i++)
                arr[i] = arrJson.getString(i);

            List<String> list = Arrays.asList(arr);
            s.setIngredients(list);

            // Set Place of Origin
            s.setPlaceOfOrigin(jsonData.getString("placeOfOrigin"));

            // Set Description
            s.setDescription(jsonData.getString("description"));

            // Set Image
            s.setImage(jsonData.getString("image"));

            // Set Ingredients
            JSONArray arrJson1 = jsonData.getJSONArray("ingredients");

            Log.d("arrJson1", ""+arrJson1);
            String[] arr1 = new String[arrJson1.length()];
            for(int i = 0; i < arrJson1.length(); i++)
                arr1[i] = arrJson1.getString(i);

            List<String> list1 = Arrays.asList(arr1);
            s.setIngredients(list1);

            return s;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
