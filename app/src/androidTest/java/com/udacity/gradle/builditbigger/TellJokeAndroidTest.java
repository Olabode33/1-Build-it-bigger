package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.util.Pair;
import android.test.ActivityInstrumentationTestCase2;
import android.test.InstrumentationTestCase;
import android.util.Log;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

@RunWith(AndroidJUnit4.class)
public class TellJokeAndroidTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;


    public TellJokeAndroidTest() {
        super(MainActivity.class);
    }

    @Before
    public void setup() throws Exception {
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mMainActivity = getActivity();
    }

    @Test
    public void testTellJokeTest() throws Throwable {

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                MainActivity.EndPointAsyncTask task = new MainActivity.EndPointAsyncTask();
                task.execute(new Pair<Context, String>(mMainActivity, ""));
                String s = "";
                try {
                    s = task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("TestJOke", s);
                assertThat(s, notNullValue());
            }
        });
    }
}
