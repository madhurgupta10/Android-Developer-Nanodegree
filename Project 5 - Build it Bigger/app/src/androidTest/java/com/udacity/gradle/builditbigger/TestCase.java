package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class TestCase extends AndroidTestCase {


    public void test() throws ExecutionException, InterruptedException {
        String result = "";
        result = new EndpointsAsyncTask().execute(new Pair<Context, String>(getContext(),
                "Manfred")).get();
        Log.d("Test", "test: "+result);
        assertTrue(result.length()!=0);
    }
}
