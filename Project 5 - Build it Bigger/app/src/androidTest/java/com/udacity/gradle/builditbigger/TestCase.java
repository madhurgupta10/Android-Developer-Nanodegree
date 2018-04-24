package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;

import java.util.concurrent.ExecutionException;

public class TestCase extends AndroidTestCase {


    public TestCase() {
        String result = null;
        try {
            result = new EndpointsAsyncTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        assert result.length()!=0;
    }
}
