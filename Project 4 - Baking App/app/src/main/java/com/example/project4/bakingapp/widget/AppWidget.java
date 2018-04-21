package com.example.project4.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.example.project4.bakingapp.R;
import com.example.project4.bakingapp.model.Ingredient;

import java.util.List;

import io.paperdb.Paper;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        Paper.init(context);
        List<Ingredient> ingredients = Paper.book().read("Ingredients");
        StringBuilder textToDisplay = new StringBuilder(new StringBuilder());
        
        for (int i = 0; i < ingredients.size(); i++) {
            textToDisplay.append(i + 1).append(". ").append(String.format("%s (%s%s)", ingredients.get(i).getIngredient(),
                    ingredients.get(i).getQuantity(), ingredients.get(i).getMeasure()));
            textToDisplay.append("\n");
        }

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.appwidget_text, textToDisplay.toString());

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

